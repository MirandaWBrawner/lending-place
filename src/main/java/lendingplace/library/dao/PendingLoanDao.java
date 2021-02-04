package lendingplace.library.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lendingplace.library.model.Lendable;
import lendingplace.library.model.PendingLoan;

@Repository
public interface PendingLoanDao extends JpaRepository<PendingLoan, Long> {
	Page<PendingLoan> findByNameContaining(String name, Pageable pageable);
	List<PendingLoan> findByNameContaining(String name);
	Page<PendingLoan> findByLendable(Lendable lendable, Pageable pageable);
	Page<PendingLoan> findAll(Pageable pageable);
}
