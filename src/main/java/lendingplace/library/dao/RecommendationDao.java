package lendingplace.library.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lendingplace.library.model.Recommendation;

@Repository
public interface RecommendationDao extends JpaRepository<Recommendation, Integer> {
	
}
