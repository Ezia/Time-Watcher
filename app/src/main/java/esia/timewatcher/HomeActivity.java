package esia.timewatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.OccupationTypeData;
import esia.timewatcher.structures.OccupationType;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);

        DatabaseManager.initializeInstance(this.getApplicationContext());

        Bitmap myImage1 =
                BitmapFactory.decodeResource(getResources(), R.drawable.twitter);
        OccupationType type1 = new OccupationType("type1", myImage1);
        OccupationTypeData data1 = DatabaseManager.getInstance().createOccupationType(type1);

        Log.i("Home activity", "data1 : " + data1.getOccupationType().getName());

        OccupationType type2 = new OccupationType("type2", myImage1);

        DatabaseManager.getInstance().updateOccupationType(data1, type2);
        DatabaseManager.getInstance().requestOccupationType(data1);
        Log.i("Home activity", "data1 : " + data1.getOccupationType().getName());

        DatabaseManager.getInstance().deleteOccupationType(data1);
    }
}
