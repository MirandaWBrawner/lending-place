package lendingplace.library.requestHandler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lendingplace.library.dao.CategoryDao;
import lendingplace.library.model.Category;
import lendingplace.library.model.Lendable;
import lendingplace.library.request.CategoryDetailsRequest;
import lendingplace.library.request.DeleteItemRequest;
import lendingplace.library.request.UpdateCategoryRequest;
import lendingplace.library.service.LendableCategoryService;
import lendingplace.library.service.PagingService;

@RestController
@CrossOrigin
public class CategoryRequestHandler {
	
	@Autowired
	private PagingService pagingService;
	
	@Autowired
	private LendableCategoryService categoryService;
	
	@GetMapping(path = "/browse/categories", produces = "application/json")
	public Page<Category> getCategories(
			@RequestParam(name = "language", required = false) String language,
			@RequestParam(name = "pageSize", required = false) Integer pageSize, 
			@RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
			@RequestParam(name = "sortBy", required = false) String sortBy) {
		Integer defaultPageSize = 20;
		Integer maxPageSize = 50;
		Integer defaultPageNumber = 1;
		Sort defaultSort = Sort.unsorted();
		Pageable pageSettings = pagingService.getPageSettings(sortBy, defaultSort, 
				pageSize, defaultPageSize, maxPageSize, pageNumber, defaultPageNumber);
		return categoryService.getCategoryDao().findAll(pageSettings);
	}
	
	@GetMapping(path = "/search/categories", produces = "application/json")
	public Page<Category> searchByName(@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "exactMatch", required = false) Boolean exactMatch,
			@RequestParam(name = "pageSize", required = false) Integer pageSize, 
			@RequestParam(name = "pageNumber", required = false) Integer pageNumber) {
		Integer defaultPageSize = 20;
		Integer defaultPageNumber = 1;
		Sort defaultSort = Sort.unsorted();
		boolean defaultExactMatch = false;
		exactMatch = exactMatch == null ? defaultExactMatch : exactMatch;
		Pageable pageSettings = pagingService.getPageSettings("unsorted", defaultSort, 
				pageSize, defaultPageSize, defaultPageSize, defaultPageNumber, defaultPageNumber);
		return categoryService.searchByName(name, pageSettings, exactMatch);
	}
	
	@PostMapping(path = "/add/category")
	@PreAuthorize("hasAuthority('Librarian')")
	public ResponseEntity<?> addCategory(@RequestBody CategoryDetailsRequest request) {
		Category newCategory = null; 
		try {
			newCategory = categoryService.buildCategory(request);
			categoryService.getCategoryDao().save(newCategory);
		} catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(exception.getMessage());
		}
		return ResponseEntity.ok("Success");
	}
	
	@PutMapping(path = "/update/category")
	@PreAuthorize("hasAuthority('Librarian')")
	public ResponseEntity<?> updateCategoryById(@RequestBody UpdateCategoryRequest request) {
		if (request == null) {
			return ResponseEntity.badRequest().body("The request is empty.");
		}
		if (request.getId() == null) {
			return ResponseEntity.badRequest().body("The id is null.");
		}
		Optional<Category> optional = categoryService.getCategoryDao()
				.findById(request.getId());
		if (optional.isPresent()) {
			try {
				Category current = optional.get();
				categoryService.updateCategory(request, current);
				categoryService.getCategoryDao().save(current);
				return ResponseEntity.ok("Success");
			} catch (IllegalArgumentException exception) {
				return ResponseEntity.badRequest().body(exception.getMessage());
			} catch (Exception exception) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						"This part of the app is not working at the moment.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					"No category could be found that matches the id");
		}
	}
	
	@DeleteMapping(path = "/delete/category")
	@PreAuthorize("hasAuthority('Librarian')")
	public ResponseEntity<?> deleteCategoryById(@RequestBody DeleteItemRequest request){
		if (request == null) {
			return ResponseEntity.badRequest().body("The request is empty.");
		}
		if (request.getId() == null) {
			return ResponseEntity.badRequest().body("The id is null.");
		}
		Optional<Category> optional = categoryService.getCategoryDao().findById(request.getId());
		if (optional.isPresent()) {
			try {
				categoryService.getCategoryDao().deleteById(request.getId());
				return ResponseEntity.ok("Success");
			} catch (Exception exception) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						"This part of the app is not working at the moment.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					"No lendable could be found that matches the id");
		}
	}
}
