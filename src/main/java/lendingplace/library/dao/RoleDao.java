package lendingplace.library.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lendingplace.library.model.UserRole;

@Repository
public interface RoleDao extends JpaRepository<UserRole, Integer> {
	List<UserRole> findByEnglish(String english);
	List<UserRole> findByHindi(String hindi);
	List<UserRole> findBySwahili(String swahili);
	List<UserRole> findByArabic(String arabic);
	List<UserRole> findByMandarin(String mandarin);
	List<UserRole> findBySpanish(String spanish);
	List<UserRole> findByFrench(String french);
}
