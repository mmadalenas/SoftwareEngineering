package pt.ulisboa.tecnico.learnjava.sibs.mbway;

public class MbWayView {
	
	public void printCode(int code) {
		System.out.println("Code: " + code + " (don't share it with anyone).");
	}
	
	public void confirmationSuccess() {
			System.out.println("MBWAY association confirmed successfully!");
	}
	
	public void confirmationWrongCode() {
		System.out.println("Wrong confirmation code. Try association again.");
	}

	public void tranferSuccess() {
		System.out.println("Transfer performed successfully!");
	}

	public void tranferNoMoney() {
		System.out.println("Not enough money on the source account.");
	}

	public void tranferWrongNumber() {
		System.out.println("Wrong phone number.");
	}

	public void insuranceNotFamily() {
		System.out.println("Only family-member commands accepted right now.");		
	}

	public void insuranceWrongNumber(String pNumber) {
		System.out.println("Family-member "+pNumber+" is not registered.");
	}

	public void insuranceAmountWrong() {
		System.out.println("Something is wrong. Is the insurance amount right?");
	}

	public void insuranceNotEnoughMoney() {
		System.out.println("Oh no! One family member doesn’t have money to pay!");
	}

	public void insuranceTooMany() {
		System.out.println("Oh no! Too many family members.");
	}

	public void insuranceMMissing(int missing) {
		System.out.println("Oh no! " + missing + " family member is missing.");
	}

	public void insuranceSuccess() {
		System.out.println("Insurance paid successfully!");
	}

	public void terminate() {
		System.out.println("MBWAY terminated with success.");
	}
}
