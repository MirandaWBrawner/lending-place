package lendingplace.library.service;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lendingplace.library.dao.CategoryDao;
import lendingplace.library.model.Category;
import lendingplace.library.request.CategoryDetailsRequest;
import lendingplace.library.request.LendableDetailsRequest;
import lendingplace.library.request.UpdateCategoryRequest;

@Service
public class LendableCategoryService {

	@Autowired
	private CategoryDao categoryDao;
	
	public CategoryDao getCategoryDao() {
		return this.categoryDao;
	}
	
	public Category buildCategory(CategoryDetailsRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("The request is empty.");
		} 
		return new Category(request.getEnglish(), request.getHindi(), request.getSwahili(),
				request.getArabic(), request.getMandarin(), request.getSpanish(),
				request.getFrench(), request.getImagePath());
	}
	public void updateCategory(UpdateCategoryRequest request, Category current) {
		Category changes = buildCategory(request);
		if (request.getId() == null) {
			throw new IllegalArgumentException("The id is null");
		} 
		for (Method getMethod: Category.class.getMethods()) {
			if (getMethod.getName().startsWith("get")) {
				try {
					Object currentValue = getMethod.invoke(current);
					Object updatedValue = getMethod.invoke(changes);
					if (updatedValue != null && !updatedValue.equals(currentValue)) {
						String partialName = getMethod.getName().substring(3);
						if (!partialName.equals("Id")) {
							Class<?> fieldType = getMethod.getReturnType();
							Method setMethod = Category.class.getMethod("set" + partialName, fieldType);
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
	public Page<Category> searchByName(String searchTerm, Pageable overallPageSettings,
			boolean exactMatch) {
		Set<Category> union = new HashSet<>();
		int resultsPerLanguage = 100;
		Pageable partialPageSettings = PageRequest.of(0, resultsPerLanguage);
		for (Method method: CategoryDao.class.getDeclaredMethods()) {
			if (method.getName().startsWith("findBy")
					&& method.getName().endsWith("Containing") != exactMatch) {
				try {
					Object rawMethodResult = method.invoke(categoryDao, searchTerm, partialPageSettings);
					Page<Category> partialResultPage = (Page<Category>) rawMethodResult;
					for (Category record: partialResultPage) {
						union.add(record);
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		List<Category> list = union.parallelStream().collect(Collectors.toList());
		return new PageImpl<Category>(list, overallPageSettings, list.size());
	}
	
	public Set<Category> searchByName(String searchTerm) {
		Set<Category> union = new HashSet<>();
		for (Method method: CategoryDao.class.getDeclaredMethods()) {
			if (method.getName().startsWith("findBy")
					&& !method.getName().endsWith("Containing")
					&& List.class.isAssignableFrom(method.getReturnType())) {
				try {
					Object rawMethodResult = method.invoke(categoryDao, searchTerm);
					List<Category> partialResultPage = (List<Category>) rawMethodResult;
					for (Category record: partialResultPage) {
						union.add(record);
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		return union;
	}
	public Set<Category> getCategoriesFromLendableRequest(LendableDetailsRequest request) {
		Set<Category> union = new HashSet<>();
		if (request.getCategories() != null) {
			for (String name: request.getCategories()) {
				searchByName(name).forEach(result -> union.add(result));
			}
		}
		return union;
	}
}
