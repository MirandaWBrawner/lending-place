package lendingplace.library.service;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lendingplace.library.dao.CategoryDao;
import lendingplace.library.dao.CommunityMemberDao;
import lendingplace.library.dao.LendableDao;
import lendingplace.library.dao.OnLoanDao;
import lendingplace.library.dao.PendingLoanDao;
import lendingplace.library.model.Category;
import lendingplace.library.model.CommunityMember;
import lendingplace.library.model.ItemOnLoan;
import lendingplace.library.model.Lendable;
import lendingplace.library.model.MultipleLendables;
import lendingplace.library.model.PendingLoan;
import lendingplace.library.model.User;
import lendingplace.library.request.LendableDetailsRequest;
import lendingplace.library.request.UpdateLendableRequest;

@Service
public class LendableService {
	
	private static final Logger logger = LoggerFactory.getLogger(LendableService.class);
	
	@Autowired
	private LendableCategoryService categoryService;
	
	@Autowired
	private LendableDao lendableDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private OnLoanDao onLoanDao;
	
	@Autowired
	private PendingLoanDao pendingLoanDao;
	
	@Autowired
	private CommunityMemberDao memberDao;

	public LendableDao getLendableDao() {
		return lendableDao;
	}
	public CategoryDao getCategoryDao() {
		return categoryDao;
	}
	public PendingLoanDao getPendingLoanDao() {
		return pendingLoanDao;
	}
	
	public int getMaxLendableId() {
		Pageable pageSettings = PageRequest.of(0, 1, Sort.by("id").descending());
		Page<Lendable> firstPage = lendableDao.findAll(pageSettings);
		if (firstPage.isEmpty() || firstPage.getContent().isEmpty()) {
			return 0;
		} else {
			return firstPage.getContent().get(0).getId();
		}
	}
	
	public long getMaxPendingLoanId() {
		Pageable pageSettings = PageRequest.of(0, 1, Sort.by("id").descending());
		Page<PendingLoan> firstPage = pendingLoanDao.findAll(pageSettings);
		if (firstPage.isEmpty() || firstPage.getContent().isEmpty()) {
			return 0;
		} else {
			return firstPage.getContent().get(0).getId();
		}
	}
	
