package lendingplace.library.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PagingService {
	
	public Pageable getPageSettings(String givenSort, Sort defaultSort, 
			Integer givenPageSize, Integer defaultPageSize, Integer maxPageSize,
			Integer givenPageNumber, Integer defaultPageNumber) {
		
	/* If no page size is provided, use the default value.
	 If the page size is outside the acceptable range,
	 set it to the maximum size if it is too high, and set it to 1
	 if it is too low. */
	int pageSize = givenPageSize == null ? defaultPageSize: givenPageSize;
	if (pageSize > maxPageSize) pageSize = maxPageSize;
	if (pageSize < 1) pageSize = 1;
	
	/* If no page number is provided, or if the number is too low,
	  use the default page. */
	int pageNumber = defaultPageNumber;
	if (givenPageNumber != null && givenPageNumber >= 1) {
		pageNumber = givenPageNumber;
	}
	
	/* Use the default sorting order if the given sorting order is empty. 
	 * Set givenSort to "unsorted" to return the results without sorting them.*/
	Sort sortingOrder = defaultSort;
	if (givenSort != null) {
		if (givenSort.equalsIgnoreCase("unsorted")) {
			sortingOrder = Sort.unsorted();
		} else {
			sortingOrder = Sort.by(givenSort);
		}
	}
	return PageRequest.of(pageNumber - 1, pageSize, sortingOrder);
	}
}
