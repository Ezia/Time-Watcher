package esia.timewatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import java.util.Date;
import java.util.LinkedList;

import esia.timewatcher.adapters.RunningHobbyRecyclerViewAdapter;
import esia.timewatcher.adapters.TabPagerAdapter;
import esia.timewatcher.adapters.TypeSpinnerAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.database.OccupationTypeData;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.OccupationType;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);

        initializeDatabase();

		ViewPager pager = findViewById(R.id.pager);
		TabLayout tab = findViewById(R.id.tab);

		pager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
		tab.setupWithViewPager(pager);
    }

    private void initializeDatabase() {
        deleteDatabase(DatabaseManager.DATABASE_NAME);
        DatabaseManager.initializeInstance(this.getApplicationContext());

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.twitter_small);

        LinkedList<OccupationType> types = new LinkedList<>();
        types.add(new OccupationType("now", icon));
		types.add(new OccupationType("1sec", icon));
		types.add(new OccupationType("1min", icon));
		types.add(new OccupationType("1hour", icon));
		types.add(new OccupationType("1day", icon));
		types.add(new OccupationType("2days", icon));
		types.add(new OccupationType("1month", icon));
		types.add(new OccupationType("1year", icon));

        LinkedList<Hobby> hobbies = new LinkedList<>();
        hobbies.add(new Hobby(new DateTime()));
		hobbies.add(new Hobby(new DateTime().minus(Period.seconds(1))));
		hobbies.add(new Hobby(new DateTime().minus(Period.minutes(1))));
		hobbies.add(new Hobby(new DateTime().minus(Period.hours(1))));
		hobbies.add(new Hobby(new DateTime().minus(Period.days(1))));
		hobbies.add(new Hobby(new DateTime().minus(Period.days(2))));
		hobbies.add(new Hobby(new DateTime().minus(Period.months(1))));
		hobbies.add(new Hobby(new DateTime().minus(Period.years(1))));

		for (int i = 0; i < types.size(); ++i) {
			OccupationTypeData data = DatabaseManager.getInstance().createType(types.get(i));
			HobbyData d2 = DatabaseManager.getInstance().createHobby(hobbies.get(i), data.getId());
		}
    }

}
