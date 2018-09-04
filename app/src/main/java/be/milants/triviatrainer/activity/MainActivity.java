package be.milants.triviatrainer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import be.milants.triviatrainer.R;
import be.milants.triviatrainer.service.impl.DefaultTriviaDatabaseService;

public class MainActivity extends AppCompatActivity {

    DefaultTriviaDatabaseService databaseService = new DefaultTriviaDatabaseService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseService.addMainCategoriesToDatabase(this);
        setContentView(R.layout.activity_main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton mainFab = findViewById(R.id.fab);
        FloatingActionButton addEntryFab = findViewById(R.id.addEntryFab);
        FloatingActionButton listEntriesFab = findViewById(R.id.listEntriesFab);
        FloatingActionButton entriesSlideShowFab = findViewById(R.id.slideShowFab);

        final LinearLayout addEntryLayout = findViewById(R.id.addEntryLayout);
        addEntryLayout.setVisibility(View.GONE);
        final LinearLayout listEntriesLayout = findViewById(R.id.listEntriesLayout);
        listEntriesLayout.setVisibility(View.GONE);
        final LinearLayout entriesSlideShowLayout = findViewById(R.id.slideShowLayout);
        entriesSlideShowLayout.setVisibility(View.GONE);

        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (addEntryLayout.getVisibility() == View.VISIBLE && listEntriesLayout.getVisibility() == View.VISIBLE
                       && entriesSlideShowLayout.getVisibility() == View.VISIBLE) {
                   addEntryLayout.setVisibility(View.GONE);
                   listEntriesLayout.setVisibility(View.GONE);
                   entriesSlideShowLayout.setVisibility(View.GONE);
               } else {
                   addEntryLayout.setVisibility(View.VISIBLE);
                   listEntriesLayout.setVisibility(View.VISIBLE);
                   entriesSlideShowLayout.setVisibility(View.VISIBLE);
               }
            }
        });

        addEntryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEntryIntent = new Intent(MainActivity.this, AddEntryActivity.class);
                MainActivity.this.startActivity(addEntryIntent);
            }
        });

        listEntriesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Intent listEntriesIntent = new Intent (MainActivity.this, ListEntriesActivity.class);
                MainActivity.this.startActivity(listEntriesIntent);
            }
        });

        entriesSlideShowFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entriesSlideShowIntent = new Intent(MainActivity.this, EntriesSlideShowActivity.class);
                MainActivity.this.startActivity(entriesSlideShowIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
