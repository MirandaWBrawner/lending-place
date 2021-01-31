package lendingplace.library.requestHandler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lendingplace.library.model.Category;
import lendingplace.library.model.Lendable;
import lendingplace.library.request.AddLendableToCategoryRequest;
import lendingplace.library.request.DeleteItemRequest;
import lendingplace.library.request.LendableDetailsRequest;
import lendingplace.library.request.UpdateLendableRequest;
import lendingplace.library.service.LendableCategoryService;
import lendingplace.library.service.LendableService;

@RestController
@CrossOrigin
public class LendingRequestHandler {
	
	@Autowired
	private LendableService lendableService;
	
	@Autowired
	private LendableCategoryService categoryService;
	
	@GetMapping(path = "/browse/lendables", produces = "application/json")
	public Page<Lendable> getLendables(
			@RequestParam(name = "language", required = false) String language, 
			@RequestParam(name = "sortBy", required = false) String sortSetting,
			@RequestParam(name = "pageSize", required = false) Integer pageSize, 
			@RequestParam(name = "pageNumber", required = false) Integer pageNumber) {
		if (sortSetting == null) sortSetting = "creator";
		if (pageSize == null) pageSize = 20;
		if (pageSize < 1) pageSize = 1;
		if (pageNumber == null || pageNumber < 1) pageNumber = 1;
		Sort sortingOrder = Sort.by(sortSetting);
		Pageable pageSettings = PageRequest.of(pageNumber - 1, pageSize, sortingOrder);
		return lendableService.getLendableDao().findAll(pageSettings);
	}
	
	@GetMapping(path = "/search/lendables", produces = "application/json")
	public Page<Lendable> search(@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "pageSize", required = false) Integer pageSize, 
			@RequestParam(name = "pageNumber", required = false) Integer pageNumber) {
		if (pageSize == null) pageSize = 20;
		if (pageSize < 1) pageSize = 1;
		if (pageNumber == null || pageNumber < 1) pageNumber = 1;
		Pageable pageSettings = PageRequest.of(1- pageNumber, pageSize);
		return lendableService.searchByNameAndCreator(name, pageSettings);
	}
	
	@PostMapping(path = "/add/lendable")
	public ResponseEntity<?> addLendable(@RequestBody LendableDetailsRequest request) {
		Lendable newLendable = null; 
		try {
			newLendable = lendableService.buildLendable(request, categoryService);
			lendableService.getLendableDao().save(newLendable);
		} catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(exception.getMessage());
		}
		return ResponseEntity.ok("Success");
	}
	
	@PutMapping(path = "/update/lendable")
	public ResponseEntity<?> updateLendableById(@RequestBody UpdateLendableRequest request) {
		if (request == null) {
			return ResponseEntity.badRequest().body("The request is empty.");
		}
		if (request.getId() == null) {
			return ResponseEntity.badRequest().body("The id is null.");
		}
		Optional<Lendable> optional = lendableService.getLendableDao()
				.findById(request.getId());
		if (optional.isPresent()) {
			try {
				Lendable current = optional.get();
				lendableService.updateLendable(request, current, categoryService);
				lendableService.getLendableDao().save(current);
				return ResponseEntity.ok("Success");
			} catch (IllegalArgumentException exception) {
				return ResponseEntity.badRequest().body(exception.getMessage());
			} catch (Exception exception) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						"This part of the app is not working at the moment.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					"No lendable could be found that matches the id");
		}
	}
	
	@PutMapping(path = "/update/addToCategory")
	public ResponseEntity<?> addLendableToCategory(
			@RequestBody AddLendableToCategoryRequest request) {
		if (request == null) {
			return ResponseEntity.badRequest().body("The request is empty.");
		}
		Integer lendableId = request.getLendableId();
		if (lendableId == null) {
			return ResponseEntity.badRequest().body("The id of the lendable is null.");
		}
		Optional<Lendable> optionalLendable = lendableService
				.getLendableDao().findById(lendableId);
		if (optionalLendable.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					"No lendable could be found that matches the id");
		}
		Lendable lendable = optionalLendable.get();
		Integer categoryId = request.getCategoryId();
		if (categoryId == null) {
			return ResponseEntity.badRequest().body("The id of the category is null.");
		}
		Optional<Category> optionalCategory = categoryService
				.getCategoryDao().findById(categoryId);
		if (optionalCategory.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					"No category could be found that matches the id");
		}
		Category category = optionalCategory.get();
		if (lendable.addToCategory(category)) {
			lendableService.getLendableDao().save(lendable);
		}
		return ResponseEntity.ok("Success");
	}
	
	@DeleteMapping(path = "/delete/lendable") 
	public ResponseEntity<?> deleteLendableById(@RequestBody DeleteItemRequest request){
		if (request == null) {
			return ResponseEntity.badRequest().body("The request is empty.");
		}
		if (request.getId() == null) {
			return ResponseEntity.badRequest().body("The id is null.");
		}
		Optional<Lendable> optional = lendableService
				.getLendableDao().findById(request.getId());
		if (optional.isPresent()) {
			try {
				lendableService.getLendableDao().deleteById(request.getId());
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
