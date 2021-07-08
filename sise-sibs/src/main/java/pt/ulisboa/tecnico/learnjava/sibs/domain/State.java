package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

abstract class State {
	public void process(Operation wrapper, Sibs sibs) throws OperationException, AccountException{
	}

	public void cancel(Operation operation, Sibs sibs) throws AccountException {
	}

	public void processRetry(Operation wrapper, Sibs sibs) throws OperationException, AccountException{
		
	}
}