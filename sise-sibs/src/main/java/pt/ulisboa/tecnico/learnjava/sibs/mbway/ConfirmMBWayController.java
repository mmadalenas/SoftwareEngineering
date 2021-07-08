package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import pt.ulisboa.tecnico.learnjava.sibs.mbway.MbWayAccount.mbWayState;

public class ConfirmMBWayController {

	private String nPhone;
	private int code;
	private MbWay mbWay;

	public ConfirmMBWayController(MbWay mbWay, String nPhone, String code) {
		this.nPhone=nPhone;
		this.code=Integer.valueOf(code);
		this.mbWay=mbWay;
	}

	public boolean confirmMBwayAccount(){
		if(mbWay.mbWayAccount.containsKey(nPhone) && mbWay.mbWayAccount.get(nPhone).getState()==mbWayState.INITIALIZED) {
			if(mbWay.mbWayAccount.get(nPhone).getCode()==(this.code)) {
				mbWay.mbWayAccount.get(nPhone).stateToActive();
				return true;
			}
		}
		return false;
	}
}
