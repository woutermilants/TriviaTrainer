package be.milants.triviatrainer.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import be.milants.triviatrainer.R;
import be.milants.triviatrainer.data.TriviaData;

public class SlideShowEntry extends Fragment {
    private String subject;
    private String mainCategory;
    private String secondaryCategory;
    private String[] tags;

    public static SlideShowEntry newInstance(TriviaData triviaData) {
        SlideShowEntry fragment = new SlideShowEntry();
        Bundle args = new Bundle();
        args.putString("subject", triviaData.getSubject());
        args.putString("mainCategory", triviaData.getMainCategory());
        args.putString("secondaryCategory", triviaData.getSecondaryCategory());
        args.putStringArray("tags", (String[]) triviaData.getTags().toArray());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subject = getArguments().getString("subject");
        mainCategory = getArguments().getString("mainCategory");
        secondaryCategory = getArguments().getString("secondaryCategory");
        tags = getArguments().getStringArray("tags");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_show_entry, container, false);
        TextView subjectLabel = (TextView) view.findViewById(R.id.slideShowEntrySubject);
        subjectLabel.setText(subject);
        TextView mainCategoryLabel = (TextView) view.findViewById(R.id.slideShowEntryMainCategory);
        mainCategoryLabel.setText(mainCategory);
        TextView secondaryCategoryLabel = (TextView) view.findViewById(R.id.slideShowEntrySecondaryCategory);
        secondaryCategoryLabel.setText(secondaryCategory);
        @SuppressLint("ResourceType")
        final TextView tagTextView= (TextView) getLayoutInflater().inflate(R.id.slideShowEntryTag, null);

        TextView tagLabel = (TextView) view.findViewById(R.id.slideShowEntryTag);
        for (String tag : tags) {
            tagTextView.setText(tag);
            tagTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tagTextView.setMaxLines(5);
            view.
        }

        return view;
    }
}