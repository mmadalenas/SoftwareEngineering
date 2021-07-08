package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.MbWay;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.MbWayView;

public class Sibs {
	final Operation[] operations;
	Services services;
	MbWay mbWay;
	MbWayView mbWayView;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}

	public void transfer(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException {

		addOperation(sourceIban, targetIban, amount);
	}

	public int addOperation(String sourceIban, String targetIban, int value)
			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			throw new SibsException();
		}

		Operation operation = new Operation(sourceIban, targetIban, value);

		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}

	public int GetNumberOfOperations() throws SibsException{
		int count=0;
		for (Operation i:operations) {
			if (i!=null)
				count++;
		}
		return count;
	}

	public int GetValueOfOperations() {
		int count=0;
		for (Operation i:operations) {
			if (i!=null)
				count=count+i.getValue();
		}
		return count;
	}

	public void processOperations() throws OperationException, AccountException, SibsException {
		for (Operation i:operations) {
			if(i==null) {break;}
			State curState=i.getState();
			while (!(curState instanceof Completed)) {
				i.process(this);
				curState=i.getState();
			}
		}
	}

	public void cancelOperation(int pos) throws OperationException, AccountException {
		operations[pos].cancel(this);
	}
	
	public void retry(int pos) {
		
	}
}
