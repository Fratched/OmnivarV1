package com.example.testbot.variable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.testbot.DataAccessObject;
import com.example.testbot.R;
import com.example.testbot.model.VariableHeader;
import com.example.testbot.model.VariableType;

import java.util.ArrayList;

public class ItemVariableFragment extends Fragment implements AddVariableDialog.AddVariableListener, ItemVariableAdapter.VariableClickListener {



    public ItemVariableFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View page = inflater.inflate(R.layout.fragment_item_variable, container, false);


        // TODO: Make this a field and reuse it in the listener below
        RecyclerView recyclerView = page.findViewById(R.id.variableList);

        DataAccessObject instance = DataAccessObject.getInstance(getContext());

        ItemVariableAdapter adapter = new ItemVariableAdapter(getContext(), instance.variables, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        Button addBtn = page.findViewById(R.id.add_variable_button);
        addBtn.setOnClickListener(v -> {
            AddVariableDialog dialog = new AddVariableDialog();
            dialog.show(getChildFragmentManager(), "AddVariableDialog");
        });


        return page;
    }
    @Override
    public void onVariableAdded(String name, String units) {
        DataAccessObject instance = DataAccessObject.getInstance(getContext());
        VariableType type = VariableType.valueOf(units);
        instance.newVariable(new VariableHeader(name, type));
        RecyclerView recyclerView = getView().findViewById(R.id.variableList);
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void OnVariableClick(VariableHeader header) {
        NoteDialog dialog = new NoteDialog(header);
        dialog.show(getChildFragmentManager(), "AddNote");
    }
}