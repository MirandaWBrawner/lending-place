package lendingplace.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lendingplace.library.dao.UserDao;
import lendingplace.library.model.User;

@Service
public class AccountService {
	
	@Autowired
	private UserDao userDao;
	
	public int getMaxId() {
		Pageable pageSettings = PageRequest.of(0, 1, Sort.by("id").descending());
		Page<User> firstPage = userDao.findAll(pageSettings);
		if (firstPage.isEmpty() || firstPage.getContent().isEmpty()) {
			return 0;
		} else {
			return firstPage.getContent().get(0).getId();
		}
	}
}
