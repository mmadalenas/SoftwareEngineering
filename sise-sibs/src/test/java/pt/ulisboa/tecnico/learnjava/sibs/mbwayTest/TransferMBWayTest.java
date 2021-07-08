package pt.ulisboa.tecnico.learnjava.sibs.mbwayTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;


import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.MbWay;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.MbWayAccount;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.TransferMBWayController;


public class TransferMBWayTest {

	private static final String AMOUNT = "50";
	private static final int SOURCE_AMOUNT = 1000;
	private static final int TARGET_AMOUNT = 5000;
	Bank bank1;
	Bank bank2;
	Services services;
	Client client1;
	Client client2;
	HashMap<String, MbWayAccount> mbWayAccount;
	MbWay mbWay;
	TransferMBWayController controller;
	String sourceIban;
	String targetIban;


	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		services = new Services();
		bank1 = new Bank("CGD");
		bank2 = new Bank("BPI");
		client1 = new Client(bank1, "Maria", "Soares", "123456789", "912346987", "Rua Alves Redol", 25);
		client2 = new Client(bank2, "Clara", "Rodrigues", "987654321", "917895234", "Rua Redol", 30);
		sourceIban=bank1.createAccount(AccountType.CHECKING, client1, SOURCE_AMOUNT, 0);
		targetIban=bank2.createAccount(AccountType.CHECKING, client2, TARGET_AMOUNT, 0);
		mbWayAccount = new HashMap<String, MbWayAccount>();
		mbWayAccount.put("912346987", new MbWayAccount("CGDCK1",123));
		mbWayAccount.put("917895234", new MbWayAccount("BPICK2",123));
		mbWay = new MbWay(services,mbWayAccount);
		controller = new TransferMBWayController(mbWay,"912346987","917895234",AMOUNT);
	}
	
	@Test
	public void transferTests() throws AccountException {
		
		//Test to check if numbers exist in MBWay
		assertTrue(controller.checkNumber());
	
		//Test to check if source account has enough money for the operation
		assertTrue(controller.hasMoney());
	
		//Test to check if the amount was successfully withdrawn and deposited
		controller.doTransfer();
		assertEquals(SOURCE_AMOUNT - Integer.parseInt(AMOUNT),this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(TARGET_AMOUNT + Integer.parseInt(AMOUNT),this.services.getAccountByIban(targetIban).getBalance());

	}
	
	@After
	public void tearDown() {
		Bank.clearBanks();
	}
}



