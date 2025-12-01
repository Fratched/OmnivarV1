package com.example.testbot.variable;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.testbot.DataAccessObject;
import com.example.testbot.R;
import com.example.testbot.model.NoteHeader;
import com.example.testbot.model.VariableHeader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
public class NoteDialog extends DialogFragment {

    private VariableHeader variable;
    private CalendarView calendarView;
    private EditText valueEditText;
    private TextView selectedDateText;
    private long currentDateEpochDay;

    public NoteDialog(VariableHeader variable) {
        this.variable = variable;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.popup_calendar, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        calendarView      = view.findViewById(R.id.calendarView);
        valueEditText     = view.findViewById(R.id.editTextDayValue);
        selectedDateText  = view.findViewById(R.id.textSelectedDate);
        Button saveButton = view.findViewById(R.id.buttonSaveDayValue);

        long todayMillis = Calendar.getInstance().getTimeInMillis();
        calendarView.setDate(todayMillis);

        currentDateEpochDay = convertMillisToEpochDay(todayMillis);
        updateSelectedDateLabel(currentDateEpochDay);
        loadValueForCurrentDate();

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth, 0, 0, 0);
            currentDateEpochDay = convertMillisToEpochDay(c.getTimeInMillis());
            updateSelectedDateLabel(currentDateEpochDay);
            loadValueForCurrentDate();
        });

        saveButton.setOnClickListener(v -> {
            String entered = valueEditText.getText().toString().trim();

            if (entered.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a value", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Double.parseDouble(entered);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Value must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            DataAccessObject db = DataAccessObject.getInstance(requireContext());
            NoteHeader existing = findNoteForDate(db.getNotesForVariableLocal(variable.getName()));

            if (existing == null) {
                db.newNote(new NoteHeader(
                        variable.getName(),
                        entered,
                        currentDateEpochDay
                ));
            } else {
                existing.setVal(entered);
                existing.setDate(currentDateEpochDay);
                db.updateNote(existing);
            }

            Toast.makeText(
                    requireContext(),
                    "Saved \"" + entered + "\"",
                    Toast.LENGTH_SHORT
            ).show();

            dialog.dismiss();
        });

        return dialog;
    }

    private void updateSelectedDateLabel(long epochDay) {
        selectedDateText.setText("Selected date: " + epochToString(epochDay));
    }

    private void loadValueForCurrentDate() {
        DataAccessObject db = DataAccessObject.getInstance(requireContext());
        ArrayList<NoteHeader> notes = db.getNotesForVariableLocal(variable.getName());

        NoteHeader n = findNoteForDate(notes);

        if (n != null) {
            valueEditText.setText(n.getVal());
        } else {
            valueEditText.setText("");
        }
    }

    private NoteHeader findNoteForDate(ArrayList<NoteHeader> notes) {
        for (NoteHeader n : notes) {
            if (n.getDate() == currentDateEpochDay) {
                return n;
            }
        }
        return null;
    }

    // Convert millis → epoch-day
    private long convertMillisToEpochDay(long millis) {
        return millis / (24L * 60 * 60 * 1000);
    }

    private String epochToString(long epochDay) {
        long millis = epochDay * 24L * 60 * 60 * 1000;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return String.format(Locale.US, "%04d-%02d-%02d",
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH)
        );
    }
}
