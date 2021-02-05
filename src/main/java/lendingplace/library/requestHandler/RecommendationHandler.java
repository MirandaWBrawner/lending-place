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

import com.mysql.cj.util.StringUtils;

import lendingplace.library.dao.RecommendationDao;
import lendingplace.library.model.Recommendation;
import lendingplace.library.request.RecommendationRequest;
import lendingplace.library.service.MessageResponse;



@RestController
@CrossOrigin
public class RecommendationHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(RecommendationHandler.class);
	
	@Autowired
	private RecommendationDao dao;
	
	@PostMapping(path = "/recommend", produces = "application/json")
	public ResponseEntity<?> add(@RequestBody RecommendationRequest request) {
		if (request == null) return ResponseEntity.badRequest().build();
		if (StringUtils.isNullOrEmpty(request.getSubject())) {
			logger.warn(String.format("Error with recommendation: subject is %s", request.getSubject()));
			return ResponseEntity.badRequest().body(new MessageResponse("Bad Request"));
		}
		if (StringUtils.isNullOrEmpty(request.getFullText())) {
			logger.warn(String.format("Error with recommendation: fullText is %s", request.getFullText()));
			return ResponseEntity.badRequest().body(new MessageResponse("Bad Request"));
		}
		Recommendation record = new Recommendation(null, request.getSubject(), 
				request.getFullText(), new Timestamp(System.currentTimeMillis()));
		try {
			dao.save(record);
			return ResponseEntity.status(200).body(new MessageResponse("Thank you for your recommendation"));
		} catch (Exception exception) {
			logger.error(String.format("Error with recommendation: ", exception.toString()));
			return ResponseEntity.status(404).body(new MessageResponse("Could not complete the request"));
		}
	}

}
