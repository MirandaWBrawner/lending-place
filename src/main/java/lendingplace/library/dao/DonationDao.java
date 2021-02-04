package lendingplace.library.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lendingplace.library.model.Donation;

@Repository
public interface DonationDao extends JpaRepository<Donation, Integer>  {

	
}
