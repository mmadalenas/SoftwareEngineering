package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Operation {

	private final int commission;
	private final int value;
	private final String targetIban;
	private final String sourceIban;
	private State current;

	public Operation(String sourceIban, String targetIban, int value) throws OperationException {
		checkParameters(value);
		this.value = value;
		this.commission=(int)(value*0.02);
		current = new Registered();

		if (invalidString(targetIban)) {
			throw new OperationException();
		}
		if (invalidString(sourceIban)) {
			throw new OperationException();
		}

		this.targetIban = targetIban;
		this.sourceIban = sourceIban;
	}

	private void checkParameters(int value) throws OperationException {

		if (value <= 0) {
			throw new OperationException(value);
		}
	}

	public int getValue() {
		return this.value;
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	public int getCommission() {
		return this.commission;
	}

	public String getTargetIban() {
		return this.targetIban;
	}
	public String getSourceIban() {
		return this.sourceIban;
	}

	public void setState(State state) {
		current = state;
	}

	public void process(Sibs sibs) throws OperationException, AccountException {
		current.process(this,sibs);
	}

	public State getState() {
		return current;
	}
	
	public void cancel(Sibs sibs) throws OperationException, AccountException {
		current.cancel(this,sibs);
	}
	
	public void setToRetryForTests(State state) {
		this.setState(new Retry(state));
	}
	
	public void setToRetryWZeroForTests(State state) {
		this.setState(new Retry(state,0));
	}
}
