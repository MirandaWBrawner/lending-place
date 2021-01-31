package lendingplace.library.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lendingplace.library.dao.UserDao;
import lendingplace.library.model.User;

@Service
public class LibrarianDetailsService implements UserDetailsService {
	
	@Autowired
	UserDao dao;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User librarian = dao.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
		return LibrarianDetails.build(librarian);
	}

}
