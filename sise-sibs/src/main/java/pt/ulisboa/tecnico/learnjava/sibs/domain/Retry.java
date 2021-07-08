package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Retry extends State{
	private State prevState;
	private int count;

	public Retry(State prevState) {
		this.count=3;
		this.prevState=prevState;

	}
	
	public Retry(State prevState, int n) { //For Tests only
		this.count=n;
		this.prevState=prevState;
	}

	public void process(Operation wrapper, Sibs sibs) throws AccountException{
		if(count>0) {
			try {
				this.prevState.processRetry(wrapper, sibs);
			}
			catch(OperationException e) {
				this.count--;
			}
		}
		else
			wrapper.setState(new Error());
	}



}
