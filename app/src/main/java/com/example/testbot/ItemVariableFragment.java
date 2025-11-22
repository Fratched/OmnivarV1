package com.example.testbot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemVariableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemVariableFragment extends Fragment implements AddVariableDialog.AddVariableListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemVariableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemVariableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemVariableFragment newInstance(String param1, String param2) {
        ItemVariableFragment fragment = new ItemVariableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View page = inflater.inflate(R.layout.fragment_item_variable, container, false);


        // TODO: Make this a field and reuse it in the listener below
        RecyclerView recyclerView = page.findViewById(R.id.variableList);

        DataAccessObject db = new DataAccessObject(getContext());
        ArrayList<VariableHeader> variables = db.getAllVariables();

        ItemVariableAdapter adapter = new ItemVariableAdapter(getContext(), variables);

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
        DataAccessObject db = new DataAccessObject(getContext());
        //System.out.println("Trying to add a variable to db: " + name + " " + units);
        VariableType type = VariableType.valueOf(units);
        db.newVariable(new VariableHeader(name, VariableType.valueOf(units)));

        System.out.println("Success?");

        // Recargar RecyclerView
        RecyclerView recyclerView = getView().findViewById(R.id.variableList);
        ArrayList<VariableHeader> variables = db.getAllVariables();
        ((ItemVariableAdapter) recyclerView.getAdapter()).variables = variables;
        recyclerView.getAdapter().notifyDataSetChanged();
    }


}