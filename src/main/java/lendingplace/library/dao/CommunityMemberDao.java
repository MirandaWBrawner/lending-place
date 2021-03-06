package lendingplace.library.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lendingplace.library.model.CommunityMember;

@Repository
public interface CommunityMemberDao extends JpaRepository<CommunityMember, Integer> {
	Page<CommunityMember> findByNameContaining(String name, Pageable pageable);
	Page<CommunityMember> findByEmailContaining(String email, Pageable pageable);
	Page<CommunityMember> findByPhoneContaining(String phone, Pageable pageable);
}
