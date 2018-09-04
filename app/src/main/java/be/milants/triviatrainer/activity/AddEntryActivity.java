package be.milants.triviatrainer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.milants.triviatrainer.R;
import be.milants.triviatrainer.data.TriviaData;
import be.milants.triviatrainer.service.impl.DefaultTriviaDatabaseService;

public class AddEntryActivity extends AppCompatActivity {

    private DefaultTriviaDatabaseService triviaDatabaseService = new DefaultTriviaDatabaseService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry_activity);

        Spinner mainCategoriesSpinner = findViewById(R.id.mainCategoriesSpinner);
        final List<String> mainCategories = triviaDatabaseService.getMainCategories(this);
        final Context context = this;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mainCategories);
        mainCategoriesSpinner.setAdapter(adapter);
        mainCategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner secondaryCategoriesSpinner = findViewById(R.id.secondaryCategoriesSpinner);
                List<String> secondaryCategories = triviaDatabaseService.getSecondaryCategoriesForMainCategory(mainCategories.get((int) id), context);
                ArrayAdapter<String> secondaryCategoriesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, secondaryCategories);
                secondaryCategoriesSpinner.setAdapter(secondaryCategoriesAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton addNewTagEntryFab = (FloatingActionButton) findViewById(R.id.addNewTagEntry);
        final TableLayout tagsLayout = findViewById(R.id.tagsLayout);
        addNewTagEntryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final TableRow rowView = (TableRow) inflater.inflate(R.layout.tag_entry, null);
                rowView.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewGroup) rowView.getParent()).removeView(rowView);
                    }
                });
                tagsLayout.addView(rowView, tagsLayout.getChildCount());
                tagsLayout.invalidate();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get("subject") instanceof TriviaData) {
            TriviaData triviaData = (TriviaData) bundle.get("subject");
            EditText subjectEditText = findViewById(R.id.subject);
            subjectEditText.setText(triviaData.getSubject());

            mainCategoriesSpinner.setSelection(((ArrayAdapter) mainCategoriesSpinner.getAdapter()).getPosition(triviaData.getMainCategory()));
            Spinner secondaryCategorySpinner = findViewById(R.id.secondaryCategoriesSpinner);
            //secondaryCategorySpinner.setSelection(((ArrayAdapter) secondaryCategorySpinner.getAdapter()).getPosition(triviaData.getSecondaryCategory()));

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (String tag : triviaData.getTags()) {
                final TableRow rowView = (TableRow) inflater.inflate(R.layout.tag_entry, null);
                tagsLayout.addView(rowView, tagsLayout.getChildCount());
                ((TextView) rowView.getChildAt(0)).setText(tag);
                tagsLayout.invalidate();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_entry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_entry:
                saveEntry();
                return true;
        }
        return true;
    }

    private void saveEntry() {
        TriviaData triviaData = new TriviaData();
        EditText subjectEditText = findViewById(R.id.subject);
        triviaData.setSubject(subjectEditText.getText().toString());
        Spinner mainCategorySpinner = findViewById(R.id.mainCategoriesSpinner);
        triviaData.setMainCategory(mainCategorySpinner.getSelectedItem() != null ? mainCategorySpinner.getSelectedItem().toString() : null);
        Spinner secondaryCategorySpinner = findViewById(R.id.secondaryCategoriesSpinner);
        triviaData.setSecondaryCategory(secondaryCategorySpinner.getSelectedItem() != null ? secondaryCategorySpinner.getSelectedItem().toString() : null);
        TableLayout tagsLayout = findViewById(R.id.tagsLayout);
        List<String> tags = new ArrayList();
        for (int i = 0; i < tagsLayout.getChildCount(); i++) {
            View view = tagsLayout.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow tagRow = (TableRow) view;
                for (int j = 0; j < tagRow.getChildCount(); j++) {
                    if (tagRow.getChildAt(j) instanceof EditText) {
                        EditText tag = (EditText) tagRow.getChildAt(j);
                        tags.add(tag.getText().toString());
                    }
                }
            }
        }
        triviaData.setTags(tags);

        triviaDatabaseService.addEntryToDatabase(this, triviaData);
    }
}
