package lendingplace.library.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lendingplace.library.dao.UserDao;
import lendingplace.library.model.User;

@Service
public class LibrarianDetailsService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(LibrarianDetailsService.class);
	
	@Autowired
	UserDao dao;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User librarian = dao.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
		return LibrarianDetails.build(librarian);
	}
	
	public int getMaxId() {
		Pageable pageSettings = PageRequest.of(0, 1, Sort.by("id").descending());
		Page<User> firstPage = dao.findAll(pageSettings);
		if (firstPage.isEmpty() || firstPage.getContent().isEmpty()) {
			return 0;
		} else {
			return firstPage.getContent().get(0).getId();
		}
	}
	
	public String updateLanguage(User user, String language) {
		if (user == null) {
			logger.error("Cannot update language of null user.");
			return language;
		} else if (language == null) {
			return user.getLanguage();
		} else {
			user.setLanguage(language);
			return dao.save(user).getLanguage();
		}
	}
	
	public String updateLanguage(String username, String language) throws UsernameNotFoundException {
		Optional<User> possibleUser = dao.findByUsername(username);
		if (possibleUser.isEmpty()) {
			String message = String.format("No user with username '%s' found.", username);
			logger.error(message);
			throw new UsernameNotFoundException(message);
		} else {
			return updateLanguage(possibleUser.get(), language);
		}
	}
	
	public void updateLanguage(LibrarianDetails details, String requestLanguage) throws UsernameNotFoundException {
		String responseLanguage = updateLanguage(details.getUsername(), requestLanguage);
		details.setLanguage(responseLanguage);
	}

}
