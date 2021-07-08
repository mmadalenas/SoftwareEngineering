package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MVCPattern {

	public static void main(String[] args) throws BankException, AccountException, ClientException, SibsException{
		MbWayView view = new MbWayView();

		Scanner input = new Scanner(System.in);
		boolean isOnRepeat=true;
		MbWay newMBWay=retrieveMBway();

		while(isOnRepeat==true) {

			String mnString = input.nextLine();

			String[] mnstringArray=mnString.split(" ");
			String message=mnstringArray[0];
			String n1="";
			String n2="";
			String n3="";
			if(mnstringArray.length>1) {
				n1=mnstringArray[1];
				n2=mnstringArray[2];
				if(mnstringArray.length>3 && mnstringArray.length<5)
					n3=mnstringArray[3];
			}

			if(message.equals("associate-mbway")) {
				String[] n2n1= {n2,n1};
				AssociateMBWayController controller = new AssociateMBWayController(newMBWay,n2n1);
				int code = controller.associateMBwayAccount();
				view.printCode(code);
			}
			else if(message.equals("confirm-mbway")) {
				ConfirmMBWayController controller = new ConfirmMBWayController(newMBWay,n1,n2);
				boolean confirm=controller.confirmMBwayAccount();
				if(confirm) view.confirmationSuccess();
				else view.confirmationWrongCode();
			}
			else if(message.equals("mbway-transfer")) {
				TransferMBWayController controller = new TransferMBWayController(newMBWay,n1,n2,n3);
				boolean exists=controller.checkNumber();
				if(exists) {
					boolean doesHave=controller.hasMoney();
					if(doesHave) {
						controller.doTransfer();
						view.tranferSuccess();
					}					
					else view.tranferNoMoney();
				}else view.tranferWrongNumber();
			}
			else if(message.equals("mbway-split-insurance")) {
				HashMap<String, String> insuranceSplit = new HashMap<String, String>();
				InsuranceMBWayController controller = new InsuranceMBWayController(newMBWay,n1,n2);
				String firstNumber=introduceFamilyMembers(input,controller,view,insuranceSplit);
				processFamilyMembers(controller,view,insuranceSplit,firstNumber);
			}
			else if(message.equals("exit")) {
				view.terminate();
				isOnRepeat=false;
			}
		}
		input.close();
	}

	private static MbWay retrieveMBway() throws BankException, AccountException, ClientException {
		Bank bank1 = new Bank("CGD");
		Bank bank2 = new Bank("BPI");
		Services services = new Services();
		Client client1 = new Client(bank1, "Maria", "Soares", "123456789", "912346987", "Rua Alves Redol", 25);
		Client client2 = new Client(bank2, "Clara", "Rodrigues", "987654321", "917895234", "Rua Redol", 30);
		Client client3 = new Client(bank2, "Marco", "Andrade", "345123678", "967896734", "Rua da Figueira", 40);

		bank1.createAccount(AccountType.CHECKING, client1, 1000, 0);
		bank2.createAccount(AccountType.CHECKING, client2, 5000, 0);
		bank2.createAccount(AccountType.CHECKING, client3, 2500, 0);

		HashMap<String, MbWayAccount> mbWayAccount = new HashMap<String, MbWayAccount>();
		mbWayAccount.put("912346987", new MbWayAccount("CGDCK1",123));
		mbWayAccount.put("917895234", new MbWayAccount("BPICK2",123));
		mbWayAccount.put("967896734", new MbWayAccount("BPICK3",123));
		MbWay mbWay=new MbWay(services,mbWayAccount);

//		MbWay mbWay=new MbWay(services);

		return mbWay;
	}
	
	private static void processFamilyMembers(InsuranceMBWayController controller, MbWayView view, HashMap<String, String> insuranceSplit, String firstNumber) throws NumberFormatException, AccountException {

		if(insuranceSplit.size()==0) {
			int nMemberMin = controller.memberMin(insuranceSplit,firstNumber);
			view.insuranceMMissing(nMemberMin);
		}

		else {
			boolean totalAmountCheck = controller.checkTotalAmount(insuranceSplit,firstNumber);
			if(totalAmountCheck) {
				boolean balances = controller.checkBalances(insuranceSplit,firstNumber);
				if(balances) {
					boolean nMemberMax = controller.memberMax(insuranceSplit,firstNumber);
					if(nMemberMax) {
						int nMemberMin = controller.memberMin(insuranceSplit,firstNumber);
						if(nMemberMin==0) {
							controller.doTransfers(insuranceSplit,firstNumber);
							view.insuranceSuccess();
						}
						else view.insuranceMMissing(nMemberMin);
					}
					else view.insuranceTooMany();
				}
				else view.insuranceNotEnoughMoney();
			}
			else view.insuranceAmountWrong();
		}
	}
	
	private static String introduceFamilyMembers(Scanner input, InsuranceMBWayController controller, MbWayView view, HashMap<String, String> insuranceSplit) {
		String messageIns="";
		String firstNumber="";
		int count=0;
		while(!messageIns.equals("end")) {
			String mnStringFam = input.nextLine();
			String[] mnStringFamily=mnStringFam.split(" ");
			messageIns=mnStringFamily[0];
			String pNumber="";
			String amount="";
			if(mnStringFamily.length>1) {
				pNumber=mnStringFamily[1];
				amount=mnStringFamily[2];
			}

			if (messageIns.equals("family-member")) {
				boolean exists=controller.checkNumber(pNumber);
				if(exists) {
					insuranceSplit.put(pNumber, amount);
				}
				else view.insuranceWrongNumber(pNumber);
			}
			else if(messageIns.equals("end")) {
			}
			else view.insuranceNotFamily();
			if(insuranceSplit.size()==1 && pNumber!="" &&count==0)firstNumber=pNumber;count++;
		}
		return firstNumber;
	}

}
