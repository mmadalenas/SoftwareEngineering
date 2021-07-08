package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.sibs.domain.*;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Error;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";
	private static final int VALUE = 10000;
	private static final int COMMISSION = 200;
	private static final int SOURCE_AMOUNT = 40000;
	private static final int TARGET_AMOUNT = 60000;
	private static final int VALUE_SUP = 50000;

	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;
	private String sourceIban;
	private String targetIban;
	private int sourceAmount2;
	private int targetAmount2;

	@Before
	public void setUp() throws BankException, AccountException, ClientException, SibsException, OperationException {
		this.services = new Services();
		this.sibs = new Sibs(100, services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);

		sourceIban = this.sourceBank.createAccount(AccountType.CHECKING, this.sourceClient, SOURCE_AMOUNT, 100000);
		targetIban = this.targetBank.createAccount(AccountType.CHECKING, this.targetClient, TARGET_AMOUNT, 100000);

		this.sibs.transfer(sourceIban, targetIban, VALUE);
	}

	@Test
	public void success() throws BankException, AccountException, SibsException, OperationException, ClientException {
		Operation operation = this.sibs.getOperation(0);

		assertEquals(sourceIban, operation.getSourceIban());
		assertEquals(targetIban, operation.getTargetIban());
		assertEquals(VALUE, operation.getValue());
	}

	@Test
	public void comissionTest() throws BankException, AccountException, SibsException, OperationException, ClientException {
		Operation operation = this.sibs.getOperation(0);
		operation.process(sibs);
		operation.process(sibs);
		operation.process(sibs);

		assertEquals(SOURCE_AMOUNT-(VALUE+COMMISSION), this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(TARGET_AMOUNT+VALUE, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test(expected = OperationException.class)
	public void amountSuperior() throws OperationException, SibsException, AccountException {		
		this.sibs.transfer(sourceIban, targetIban, VALUE_SUP);
		Operation operation = this.sibs.getOperation(1);		
		operation.process(sibs);
	}

	@Test
	public void amountSupBalanceTest() throws BankException, AccountException, SibsException, OperationException, ClientException {
		this.sibs.transfer(sourceIban, targetIban, VALUE_SUP);

		Operation operation = this.sibs.getOperation(1);

		try {
			operation.process(sibs);
		}
		catch (OperationException e){
			assertEquals(SOURCE_AMOUNT, this.services.getAccountByIban(sourceIban).getBalance());
			assertEquals(TARGET_AMOUNT, this.services.getAccountByIban(targetIban).getBalance());
		}
	}

	@Test
	public void operationCreatedRegisteredTest() throws SibsException, AccountException, OperationException {
		Operation operation = this.sibs.getOperation(0);

		assertTrue(operation.getState() instanceof Registered);
	}

	@Test
	public void checkStatesTest() throws SibsException, AccountException, OperationException {
		Operation operation = this.sibs.getOperation(0);

		operation.process(sibs);

		assertTrue(operation.getState() instanceof Withdrawn);
		assertEquals(SOURCE_AMOUNT-VALUE, this.services.getAccountByIban(sourceIban).getBalance());

		operation.process(sibs);

		assertTrue(operation.getState() instanceof Deposited);
		assertEquals(TARGET_AMOUNT+VALUE, this.services.getAccountByIban(targetIban).getBalance());

		operation.process(sibs);

		assertTrue(operation.getState() instanceof Completed);
		assertEquals(SOURCE_AMOUNT-VALUE-COMMISSION, this.services.getAccountByIban(sourceIban).getBalance());
	}

	@Test
	public void transferTest() throws SibsException, OperationException, AccountException {
		Operation operation = this.sibs.getOperation(0);

		assertEquals(SOURCE_AMOUNT,this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(TARGET_AMOUNT,this.services.getAccountByIban(targetIban).getBalance());

		operation.process(sibs);
		operation.process(sibs);
		operation.process(sibs);

		assertEquals(SOURCE_AMOUNT-VALUE-COMMISSION,this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(TARGET_AMOUNT+VALUE,this.services.getAccountByIban(targetIban).getBalance());

	}

	@Test
	public void processOperationsTest() throws SibsException, AccountException, OperationException {
		this.sibs.transfer(sourceIban, targetIban, VALUE-50);
		this.sibs.cancelOperation(1);

		this.sibs.processOperations();

		assertTrue(this.sibs.getOperation(0).getState() instanceof Completed);
		assertTrue(this.sibs.getOperation(1).getState() instanceof Completed);
	}

	@Test
	public void cancelOperationTest() throws OperationException, AccountException, SibsException {
		Operation operation = this.sibs.getOperation(0);

		this.sibs.cancelOperation(0);

		assertTrue(operation.getState() instanceof Canceled);
		assertEquals(SOURCE_AMOUNT,this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(TARGET_AMOUNT,this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void retryTest() throws SibsException, OperationException, AccountException {
		Operation operation = this.sibs.getOperation(0);

		operation.process(sibs);

		operation.setToRetryForTests(operation.getState());
		assertTrue(operation.getState() instanceof Retry);
		operation.process(sibs);
		assertTrue(operation.getState() instanceof Deposited);
	}

	@Test
	public void errorTest() throws SibsException, OperationException, AccountException {
		Operation operation = this.sibs.getOperation(0);

		operation.setToRetryWZeroForTests(operation.getState());
		
		operation.process(sibs);
		assertTrue(operation.getState() instanceof Error);
	}
	
	@Test
	public void retryTestDeposited() throws SibsException, OperationException, AccountException {
		Operation operation = this.sibs.getOperation(0);

		operation.process(sibs);
		operation.process(sibs);

		operation.setToRetryForTests(operation.getState());
		assertTrue(operation.getState() instanceof Retry);
		operation.process(sibs);
		assertTrue(operation.getState() instanceof Completed);
	}
	
	@Test
	public void retryTestRegistered() throws SibsException, OperationException, AccountException {
		Operation operation = this.sibs.getOperation(0);

		operation.setToRetryForTests(operation.getState());
		assertTrue(operation.getState() instanceof Retry);
		operation.process(sibs);
		assertTrue(operation.getState() instanceof Withdrawn);
	}
	
	@Test
	public void cancelOperationTestDeposited() throws OperationException, AccountException, SibsException {
		Operation operation = this.sibs.getOperation(0);
		operation.process(sibs);
		operation.process(sibs);

		this.sibs.cancelOperation(0);

		assertTrue(operation.getState() instanceof Canceled);
		assertEquals(SOURCE_AMOUNT,this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(TARGET_AMOUNT,this.services.getAccountByIban(targetIban).getBalance());
	}
	
	@Test
	public void cancelOperationTestWithdrawn() throws OperationException, AccountException, SibsException {
		Operation operation = this.sibs.getOperation(0);
		operation.process(sibs);

		this.sibs.cancelOperation(0);

		assertTrue(operation.getState() instanceof Canceled);
		assertEquals(SOURCE_AMOUNT,this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(TARGET_AMOUNT,this.services.getAccountByIban(targetIban).getBalance());
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
