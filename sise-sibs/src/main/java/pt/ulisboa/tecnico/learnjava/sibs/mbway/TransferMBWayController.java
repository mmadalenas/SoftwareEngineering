package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
/** MVC controller for MbWay Transfer functionality */
public class TransferMBWayController { 
	/** */
	private String sourcePhone; /** Emitter phone number */
	private String targetPhone; /** Receiver phone number */
	private MbWay mbWay; /** MbWay system instance */
	private String amount; /** Amount to be transfered */
	
	/** Constructor for the MbWay transfer class */
	public TransferMBWayController(MbWay newMBWay, String sourcePhone, String targetPhone, String amount) {
		this.sourcePhone=sourcePhone;
		this.targetPhone=targetPhone;
		this.mbWay=newMBWay;
		this.amount=amount;
	}

	/** Method that checks if both numbers exist in the MbWay data structure */
	public boolean checkNumber() {
		return mbWay.mbWayAccount.containsKey(sourcePhone) && mbWay.mbWayAccount.containsKey(targetPhone);
	}
	
	/** Method that checks if the source number account has enough balance */
	public boolean hasMoney() {
		int balance=mbWay.getServices().getAccountByIban(mbWay.mbWayAccount.get(sourcePhone).getIban()).getBalance();
		int amountInt=Integer.parseInt(this.amount);
		if (balance>=amountInt) {
			return true;
		}
		else
			return false;
	}	
	
	/** Method that executes the transfer operation */
	public void doTransfer() throws AccountException {
		mbWay.getServices().withdraw(mbWay.mbWayAccount.get(sourcePhone).getIban(), Integer.parseInt(this.amount));
		mbWay.getServices().deposit(mbWay.mbWayAccount.get(targetPhone).getIban(), Integer.parseInt(this.amount));
	}
}
