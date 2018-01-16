package esia.timewatcher.data_init;

public class TypeSet {
	private final int runningHobbyNbr;
	private final int stoppedHobbyNbr;
	private final int eventNbr;

	public TypeSet(int runningHobbyNbr, int stoppedHobbyNbr, int eventNbr) {
		if (runningHobbyNbr < 0 || stoppedHobbyNbr < 0 || eventNbr < 0) {
			throw new TestException();
		}

		this.runningHobbyNbr = runningHobbyNbr;
		this.stoppedHobbyNbr = stoppedHobbyNbr;
		this.eventNbr = eventNbr;
	}

	public int getRunningHobbyNbr() {
		return runningHobbyNbr;
	}

	public int getStoppedHobbyNbr() {
		return stoppedHobbyNbr;
	}

	public int getEventNbr() {
		return eventNbr;
	}
}
