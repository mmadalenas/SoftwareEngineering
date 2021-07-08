package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Deposited extends State {
	public void process(Operation wrapper, Sibs sibs){
		try {
			sibs.services.withdraw(wrapper.getSourceIban(), wrapper.getCommission());
			wrapper.setState(new Completed());
		}
		catch(AccountException e){
			wrapper.setState(new Retry(new Deposited()));
			e.printStackTrace();
		}
	}
	
	public void processRetry(Operation wrapper, Sibs sibs) throws OperationException, AccountException{
		sibs.services.withdraw(wrapper.getSourceIban(), wrapper.getCommission());
		wrapper.setState(new Completed());
	}

	public void cancel(Operation wrapper, Sibs sibs) throws AccountException {
		sibs.services.deposit(wrapper.getSourceIban(), wrapper.getValue());
		sibs.services.withdraw(wrapper.getTargetIban(), wrapper.getValue());
		wrapper.setState(new Canceled());
	}
}

