package esia.timewatcher.data_init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import esia.timewatcher.DatabaseTestHelper;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.EventData;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.database.TypeData;
import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.Type;

public class DatabaseContent {

	private int typeNbr = 0;
	private TypeSet[] typeSets = new TypeSet[]{};
	private int runningHobbyNbr = 0;
	private int stoppedHobbyNbr = 0;
	private int eventNbr = 0;

	private LinkedList<Type> typeList = new LinkedList<>();
	private LinkedList<TypeData> typeDataList = new LinkedList<>();
	private LinkedList<Hobby> runningHobbyList = new LinkedList<>();
	private LinkedList<Hobby> stoppedHobbyList = new LinkedList<>();
	private LinkedList<Event> eventList = new LinkedList<>();
	private LinkedList<HobbyData> runningHobbyDataList = new LinkedList<>();
	private LinkedList<HobbyData> stoppedHobbyDataList = new LinkedList<>();
	private LinkedList<EventData> eventDataList = new LinkedList<>();

	private void reset() {
		typeList = new LinkedList<>();
		typeDataList = new LinkedList<>();
		runningHobbyList = new LinkedList<>();
		stoppedHobbyList = new LinkedList<>();
		eventList = new LinkedList<>();
		runningHobbyDataList = new LinkedList<>();
		stoppedHobbyDataList = new LinkedList<>();
		eventDataList = new LinkedList<>();
	}

	public TypeData getTypeData(int position) {
		return typeDataList.get(position);
	}

	public int getTypeNbr() {
		return typeNbr;
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

	public DatabaseContent() {
		// empty DB
	}

	public DatabaseContent(int typeNbr, TypeSet... typeSets) {
		if (typeNbr < 0 || typeSets.length > typeNbr) {
			throw new TestException();
		}

		this.typeNbr = typeNbr;
		this.typeSets = typeSets;

		for (TypeSet ts : typeSets) {
			if (ts == null) {
				throw new TestException();
			}
			runningHobbyNbr += ts.getRunningHobbyNbr();
			stoppedHobbyNbr += ts.getStoppedHobbyNbr();
			eventNbr += ts.getEventNbr();
		}
	}


	public void fillDatabase() {
		reset();

		// create types

		typeList = DatabaseTestHelper.getListOfTypes(typeNbr);

		for (Type t: typeList) {
			typeDataList.add(DatabaseManager.getInstance().createType(t));
		}

		// create each typeSets

		runningHobbyList = DatabaseTestHelper
				.getListOfRunningHobbies(runningHobbyNbr, true);
		stoppedHobbyList = DatabaseTestHelper
				.getListOfStoppedHobbiesByStartDate(stoppedHobbyNbr, true);
		eventList = DatabaseTestHelper
				.getListOfEvents(eventNbr, true);

		Iterator<Hobby> runningHobbyIt = runningHobbyList.iterator();
		Iterator<Hobby> stoppedHobbyIt = stoppedHobbyList.iterator();
		Iterator<Event> eventListIt = eventList.iterator();

		for (int i = 0; i < typeSets.length; ++i) {
			TypeSet ts = typeSets[i];
			TypeData td = typeDataList.get(i);
			for (int rhNbr = 0; rhNbr < ts.getRunningHobbyNbr(); ++rhNbr) {
				runningHobbyDataList.add(
						DatabaseManager.getInstance()
						.createHobby(runningHobbyIt.next(), td.getId())
				);
			}
			for (int shNbr = 0; shNbr < ts.getStoppedHobbyNbr(); ++shNbr) {
				stoppedHobbyDataList.add(
						DatabaseManager.getInstance()
						.createHobby(stoppedHobbyIt.next(), td.getId())
				);
			}
			for (int eNbr = 0; eNbr < ts.getEventNbr(); ++eNbr) {
				eventDataList.add(DatabaseManager.getInstance()
						.createEvent(eventListIt.next(), td.getId())
				);
			}
		}
	}

	public ArrayList<HobbyData> getStoppedHobbies(long typeId, boolean orderByDescendantStartDate) {
		ArrayList<HobbyData> resultList = new ArrayList<>();

		for (HobbyData hd : stoppedHobbyDataList) {
			if (hd.getTypeData().getId() == typeId) {
				resultList.add(hd);
			}
		}
		orderByStartDate(resultList, orderByDescendantStartDate);

		return resultList;
	}

	public ArrayList<HobbyData> getStoppedHobbies(boolean orderByDescendantStartDate) {
		ArrayList<HobbyData> resultList = new ArrayList<>(stoppedHobbyDataList);
		orderByStartDate(resultList, orderByDescendantStartDate);

		return resultList;
	}

	private static void orderByStartDate(ArrayList<HobbyData> list, boolean descendingOrder) {
		Collections.sort(list, (lhs, rhs) -> {
			if (lhs.getHobby().getStartDate().isBefore(rhs.getHobby().getStartDate())) {
				if (descendingOrder) {
					return 1;
				} else {
					return -1;
				}
			} else if (rhs.getHobby().getStartDate().isBefore(lhs.getHobby().getStartDate())) {
				if (descendingOrder) {
					return -1;
				} else {
					return 1;
				}
			} else {
				return 0;
			}
		});
	}
}
