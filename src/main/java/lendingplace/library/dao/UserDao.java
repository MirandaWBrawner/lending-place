package lendingplace.library.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lendingplace.library.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
	Boolean existsByUsername(String username);
}
