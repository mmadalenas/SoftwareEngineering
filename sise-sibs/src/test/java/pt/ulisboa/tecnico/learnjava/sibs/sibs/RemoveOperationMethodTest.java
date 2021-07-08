package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class RemoveOperationMethodTest {
	int pos;
	private static final String TARGET_IBAN = "TargetIban";
	private static final String SOURCE_IBAN = "SourceIban";
	private static final int VALUE = 100;
	

	private Sibs sibs;

	@Before
	public void setUp() throws OperationException, SibsException {
		this.sibs = new Sibs(3, new Services());
		int pos=this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
	}

	@Test
	public void success() throws SibsException {
	}

	@Test(expected = SibsException.class)
	public void negativePosition() throws SibsException {
		throw new SibsException();
	}

	@Test(expected = SibsException.class)
	public void positionAboveLength() throws SibsException {
		throw new SibsException();
	}

	@Test
	public void removeOperationTest() throws SibsException {
		this.sibs.removeOperation(pos);
		assertEquals(null, this.sibs.getOperation(pos));
	}
	
	@After
	public void tearDown() {
		this.sibs = null;
	}

}
