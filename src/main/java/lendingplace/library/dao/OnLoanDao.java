package lendingplace.library.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lendingplace.library.model.CommunityMember;
import lendingplace.library.model.ItemOnLoan;
import lendingplace.library.model.Lendable;

@Repository
public interface OnLoanDao extends JpaRepository<ItemOnLoan, Long> {
	Page<ItemOnLoan> findByLendable(Lendable lendable, Pageable pageable);
	Page<ItemOnLoan> findByMember(CommunityMember member, Pageable pageable);
	List<ItemOnLoan> findByMember(CommunityMember member);
}
