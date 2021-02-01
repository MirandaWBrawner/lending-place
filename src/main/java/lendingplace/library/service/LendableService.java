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
import org.springframework.stereotype.Service;

import lendingplace.library.dao.CategoryDao;
import lendingplace.library.dao.CommunityMemberDao;
import lendingplace.library.dao.LendableDao;
import lendingplace.library.dao.OnLoanDao;
import lendingplace.library.model.Category;
import lendingplace.library.model.CommunityMember;
import lendingplace.library.model.ItemOnLoan;
import lendingplace.library.model.Lendable;
import lendingplace.library.model.MultipleLendables;
import lendingplace.library.request.LendableDetailsRequest;
import lendingplace.library.request.UpdateLendableRequest;

@Service
public class LendableService {
	
	private static final Logger logger = LoggerFactory.getLogger(LendableService.class);
	
	@Autowired
	private LendableDao lendableDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private OnLoanDao onLoanDao;
	
	@Autowired
	private CommunityMemberDao memberDao;

	public LendableDao getLendableDao() {
		return lendableDao;
	}
	public CategoryDao getCategoryDao() {
		return categoryDao;
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
			if (method.getName().startsWith("findBy") && method.getName().endsWith("Containing")) {
				try {
					Object rawMethodResult = method.invoke(lendableDao, searchTerm, partialPageSettings);
					Page<Lendable> partialResultPage = (Page<Lendable>) rawMethodResult;
					for (Lendable record: partialResultPage) {
						union.add(record);
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		List<Lendable> list = union.parallelStream().collect(Collectors.toList());
		return new PageImpl<Lendable>(list, overallPageSettings, list.size());
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

	public CheckoutResponse checkout(CommunityMember member, List<MultipleLendables> lendables) {
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
					ItemOnLoan item = new ItemOnLoan(member, lendable, checkoutDate, dueDate);
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
	
	public List<ItemOnLoan> findInstanceOnLoan(int lendableId, CommunityMember member) {
		List<ItemOnLoan> itemList = onLoanDao.findByMember(member);
		return itemList.stream().filter(instance -> instance.getLendable().getId() == lendableId)
		.collect(Collectors.toList());
	}
	
	public List<ItemOnLoan> findInstanceOnLoan(Lendable lendable, CommunityMember member) {
		return findInstanceOnLoan(lendable.getId(), member);
		
	}
	
	public void returnLendables(CommunityMember member, List<MultipleLendables> lendables) {
		for (MultipleLendables group: lendables) {
			if (group == null) continue;
			Integer lendableId = group.getId();
			
		}
	}
}
