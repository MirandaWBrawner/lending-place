package lendingplace.library.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lendingplace.library.model.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {
	Page<Category> findByEnglishContaining(String english, Pageable pageable);
	Page<Category> findByHindiContaining(String hindi, Pageable pageable);
	Page<Category> findBySwahiliContaining(String swahili, Pageable pageable);
	Page<Category> findByArabicContaining(String arabic, Pageable pageable);
	Page<Category> findByMandarinContaining(String mandarin, Pageable pageable);
	Page<Category> findBySpanishContaining(String spanish, Pageable pageable);
	Page<Category> findByFrenchContaining(String french, Pageable pageable);
	
	Page<Category> findByEnglish(String english, Pageable pageable);
	Page<Category> findByHindi(String hindi, Pageable pageable);
	Page<Category> findBySwahili(String swahili, Pageable pageable);
	Page<Category> findByArabic(String arabic, Pageable pageable);
	Page<Category> findByMandarin(String mandarin, Pageable pageable);
	Page<Category> findBySpanish(String spanish, Pageable pageable);
	Page<Category> findByFrench(String french, Pageable pageable);
	
	List<Category> findByEnglish(String english);
	List<Category> findByHindi(String hindi);
	List<Category> findBySwahili(String swahili);
	List<Category> findByArabic(String arabic);
	List<Category> findByMandarin(String mandarin);
	List<Category> findBySpanish(String spanish);
	List<Category> findByFrench(String french);
}
