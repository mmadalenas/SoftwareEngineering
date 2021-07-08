package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Withdrawn extends State {
	public void process(Operation wrapper, Sibs sibs) throws AccountException {
		try {
			sibs.services.deposit(wrapper.getTargetIban(), wrapper.getValue());
			wrapper.setState(new Deposited());
		}
		catch (AccountException e) {
			wrapper.setState(new Retry(new Withdrawn()));
			e.printStackTrace();	
		}
	}
	
	public void processRetry(Operation wrapper, Sibs sibs) throws OperationException, AccountException{
		sibs.services.deposit(wrapper.getTargetIban(), wrapper.getValue());
		wrapper.setState(new Deposited());
	}

	public void cancel(Operation wrapper, Sibs sibs) throws AccountException {
			sibs.services.deposit(wrapper.getSourceIban(), wrapper.getValue());
			wrapper.setState(new Canceled());
	}
}
