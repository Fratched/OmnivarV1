package com.example.testbot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemVariableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemVariableFragment extends Fragment {

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
        LinearLayout listLayout = page.findViewById(R.id.variable_list_container);
        DataAccessObject db = new DataAccessObject(getContext());
        ArrayList<VariableHeader> variables = db.getAllVariables();
        for (VariableHeader variableHeader : variables) {
            View entry = inflater.inflate(R.layout.variable_item_entry, listLayout, false);
            TextView nameView = entry.findViewById(R.id.variable_item_entry_name);
            nameView.setText(variableHeader.getName());
            listLayout.addView(entry);
        }




        return page;
    }
}