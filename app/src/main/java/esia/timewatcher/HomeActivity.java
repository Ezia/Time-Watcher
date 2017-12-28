package esia.timewatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import java.util.LinkedList;

import esia.timewatcher.adapters.TypeSpinnerAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.structures.OccupationType;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        initializeDatabase();

        TypeSpinnerAdapter typeSpinnerAdapter = new TypeSpinnerAdapter(this, 5);
        Spinner typeSpinner = findViewById(R.id.typeSpinner);
        typeSpinner.setAdapter(typeSpinnerAdapter);
    }

    private void initializeDatabase() {
        deleteDatabase(DatabaseManager.DATABASE_NAME);
        DatabaseManager.initializeInstance(this.getApplicationContext());
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.lan);

        for (int i = 0; i < 10; i++) {
            DatabaseManager.getInstance().createType(new OccupationType("test"+i, icon));
        }
    }
}
