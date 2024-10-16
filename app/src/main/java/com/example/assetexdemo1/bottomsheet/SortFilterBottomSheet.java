package com.example.assetexdemo1.bottomsheet;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.assetexdemo1.R;
import com.example.assetexdemo1.SortFilterOnDataPass;
import com.example.assetexdemo1.model.SortFilterOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SortFilterBottomSheet extends BottomSheetDialogFragment {

    SortFilterOptions sortFilterOptions = new SortFilterOptions();

    Spinner spinner;
    EditText sortFilterInputTitle;
    Switch prioritySwitch, limitResultSwitch;
    Button buttonBackSortFilter, buttonSaveSortFilter, sortFilterResetButton;

    private SortFilterOnDataPass dataPassListener;

    public void setDataPassListener(SortFilterOnDataPass listener) {
        dataPassListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sort_filter_bottom_sheet, container, false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.getActivity() != null) {
            getActivity().overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() != null) {
            spinner = getView().findViewById(R.id.sortFilterSpinner);
            sortFilterInputTitle = getView().findViewById(R.id.sortFilterInputTitle);
            prioritySwitch = getView().findViewById(R.id.prioritySwitch);
            limitResultSwitch = getView().findViewById(R.id.limitResultSwitch);
            buttonBackSortFilter = getView().findViewById(R.id.buttonBackSortFilter);
            buttonSaveSortFilter = getView().findViewById(R.id.buttonSaveSortFilter);
            sortFilterResetButton = getView().findViewById(R.id.sortFilterResetButton);

            ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(getView().getContext(), R.array.sort_filter_array, R.layout.spinner_dropdown_item);
            spinner.setAdapter(sortByAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // ((TextView) parent.getSelectedView()).setTypeface(null, Typeface.BOLD);
                    sortFilterOptions.setSortBy(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            buttonBackSortFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            buttonSaveSortFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = sortFilterInputTitle.getText().toString();
                    sortFilterOptions.setSearchTerm(s == null || s.isEmpty() ? null : sortFilterInputTitle.getText().toString());
                    sortFilterOptions.setPriorityFirst(prioritySwitch.isChecked());
                    sortFilterOptions.setLimitResults(limitResultSwitch.isChecked());

                    if (dataPassListener != null) {
                        dataPassListener.onDataPass(sortFilterOptions);
                    }
                    dismiss();
                }
            });

            sortFilterInputTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().trim().isEmpty()) {
                        buttonSaveSortFilter.setTextColor(Color.parseColor("#0886F6"));
                    }
                    else {
                        buttonSaveSortFilter.setTextColor(Color.parseColor("#6A6A6A"));
                    }
                }
            });

            sortFilterResetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortFilterOptions = new SortFilterOptions();

                    sortFilterInputTitle.setText("");
                    spinner.setSelection(0, true);
                    prioritySwitch.setChecked(false);
                    limitResultSwitch.setChecked(true);
                }
            });
        }
    }
}

