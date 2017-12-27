package esia.timewatcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import esia.timewatcher.database.DatabaseManager;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);

        DatabaseManager.initializeInstance(this.getApplicationContext());

    }

    private void updateTypeSpinnerAdapter() {
        DatabaseManager.getInstance().requestTypes(5);
    }
}
