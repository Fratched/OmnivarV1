package com.example.testbot.connection;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.testbot.DataAccessObject;
import com.example.testbot.R;
import com.example.testbot.model.VariableHeader;
import com.example.testbot.model.VariableType;

import java.util.ArrayList;

public class AddConnectionDialog extends DialogFragment {

    public interface AddConnectionListener {
        void onConnectionAdded(String from, String to);
    }

    private AddConnectionListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (AddConnectionListener) getParentFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_add_connection, null);

        Spinner spinnerVar1 = view.findViewById(R.id.spinnerVariable1);
        Spinner spinnerVar2 = view.findViewById(R.id.spinnerVariable2);

        ArrayList<String> variableNames = new ArrayList<>();
        for (VariableHeader v : DataAccessObject.getInstance(getContext()).variables) {
            variableNames.add(v.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                variableNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVar1.setAdapter(adapter);

        spinnerVar2.setAdapter(adapter);

        Button submit = view.findViewById(R.id.add_connection_button);
        submit.setOnClickListener(e ->
        {
            listener.onConnectionAdded((String)spinnerVar1.getSelectedItem(), (String)spinnerVar2.getSelectedItem());
            getDialog().dismiss();
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();

        return dialog;
    }
}
