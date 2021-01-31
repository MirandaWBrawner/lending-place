package lendingplace.library.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lendingplace.library.model.Category;
import lendingplace.library.model.Lendable;

@Repository
public interface LendableDao extends JpaRepository<Lendable, Integer> {
	Page<Lendable> findByCreatorContaining(String creator, Pageable pageable);
	Page<Lendable> findByEnglishContaining(String english, Pageable pageable);
	Page<Lendable> findByHindiContaining(String hindi, Pageable pageable);
	Page<Lendable> findBySwahiliContaining(String swahili, Pageable pageable);
	Page<Lendable> findByArabicContaining(String arabic, Pageable pageable);
	Page<Lendable> findByMandarinContaining(String mandarin, Pageable pageable);
	Page<Lendable> findBySpanishContaining(String spanish, Pageable pageable);
	Page<Lendable> findByFrenchContaining(String french, Pageable pageable);
	Page<Lendable> findByCategoriesContaining(Category category, Pageable pageable);
}
