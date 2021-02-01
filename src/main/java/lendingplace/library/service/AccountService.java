package lendingplace.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
	
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
	
	public void updateLanguage(User user, String language) {
		if (user == null) {
			logger.error("Could not update language of null user.");
		} else if (language != null) {
			user.setLanguage(language);
			userDao.save(user);
		}
	}
}
