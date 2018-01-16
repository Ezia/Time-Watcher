package esia.timewatcher;

import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import esia.timewatcher.data_init.DatabaseContent;
import esia.timewatcher.data_init.TypeSet;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;

@RunWith(Parameterized.class)
public class TestDatabaseListRequest {

	DatabaseContent databaseContent;

	@Parameterized.Parameters
	public static Object[] data() {
		return new Object[] {
				new DatabaseContent(),

				new DatabaseContent(2,
						new TypeSet(2, 2, 4),
						new TypeSet(5, 6, 7)),

				new DatabaseContent(2,
						new TypeSet(2, 0, 4),
						new TypeSet(5, 0, 7)),

				new DatabaseContent(2,
						new TypeSet(0, 2, 4),
						new TypeSet(0, 6, 7)),

				new DatabaseContent(2,
						new TypeSet(2, 2, 0),
						new TypeSet(5, 6, 0))
		};
	}

	public TestDatabaseListRequest(DatabaseContent databaseContent) {
		this.databaseContent = databaseContent;
	}

	@Before
	public void setUp() throws Exception {
		InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseManager.DATABASE_NAME);
		DatabaseManager.initializeInstance(InstrumentationRegistry.getTargetContext());
		databaseContent.fillDatabase();
	}

	@Test
	public void requestStoppedHobbiesOfType() throws Exception {
		if (databaseContent.getTypeNbr() > 0) {
			long typeId = databaseContent.getTypeData(0).getId();

			ArrayList<HobbyData> resultList = DatabaseManager.getInstance()
					.requestStoppedHobbies(typeId, true);

			ArrayList<HobbyData> expectedList = databaseContent
					.getStoppedHobbies(typeId, true);

			Assert.assertEquals(resultList, expectedList);
		}
	}

	@Test
	public void requestStoppedHobbiesOfTypeReverse() throws Exception {
		if (databaseContent.getTypeNbr() > 0) {
			long typeId = databaseContent.getTypeData(0).getId();

			ArrayList<HobbyData> resultList = DatabaseManager.getInstance()
					.requestStoppedHobbies(typeId, false);

			ArrayList<HobbyData> expectedList = databaseContent
					.getStoppedHobbies(typeId, false);

			Assert.assertEquals(resultList, expectedList);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void requestStoppedHobbiesOfTypeWithNonexistentType() throws Exception {
		DatabaseManager.getInstance()
				.requestStoppedHobbies(-1, true);
	}
}
