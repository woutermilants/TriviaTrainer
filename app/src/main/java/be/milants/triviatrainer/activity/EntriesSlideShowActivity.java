package be.milants.triviatrainer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import be.milants.triviatrainer.R;
import be.milants.triviatrainer.data.TriviaData;
import be.milants.triviatrainer.fragment.SlideShowEntry;
import be.milants.triviatrainer.service.impl.DefaultTriviaDatabaseService;

public class EntriesSlideShowActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    private DefaultTriviaDatabaseService triviaDatabaseService;
    ViewPager vpPager = null;
    List<TriviaData> triviaDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        triviaDatabaseService = new DefaultTriviaDatabaseService();
        setContentView(R.layout.activity_entries_slide_show_activity);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        triviaDataList = getTriviaEntries();
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), triviaDataList);
        vpPager.setAdapter(adapterViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slide_show_entry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_entry:
                deleteEntry();
                return true;
            case R.id.action_edit_entry:
                editEntry();
                return true;
        }
        return true;
    }

    private void deleteEntry() {
        TriviaData triviaData = triviaDataList.get(vpPager.getCurrentItem());
        triviaDatabaseService.deleteEntryFromDataBase(this, triviaData.getSubject());
        adapterViewPager.notifyDataSetChanged();
    }

    private void editEntry() {
        TriviaData triviaData = triviaDataList.get(vpPager.getCurrentItem());
        Intent addEntryIntent = new Intent(EntriesSlideShowActivity.this, AddEntryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("subject", triviaData);
        addEntryIntent.putExtras(bundle);
        EntriesSlideShowActivity.this.startActivity(addEntryIntent);
        adapterViewPager.notifyDataSetChanged();
    }

    public List<TriviaData> getTriviaEntries() {
        return triviaDatabaseService.getEntriesFromDataBase(this);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private List<TriviaData> triviaDataList;

        public MyPagerAdapter(FragmentManager fragmentManager, List<TriviaData> triviaDataList) {
            super(fragmentManager);
            this.triviaDataList = triviaDataList;
        }

        // Returns total number of pages.
        @Override
        public int getCount() {
            return triviaDataList.size();
        }

        // Returns the fragment to display for a particular page.
        @Override
        public Fragment getItem(int position) {
            TriviaData triviaData = triviaDataList.get(position);
            return SlideShowEntry.newInstance(triviaData);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Entry " + position;
        }
    }
}
