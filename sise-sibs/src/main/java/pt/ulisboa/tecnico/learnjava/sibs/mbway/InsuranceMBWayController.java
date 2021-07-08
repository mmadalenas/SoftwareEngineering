package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class InsuranceMBWayController {

	private MbWay mbWay;
	private String nMembers;
	private String amount;

	public InsuranceMBWayController(MbWay newMBWay, String nMembers, String amount) {
		this.mbWay=newMBWay;
		this.nMembers=nMembers;
		this.amount=amount;
	}

	public boolean checkNumber(String pNumber) {
		if(mbWay.mbWayAccount.containsKey(pNumber))
			return true;
		else
			return false;
	}

	public boolean checkTotalAmount(HashMap<String, String> insuranceSplit, String firstNumber) {
		int amountToShare=Integer.parseInt(this.amount)-Integer.parseInt(insuranceSplit.get(firstNumber));
		int sum=0;
		for (String i:insuranceSplit.keySet()) {
			if(i!=firstNumber) {
				sum+=Integer.parseInt(insuranceSplit.get(i));
			}
		}
		if(amountToShare==sum) {
			return true;
		}
		return false;
	}

	public boolean checkBalances(HashMap<String, String> insuranceSplit, String firstNumber) {
		for (String i:insuranceSplit.keySet()) {
			if(i!=firstNumber) {
				String pNumber=i;
				String amount=insuranceSplit.get(i);
				int balance=mbWay.getServices().getAccountByIban(mbWay.mbWayAccount.get(pNumber).getIban()).getBalance();
				if (balance<Integer.parseInt(amount)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean memberMax(HashMap<String, String> insuranceSplit, String firstNumber) {
		int nMembersMap = insuranceSplit.size();
		if(nMembersMap>Integer.parseInt(nMembers)) {
			return false;
		}
		return true;
	}

	public int memberMin(HashMap<String, String> insuranceSplit, String firstNumber) {
		int nMembersMap = insuranceSplit.size();
		if(nMembersMap<Integer.parseInt(nMembers)) {
			return Integer.parseInt(nMembers)-nMembersMap;
		}
		return 0;
	}

	public void doTransfers(HashMap<String, String> insuranceSplit, String firstNumber) throws NumberFormatException, AccountException {
		for (String i:insuranceSplit.keySet()) {
			if(i!=firstNumber) {
				String pNumber=i;
				String amount=insuranceSplit.get(i);
				mbWay.getServices().withdraw(mbWay.mbWayAccount.get(pNumber).getIban(), Integer.parseInt(amount));
				mbWay.getServices().deposit(mbWay.mbWayAccount.get(firstNumber).getIban(), Integer.parseInt(amount));
			}
		}
	}
}
