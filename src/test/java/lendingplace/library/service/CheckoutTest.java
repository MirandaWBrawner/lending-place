package lendingplace.library.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import lendingplace.library.LendingPlaceApplication;
import lendingplace.library.dao.CommunityMemberDao;
import lendingplace.library.dao.LendableDao;
import lendingplace.library.dao.OnLoanDao;
import lendingplace.library.dao.PendingLoanDao;
import lendingplace.library.model.CommunityMember;
import lendingplace.library.model.Lendable;
import lendingplace.library.model.PendingLoan;

class CheckoutTest {
	
	private static ApplicationContext context;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		context = SpringApplication.run(LendingPlaceApplication.class);
	}
	
	@Test
	void failOnNullLoanRequest() {
		LendableService service = context.getBean(LendableService.class);
		CommunityMember member = new CommunityMember("TestWithNullLoanRequest", "email@email.com", "555-555-5555");
		Assertions.assertFalse(service.checkoutOneCopyFromPending(null));
		Assertions.assertFalse(service.checkoutOneCopyFromPending(null, member));
	}

	@Test
	void failOnNullLendable() {
		LendableService service = context.getBean(LendableService.class);
		CommunityMember member = new CommunityMember("TestWithNullLendable", "email@email.com", "555-555-5555");
		Timestamp requestDate = new Timestamp(System.currentTimeMillis());
		PendingLoan loanWithMember = new PendingLoan(null, 1, "TestWithNullLendable1", requestDate, member);
		PendingLoan loanWithoutMember = new PendingLoan(null, 1, "TestWithNullLendable2", requestDate);
		Assertions.assertFalse(service.checkoutOneCopyFromPending(loanWithMember));
		Assertions.assertFalse(service.checkoutOneCopyFromPending(loanWithoutMember, member));
	}
	
	@Test
	void failOnNullMember() {
		LendableService service = context.getBean(LendableService.class);
		Optional<Lendable> possibleItem = service.getLendableDao().findById(3);
		Assertions.assertTrue(possibleItem.isPresent());
		Lendable lendable = possibleItem.get();
		Timestamp requestDate = new Timestamp(System.currentTimeMillis());
		PendingLoan loanWithMember = new PendingLoan(lendable, 1, "TestWithNullMember1", requestDate, null);
		PendingLoan loanWithoutMember = new PendingLoan(lendable, 1, "TestWithNullMember2", requestDate);
		Assertions.assertFalse(service.checkoutOneCopyFromPending(loanWithMember));
		Assertions.assertFalse(service.checkoutOneCopyFromPending(loanWithoutMember, null));
	}
	
	@Test
	void failOnNoItemsRequested() {
		LendableService service = context.getBean(LendableService.class);
		Optional<Lendable> possibleItem = service.getLendableDao().findById(3);
		Assertions.assertTrue(possibleItem.isPresent());
		Lendable lendable = possibleItem.get();
		Timestamp requestDate = new Timestamp(System.currentTimeMillis());
		CommunityMember member = new CommunityMember("TestWithNoItemsRequested", "email@email.com", "555-555-5555");
		PendingLoan loanWithMember = new PendingLoan(lendable, 0, "TestWithZeroItemsRequested1", requestDate, member);
		PendingLoan loanWithoutMember = new PendingLoan(lendable, 0, "TestWithZeroItemsRequested2", requestDate);
		Assertions.assertFalse(service.checkoutOneCopyFromPending(loanWithMember));
		Assertions.assertFalse(service.checkoutOneCopyFromPending(loanWithoutMember, member));
	}

	@Test
	void failOnLendableNotFound() {
		LendableService service = context.getBean(LendableService.class);
		Lendable lendable = new Lendable();
		lendable.setId(-1);
		lendable.setNumberAvailable(3);
		Timestamp requestDate = new Timestamp(System.currentTimeMillis());
		CommunityMember member = new CommunityMember("TestWithLendableNotFound", "email@email.com", "555-555-5555");
		PendingLoan loanWithMember = new PendingLoan(lendable, 1, "TestWithLendableNotFound1", requestDate, member);
		PendingLoan loanWithoutMember = new PendingLoan(lendable, 1, "TestWithLendableNotFound2", requestDate);
		Assertions.assertFalse(service.checkoutOneCopyFromPending(loanWithMember));
		Assertions.assertFalse(service.checkoutOneCopyFromPending(loanWithoutMember, member));
	}
	
	@Test
	void failOnNoCopiesAvailable() {
		LendableService service = context.getBean(LendableService.class);
		Optional<Lendable> possibleItem = service.getLendableDao().findById(5);
		Assertions.assertTrue(possibleItem.isPresent());
		Lendable lendable = possibleItem.get();
		Timestamp requestDate = new Timestamp(System.currentTimeMillis());
		CommunityMember member = new CommunityMember("TestWithNoCopiesAvailable", "email@email.com", "555-555-5555");
		PendingLoan loanWithMember = new PendingLoan(lendable, 1, "TestWithNoCopiesAvailable1", requestDate, member);
		PendingLoan loanWithoutMember = new PendingLoan(lendable, 1, "TestWithNoCopiesAvailable2", requestDate);
		Assertions.assertFalse(service.checkoutOneCopyFromPending(loanWithMember));
		Assertions.assertFalse(service.checkoutOneCopyFromPending(loanWithoutMember, member));
	}
	
	private String randomAlphabetLetters(int length) {
		StringBuilder builder = new StringBuilder();
		Random randomizer = new Random();
		for (int index = 0; index < length; index++) {
			char letter = (char) ('a' + randomizer.nextInt(26));
			builder.append(randomizer.nextBoolean() ? Character.toUpperCase(letter) : letter);
		}
		return builder.toString();
	}

	@Test
	void getPendingItemByName() {
		PendingLoanDao pendingDao = context.getBean(PendingLoanDao.class);
		LendableDao lendableDao = context.getBean(LendableDao.class);
		Optional<Lendable> possible = lendableDao.findById(7);
		Assertions.assertTrue(possible.isPresent());
		Lendable lendable = possible.get();
		Timestamp requestDate = new Timestamp(System.currentTimeMillis() + 100_000_000);
		String randomText = randomAlphabetLetters(26);
		PendingLoan loan = new PendingLoan(lendable, 1, randomText, requestDate);
		loan = pendingDao.save(loan);
		List<PendingLoan> results = pendingDao.findByNameContaining(randomText);
		Assertions.assertTrue(results.size() >= 1);
		Assertions.assertEquals(loan.getName(), results.get(0).getName());
	}
	
	@Test
	void successWithMember() {
		LendableService service = context.getBean(LendableService.class);
		CommunityMemberDao memberDao = context.getBean(CommunityMemberDao.class);
		LendableDao lendableDao = context.getBean(LendableDao.class);
		PendingLoanDao pendingDao = context.getBean(PendingLoanDao.class);
		OnLoanDao onLoanDao = context.getBean(OnLoanDao.class);
		Optional<CommunityMember> possibleMember = memberDao.findById(1);
		Assertions.assertTrue(possibleMember.isPresent());
		CommunityMember member = possibleMember.get();
		Optional<Lendable> possible = lendableDao.findById(7);
		Assertions.assertTrue(possible.isPresent());
		Lendable lendable = possible.get();
		Timestamp requestDate = new Timestamp(System.currentTimeMillis());
		PendingLoan loan = new PendingLoan(lendable, 1, "Test Checkout with Member", requestDate, member);
		loan = pendingDao.save(loan);
		long activeLoanCount = onLoanDao.count();
		Assertions.assertTrue(service.checkoutOneCopyFromPending(loan));
		Assertions.assertFalse(pendingDao.existsById(loan.getId()));
		Assertions.assertTrue(onLoanDao.count() > activeLoanCount);
	}
	
	@Test
	void successWithoutMember() {
		LendableService service = context.getBean(LendableService.class);
		CommunityMemberDao memberDao = context.getBean(CommunityMemberDao.class);
		LendableDao lendableDao = context.getBean(LendableDao.class);
		PendingLoanDao pendingDao = context.getBean(PendingLoanDao.class);
		OnLoanDao onLoanDao = context.getBean(OnLoanDao.class);
		Optional<CommunityMember> possibleMember = memberDao.findById(1);
		Assertions.assertTrue(possibleMember.isPresent());
		CommunityMember member = possibleMember.get();
		Optional<Lendable> possible = lendableDao.findById(6);
		Assertions.assertTrue(possible.isPresent());
		Lendable lendable = possible.get();
		Timestamp requestDate = new Timestamp(System.currentTimeMillis());
		String randomText = randomAlphabetLetters(35);
		PendingLoan loan = new PendingLoan(lendable, 2, randomText, requestDate);
		pendingDao.save(loan);
		List<PendingLoan> loanList = pendingDao.findByNameContaining(loan.getName());
		Assertions.assertTrue(loanList.size() == 1);
		loan = loanList.get(0);
		long activeLoanCount = onLoanDao.count();
		Assertions.assertTrue(service.checkoutOneCopyFromPending(loan, member));
		loanList = pendingDao.findByNameContaining(loan.getName());
		Assertions.assertTrue(loanList.size() == 1);
		loan = loanList.get(0);
		Assertions.assertEquals(1, loan.getCount());
		Assertions.assertTrue(onLoanDao.count() > activeLoanCount);
	}
}
