package lendingplace.library.requestHandler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lendingplace.library.authentication.JwtUtilities;
import lendingplace.library.authentication.ResponseWithJwt;
import lendingplace.library.dao.UserDao;
import lendingplace.library.model.User;
import lendingplace.library.model.UserRole;
import lendingplace.library.request.LoginRequest;
import lendingplace.library.request.SignupRequest;
import lendingplace.library.service.AccountService;
import lendingplace.library.service.LibrarianDetails;
import lendingplace.library.service.LibrarianDetailsService;
import lendingplace.library.service.UserRoleService;

@RestController
@CrossOrigin
@RequestMapping("/accounts")
public class UserRequestHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRequestHandler.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtilities jwtUtilities;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleService roleService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private LibrarianDetailsService detailsService;
	
	@PostMapping(path = "/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
		if (request == null) {
			logger.info("Signup failed because request was null.");
			return ResponseEntity.badRequest().build();
		}
		String username = request.getUsername();
		if (userDao.existsByUsername(username)) {
			logger.info("Signup failed because username is already in use.");
			return ResponseEntity.badRequest()
					.body("The username is not available.");
		}
		String encryptedPassword = passwordEncoder.encode(request.getPassword());
		Set<UserRole> roles = roleService.searchByName("User");
		if (request.getRoleNames() != null) {
			for (String roleString: request.getRoleNames()) {
				roles.addAll(roleService.searchByName(roleString));
			}
		}
		int userId = accountService.getMaxId() + 1;
		User user = new User(userId, username, encryptedPassword, 
				request.getEmail(), request.getLanguage(), roles);
		try {
			userDao.save(user);
		} catch (Throwable problem) {
			logger.error(problem.toString());
			return ResponseEntity.status(
					HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		logger.info("Signup successful");
		return ResponseEntity.ok("You are now signed up.");
	}

	@PostMapping(path = "/login", produces = "application/json")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
		if (request == null) {
			logger.info("Login failed because request was null.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(
				request.getUsername(), request.getPassword());
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(upaToken);
		} catch (BadCredentialsException exception) {
			logger.info("Login failed because login information was incorrect.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtUtilities.generateToken(authentication);
		LibrarianDetails details = (LibrarianDetails) authentication.getPrincipal();
		List<String> roles = details.getAuthorities().parallelStream().map(
				role -> role.getAuthority()).collect(Collectors.toList());
		try {
			detailsService.updateLanguage(details, request.getLanguage());
		} catch (UsernameNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		ResponseWithJwt response = new ResponseWithJwt(token, details.getUsername(), 
				details.getEmail(), details.getLanguage(), roles);
		logger.info("Login successful");
		return ResponseEntity.ok(response);
	}
}
