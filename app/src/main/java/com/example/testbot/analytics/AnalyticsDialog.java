package com.example.testbot.analytics;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.testbot.DataAccessObject;
import com.example.testbot.R;
import com.example.testbot.model.ConnectionHeader;
import com.example.testbot.model.NoteHeader;
import com.example.testbot.model.VariableHeader;
import com.example.testbot.model.VariableType;

import java.util.ArrayList;

public class AnalyticsDialog extends DialogFragment {

    ConnectionHeader connection;

    public AnalyticsDialog(ConnectionHeader connection) {
        this.connection = connection;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_analitycs, null);

        TextView label = view.findViewById(R.id.analyticsTextView);
        ListView listView = view.findViewById(R.id.analyticsList);


        builder.setView(view);
        AlertDialog dialog = builder.create();



        DataAccessObject db = DataAccessObject.getInstance(requireContext());

        label.setText(String.format("%s  →  %s", connection.getFrom(), connection.getTo()));
        VariableType typeFrom = null;
        VariableType typeTo = null;

        ArrayList<NoteHeader> notesA = db.getNotesForVariableLocal(connection.getFrom());
        ArrayList<NoteHeader> notesB = db.getNotesForVariableLocal(connection.getTo());

        double[] arrA = db.toDoubleSeries(notesA, typeFrom);
        double[] arrB = db.toDoubleSeries(notesB, typeTo);

        boolean[] defA = db.allDefined(arrA.length);
        boolean[] defB = db.allDefined(arrB.length);

        ArrayList<String> insights =
                AnalyticsCore.getInstance().getAnalytics(
                        arrA, arrB,
                        defA, defB,
                        connection.getFrom(), connection.getTo()
                );


        ArrayList<String> lst = new ArrayList<>();
        if (insights.isEmpty()) {
            lst.add("No statistics found.");
        } else {
            lst.addAll(insights);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                lst
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listView.setAdapter(adapter);

        Button btn = view.findViewById(R.id.closeButton);
        btn.setOnClickListener(v -> dialog.dismiss());

        return dialog;
    }




}
