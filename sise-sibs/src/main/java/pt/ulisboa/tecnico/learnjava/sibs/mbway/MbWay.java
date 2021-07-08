package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class MbWay {
	
	private Services services;
	
	HashSet<String> banksWithMBWay=new HashSet<>(Arrays.asList("CGD", "BPI"));
	
	HashMap<String, MbWayAccount> mbWayAccount = new HashMap<String, MbWayAccount>();
	
	public MbWay(Services services) {
		this.services=services;
	}
	
	public MbWay(Services services, HashMap<String, MbWayAccount> mbWayAccount) {	//APAGAR DEPOIS DE TESTAR
		this.services=services;
		this.mbWayAccount=mbWayAccount;
	}
	
	public Services getServices() {
		return services;
	}
	
	public void clearAccounts() {
		mbWayAccount.clear();
	}
}
