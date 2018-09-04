package be.milants.triviatrainer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import be.milants.triviatrainer.R;
import be.milants.triviatrainer.data.TriviaData;
import be.milants.triviatrainer.service.impl.DefaultTriviaDatabaseService;

public class ListEntriesActivity extends AppCompatActivity {

    private DefaultTriviaDatabaseService triviaDatabaseService = new DefaultTriviaDatabaseService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_entries_activity);

        List<TriviaData> triviaDataList = triviaDatabaseService.getEntriesFromDataBase(this);

        final TableLayout tagsLayout = findViewById(R.id.triviaEntriesLayout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (TriviaData triviaData : triviaDataList) {
            final TableRow rowView = (TableRow) inflater.inflate(R.layout.trivia_entry, null);

            ((TextView) rowView.findViewById(R.id.subjectValue)).setText(triviaData.getSubject());
            ((TextView) rowView.findViewById(R.id.mainCategoryValue)).setText(triviaData.getMainCategory());
            ((TextView) rowView.findViewById(R.id.secondaryCategoryValue)).setText(triviaData.getSecondaryCategory());

            for (int i = 0; i < triviaData.getTags().size(); i++) {
                String tag = triviaData.getTags().get(i);
                TableRow tagTableRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.trivia_entry_tag_entry, null);
                ((TextView) tagTableRow.findViewById(R.id.tagValue)).setText(tag);
                RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                relativeLayoutParams.topMargin = 50 * i;
                relativeLayoutParams.addRule(RelativeLayout.BELOW, tagTableRow.getId());
                ((RelativeLayout) rowView.findViewById(R.id.triviaEntryTagList)).addView(tagTableRow, relativeLayoutParams);
            }

            tagsLayout.addView(rowView, tagsLayout.getChildCount());
            tagsLayout.invalidate();
        }
    }
}
