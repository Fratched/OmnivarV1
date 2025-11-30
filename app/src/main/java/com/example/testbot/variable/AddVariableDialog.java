package com.example.testbot.variable;

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

import com.example.testbot.R;
import com.example.testbot.model.VariableType;

public class AddVariableDialog extends DialogFragment {

    public interface AddVariableListener {
        void onVariableAdded(String name, String units);
    }

    private AddVariableListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (AddVariableListener) getParentFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_add_variable, null);

        EditText editName = view.findViewById(R.id.editVariableName);
        Spinner spinnerUnits = view.findViewById(R.id.spinnerVariableUnits);

        ArrayAdapter<VariableType> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                VariableType.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(adapter);

        builder.setView(view);
        AlertDialog dialog = builder.create();


        Button btn = view.findViewById(R.id.addButton);
        btn.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            VariableType units = (VariableType) spinnerUnits.getSelectedItem();

            if (!name.isEmpty()) {
                listener.onVariableAdded(name, units.name());
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