	public Lendable buildLendable(LendableDetailsRequest request,
			LendableCategoryService categoryService) {
		if (request == null) {
			throw new IllegalArgumentException("The request is empty.");
		} else if (!(request instanceof UpdateLendableRequest) && request.getNumberAvailable() < 0) {
			throw new IllegalArgumentException("numberAvailable cannot be negative.");
		}
		Lendable lendable = new Lendable(request.getEnglish(), request.getHindi(), 
				request.getSwahili(), request.getArabic(), request.getMandarin(), 
				request.getSpanish(), request.getFrench(), request.getImagePath(), 
				request.getCreator(), request.getNumberAvailable());
		Set<Category> categories = categoryService.getCategoriesFromLendableRequest(request);
		for (Category category: categories) {
			System.out.printf("In LendableService.buildLendable: %s\n", category);
		}
		lendable.setCategories(categories);
		return lendable;
	}
	public void updateLendable(UpdateLendableRequest request, Lendable current, 
			LendableCategoryService categoryService) {
		Lendable changes = buildLendable(request, categoryService);
		if (request.getId() == null) {
			throw new IllegalArgumentException("The id is null");
		} 
		changes.getCategories().stream().forEach(
				newCategory -> current.addToCategory(newCategory));
		for (Method getMethod: Lendable.class.getMethods()) {

			if (getMethod.getName().startsWith("get")) {
				try {
					Object currentValue = getMethod.invoke(current);
					Object updatedValue = getMethod.invoke(changes);
					if (updatedValue != null && !updatedValue.equals(currentValue)) {
						String partialName = getMethod.getName().substring(3);
						if (!partialName.equals("Id") && !partialName.equals("Categories")) {
							Class<?> fieldType = getMethod.getReturnType();
							Method setMethod = Lendable.class.getMethod("set" + partialName, fieldType);
							setMethod.invoke(current, updatedValue);
						}
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				} 
				
			}
		}
	}
	/** Thank you to eugenp on github for pointing out PageImpl 
	 * https://github.com/eugenp/tutorials/issues/248 */
	public Page<Lendable> searchByNameAndCreator(String searchTerm, 
			Pageable overallPageSettings) {
		Set<Lendable> union = new HashSet<>();
		int resultsPerLanguage = 100;
		Pageable partialPageSettings = PageRequest.of(0, resultsPerLanguage);
		for (Method method: LendableDao.class.getDeclaredMethods()) {
			if (method.getName().startsWith("findBy") && method.getName().endsWith("Containing")
					&& !method.getName().contains("Categories")) {
				try {
					Object rawMethodResult = method.invoke(lendableDao, searchTerm, partialPageSettings);
					Page<Lendable> partialResultPage = (Page<Lendable>) rawMethodResult;
					for (Lendable record: partialResultPage) {
						union.add(record);
					}
				} catch (Exception exception) {
					logger.error(exception.toString());
				}
			}
		}
		Set<Category> categories = categoryService.searchByName(searchTerm);
		for (Category category: categories) {
			Page<Lendable> lendablesByCategory = lendableDao.findByCategoriesContaining(
					category, partialPageSettings);
			for (Lendable lendable: lendablesByCategory) {
				union.add(lendable);
			}
		}
		List<Lendable> list = union.parallelStream().collect(Collectors.toList());
		return new PageImpl<Lendable>(list, overallPageSettings, list.size());
	}
	
	public void addCommunityMemberToPendingLoan(CommunityMember member, PendingLoan loan) {
		loan.setMember(member);
		pendingLoanDao.save(loan);
	}
	
	public long getTimeOnLoan(Lendable lendable) {
		int days = 5;
		for (Category category: lendable.getCategories()) {
			if (category.getEnglish().equals("Books")) {
				days = 15;
				break;
			}
		}
		return days * 24 * 60 * 60 * 1000;
	}

	public PendingCheckoutResponse reserveItems(String name, List<MultipleLendables> lendables) {
		List<Integer> acceptedItemIds = new ArrayList<>();
		List<Integer> notFoundItemIds = new ArrayList<>();
		for (MultipleLendables group: lendables) {
			Optional<Lendable> possibleLendable = lendableDao.findById(group.getId());
			if (possibleLendable.isPresent()) {
				Lendable lendable = possibleLendable.get();
				boolean success = reserveOneLendable(name, lendable, group.getCount());
				if (success) {
					acceptedItemIds.add(group.getId());
				} else {
					notFoundItemIds.add(group.getId());
				}
			} else {
				logger.info(String.format("Lendable with id %d could not be found.", group.getId()));
				for (int i = 0; i < group.getCount(); i++) {
					notFoundItemIds.add(group.getId());
				}
			}
		}
		return new PendingCheckoutResponse(acceptedItemIds, notFoundItemIds);
	}
	
	@Transactional
	public boolean reserveOneLendable(String name, Lendable lendable, int count) {
		Timestamp reservedDate = new Timestamp(System.currentTimeMillis());
		long nextId = getMaxPendingLoanId() + 1;
		PendingLoan pendingLoan = new PendingLoan(nextId, lendable, count, name, reservedDate);
		try {
			pendingLoanDao.save(pendingLoan);
			return true;
		} catch (Exception exception) {
			logger.error(exception.toString());
			return false;
		}
	}
	
	public CheckoutResponse checkout(CommunityMember member, 
			List<MultipleLendables> lendables) {
		Timestamp checkoutDate = new Timestamp(System.currentTimeMillis() + 30_000);
		List<Integer> checkedOutItemIds = new ArrayList<>();
		List<Integer> notFoundItemIds = new ArrayList<>();
		List<ItemOnLoan> onLoanList = new ArrayList<>();
		for (MultipleLendables group: lendables) {
			Optional<Lendable> possibleLendable = lendableDao.findById(group.getId());
			if (possibleLendable.isPresent()) {
				Lendable lendable = possibleLendable.get();
				int copiesCheckedOut = 0;
				for (int i = 0; i < group.getCount(); i++) {
					int remaining = lendable.getNumberAvailable();
					if (remaining >= 1) {
						lendable.setNumberAvailable(remaining - 1);
						checkedOutItemIds.add(lendable.getId());
						copiesCheckedOut++;
					} else {
						notFoundItemIds.add(lendable.getId());
					}
				}
				lendableDao.save(lendable);
				for (int i = 0; i < copiesCheckedOut; i++) {
					long timeOnLoan = getTimeOnLoan(lendable);
					Timestamp dueDate = new Timestamp(checkoutDate.getTime() + timeOnLoan);
					ItemOnLoan item = new ItemOnLoan(member, lendable, 
							checkoutDate, dueDate);
					onLoanList.add(item);
				}
			} else {
				logger.info(String.format("Lendable with id %d could not be found.", group.getId()));
				for (int i = 0; i < group.getCount(); i++) {
					notFoundItemIds.add(group.getId());
				}
			}
		}
		onLoanDao.saveAll(onLoanList);
		return new CheckoutResponse(checkedOutItemIds, notFoundItemIds);
	}
	
	public void cancelPending(List<PendingLoan> pendingList) {
		pendingLoanDao.deleteInBatch(pendingList);
	}
	
	public boolean checkoutOneCopyFromPending(PendingLoan pendingLoan) {
		if (pendingLoan == null) return false;
		return checkoutOneCopyFromPending(pendingLoan, pendingLoan.getMember());
	}
	
	@Transactional
	public boolean checkoutOneCopyFromPending(PendingLoan pendingLoan, CommunityMember member) {
		if (pendingLoan == null) return false;
		if (member == null) return false;
		if (pendingLoan.getCount() < 1) return false;
		Lendable lendable = pendingLoan.getLendable();
		if (lendable == null) return false;
		Optional<Lendable> possibleItem = lendableDao.findById(lendable.getId());
		if (possibleItem.isEmpty()) return false;
		lendable = possibleItem.get();
		if (lendable.getNumberAvailable() < 1) return false;
		lendable.setNumberAvailable(lendable.getNumberAvailable() - 1);
		Timestamp checkoutDate = new Timestamp(System.currentTimeMillis());
		long timeOnLoan = getTimeOnLoan(lendable);
		Timestamp dueDate = new Timestamp(checkoutDate.getTime() + timeOnLoan);
		ItemOnLoan onLoan = new ItemOnLoan(member, lendable, checkoutDate, dueDate);
		pendingLoan.setCount(pendingLoan.getCount() - 1);
		lendableDao.save(onLoan.getLendable());
		memberDao.save(onLoan.getMember());
		onLoanDao.save(onLoan);
		if (pendingLoan.getCount() == 0) {
			pendingLoanDao.delete(pendingLoan);
		} else {
			pendingLoanDao.save(pendingLoan);
		}
		return true;
	}
	
	public void addCopyToLibrary(Lendable addition) {
		if (addition != null) {
			Optional<Lendable> possible = lendableDao.findById(addition.getId());
			if (possible.isPresent()) {
				Lendable existing = possible.get();
				existing.setNumberAvailable(existing.getNumberAvailable() + 1);
				lendableDao.save(existing);
			} else {
				lendableDao.save(addition);
			}
		}
		
	}
	
	@Transactional
	public void returnOneCopy(ItemOnLoan item) {
		onLoanDao.delete(item);
		addCopyToLibrary(item.getLendable());
	}
	
	public List<ItemOnLoan> findInstanceOnLoan(int lendableId, CommunityMember member) {
		List<ItemOnLoan> itemList = onLoanDao.findByMember(member);
		return itemList.stream().filter(instance -> instance.getLendable().getId() == lendableId)
		.collect(Collectors.toList());
	}
	
	public List<ItemOnLoan> findInstanceOnLoan(Lendable lendable, CommunityMember member) {
		return findInstanceOnLoan(lendable.getId(), member);
	}
	
	public Set<Integer> returnLendables(CommunityMember member, 
			List<MultipleLendables> lendables) {
		Set<Integer> errorSet = new HashSet<>();
		for (MultipleLendables group: lendables) {
			if (group == null) continue;
			Integer lendableId = group.getId();
			if (lendableId == null) continue;
			List<ItemOnLoan> itemList = findInstanceOnLoan(lendableId, member);
			for (ItemOnLoan item: itemList) {
				try {
					returnOneCopy(item);
				} catch (Exception exception) {
					errorSet.add(lendableId);
				}
			}
		}
		return errorSet;
	}
	
}
