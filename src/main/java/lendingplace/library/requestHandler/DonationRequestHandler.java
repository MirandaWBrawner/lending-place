package lendingplace.library.requestHandler;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lendingplace.library.dao.DonationDao;
import lendingplace.library.model.Donation;
import lendingplace.library.request.DonationRequest;
import lendingplace.library.service.MessageResponse;

@CrossOrigin
@RestController
public class DonationRequestHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(DonationRequestHandler.class);
	
	@Autowired
	private DonationDao dao;

	@PostMapping(path = "/donate", produces = "application/json")
	public ResponseEntity<?> donate(@RequestBody DonationRequest request) {
		Donation donation = new  Donation(request.getDonorName(), request.getAmount(), 
				request.getCurrency(), new Timestamp(System.currentTimeMillis()));
		MessageResponse response = new MessageResponse();
		try {
			dao.save(donation);
			response.setMessage("Thank you for your donation!");
			return ResponseEntity.status(200).body(response);
		} catch (Exception exception) {
			response.setMessage("The donation did not go through.");
			return ResponseEntity.status(404).body(response);
		}
	}
}
