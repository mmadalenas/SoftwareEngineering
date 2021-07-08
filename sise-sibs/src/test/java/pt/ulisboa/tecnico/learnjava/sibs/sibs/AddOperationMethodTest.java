package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class AddOperationMethodTest {
	private static final String TARGET_IBAN = "TargetIban";
	private static final String SOURCE_IBAN = "SourceIban";
	private static final int VALUE = 100;

	private Sibs sibs;

	@Before
	public void setUp() throws OperationException, SibsException {
		this.sibs = new Sibs(3, new Services());
	}
	
	@Test
	public void addOperationTest() throws SibsException, OperationException {	
		int pos=this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
		assertEquals(SOURCE_IBAN,this.sibs.getOperation(pos).getSourceIban());
		assertEquals(TARGET_IBAN,this.sibs.getOperation(pos).getTargetIban());
		assertEquals(VALUE,this.sibs.getOperation(pos).getValue());
	}
	
	@Test(expected = SibsException.class)
	public void negativePosition() throws SibsException {
		this.sibs.getOperation(-1);
	}

	@Test(expected = SibsException.class)
	public void positionAboveLength() throws SibsException {
		this.sibs.getOperation(4);
	}
		
	@After
	public void tearDown() {
		this.sibs = null;
	}
}
