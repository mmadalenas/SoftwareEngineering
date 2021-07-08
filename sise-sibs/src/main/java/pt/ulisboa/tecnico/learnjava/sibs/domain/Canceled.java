package pt.ulisboa.tecnico.learnjava.sibs.domain;

public class Canceled extends State {
	public void process(Operation wrapper, Sibs sibs) {
		wrapper.setState(new Completed());
	}
}
