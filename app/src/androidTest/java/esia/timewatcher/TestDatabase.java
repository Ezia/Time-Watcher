package esia.timewatcher;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		TestTypeDatabase.class,
		TestEventDatabase.class,
		TestHobbyDatabase.class,
		TestDatabaseUtils.class
})
public class TestDatabase {
}
