package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Registered extends State {
	public void process(Operation wrapper, Sibs sibs) throws OperationException, AccountException {
		if (sibs.services.getAccountByIban(wrapper.getSourceIban()).getBalance()<(wrapper.getValue()+wrapper.getCommission())) {
			wrapper.setState(new Retry(new Registered()));
			throw new OperationException();
		}
		else {
			sibs.services.withdraw(wrapper.getSourceIban(), wrapper.getValue());
			wrapper.setState(new Withdrawn());
		}
	}
	
	public void processRetry(Operation wrapper, Sibs sibs) throws OperationException, AccountException{
		sibs.services.withdraw(wrapper.getSourceIban(), wrapper.getValue());
		wrapper.setState(new Withdrawn());
	}
	
	public void cancel(Operation wrapper, Sibs sibs) {
		wrapper.setState(new Canceled());
	}
}
