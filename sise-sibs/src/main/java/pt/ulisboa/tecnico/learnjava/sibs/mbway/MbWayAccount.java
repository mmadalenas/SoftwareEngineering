package pt.ulisboa.tecnico.learnjava.sibs.mbway;

public class MbWayAccount {
	public enum mbWayState {
		INITIALIZED("IN"), ACTIVE("AC");

		private final String prefix;

		mbWayState(String prefix) {
			this.prefix = prefix;
		}

		public String getPrefix() {
			return this.prefix;
		}
	}
	private String iban;
	private int code;
	private mbWayState state;
	
	public MbWayAccount(String iban, int code) {
		this.iban=iban;
		this.code=code;
		state=mbWayState.INITIALIZED;
	}
	
	public String getIban() {
		return iban;
	}

	public int getCode() {
		return code;
	}

	public mbWayState getState() {
		return state;
	}

	public void stateToActive() {
		this.state=mbWayState.ACTIVE;
	}
	
	

}
