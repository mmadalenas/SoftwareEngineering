package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.concurrent.ThreadLocalRandom;

import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class AssociateMBWayController {

	private String nPhone;
	private String iban;
	private MbWay mbWay;
	private int code;

	public AssociateMBWayController(MbWay mbWay, String[] nPhoneiban) {
		this.nPhone=nPhoneiban[0];
		this.iban=nPhoneiban[1];
		this.mbWay=mbWay;
	}

	public int associateMBwayAccount() throws SibsException {
		if(mbWay.getServices().getAccountByIban(iban)!=null && !mbWay.mbWayAccount.containsKey(nPhone) && mbWay.banksWithMBWay.contains(iban.substring(0,3))) {
			mbWay.mbWayAccount.put(nPhone, new MbWayAccount(iban,generateCode()));
			return this.code;
		}

		else {
			throw new SibsException();
		}
	}

	public int generateCode() {
		this.code=ThreadLocalRandom.current().nextInt(100000, 999999);
		return this.code;
	}

}
