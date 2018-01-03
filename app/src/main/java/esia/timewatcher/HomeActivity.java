package esia.timewatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.LinkedList;

import esia.timewatcher.adapters.TabPagerAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.EventData;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.database.TypeData;
import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.Type;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        initializeDatabase();

		ViewPager pager = findViewById(R.id.pager);
		TabLayout tab = findViewById(R.id.tab);

		pager.setAdapter(new TabPagerAdapter(getFragmentManager()));
		pager.setCurrentItem(1);
		tab.setupWithViewPager(pager);
    }

    private void initializeDatabase() {
        deleteDatabase(DatabaseManager.DATABASE_NAME);
        DatabaseManager.initializeInstance(this.getApplicationContext());

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.twitter_small);

        LinkedList<Type> types = new LinkedList<>();
        types.add(new Type("now", icon));
		types.add(new Type("1sec", icon));
		types.add(new Type("1min", icon));
		types.add(new Type("1hour", icon));
		types.add(new Type("1day", icon));
		types.add(new Type("2days", icon));
		types.add(new Type("1month", icon));
		types.add(new Type("1year", icon));

        LinkedList<Hobby> hobbies = new LinkedList<>();
        hobbies.add(new Hobby(new DateTime()));
		hobbies.add(new Hobby(new DateTime().minus(Period.seconds(1))));
		hobbies.add(new Hobby(new DateTime().minus(Period.minutes(1))));
		hobbies.add(new Hobby(new DateTime().minus(Period.hours(1))));
		hobbies.add(new Hobby(new DateTime().minus(Period.days(1))));
		hobbies.add(new Hobby(new DateTime().minus(Period.days(2))));
		hobbies.add(new Hobby(new DateTime().minus(Period.months(1))));
		hobbies.add(new Hobby(new DateTime().minus(Period.years(1))));

		LinkedList<Event> events = new LinkedList<>();
		events.add(new Event(new DateTime()));
		events.add(new Event(new DateTime().minus(Period.seconds(1))));
		events.add(new Event(new DateTime().minus(Period.minutes(1))));
		events.add(new Event(new DateTime().minus(Period.hours(1))));
		events.add(new Event(new DateTime().minus(Period.days(1))));
		events.add(new Event(new DateTime().minus(Period.days(2))));
		events.add(new Event(new DateTime().minus(Period.months(1))));
		events.add(new Event(new DateTime().minus(Period.years(1))));

		for (int i = 0; i < types.size(); ++i) {
			TypeData data = DatabaseManager.getInstance().createType(types.get(i));
			HobbyData d2 = DatabaseManager.getInstance().createHobby(hobbies.get(i), data.getId());
			EventData d3 = DatabaseManager.getInstance().createEvent(events.get(i), data.getId());
		}
    }
}
