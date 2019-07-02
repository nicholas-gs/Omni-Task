package com.example.ntu_timetable_calendar.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ntu_timetable_calendar.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;

public class PlanFragment extends Fragment {

    private MultiAutoCompleteTextView multiAutoCompleteTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupAutoCompleteTextView();
    }

    private void initViews(View view){
        multiAutoCompleteTextView = view.findViewById(R.id.plan_fragment_autocompletetextview);
    }

    private void setupAutoCompleteTextView(){
        String[] courseIndexes = new String[]{
              "CE2001", "CE2002", "CE2006", "HE2005"
        };

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, courseIndexes);

        multiAutoCompleteTextView.setAdapter(listAdapter);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiAutoCompleteTextView.setThreshold(1);

        multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sel = adapterView.getItemAtPosition(i).toString();
                createRecipientChip(sel);
            }
        });

    }

    private void createRecipientChip(String sel) {
        ChipDrawable chip = ChipDrawable.createFromResource(getContext(), R.xml.standalone_chip);
        int cursorPosition = multiAutoCompleteTextView.getSelectionStart();
        int spanLength = sel.length() + 2;
        Editable text = multiAutoCompleteTextView.getText();
        chip.setText(sel);
        chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());

        ImageSpan span = new ImageSpan(chip);
        text.setSpan(span, cursorPosition - spanLength, cursorPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

}
