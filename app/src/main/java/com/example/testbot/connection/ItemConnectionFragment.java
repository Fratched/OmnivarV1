package com.example.testbot.connection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.testbot.DataAccessObject;
import com.example.testbot.R;
import com.example.testbot.analytics.AnalyticsDialog;
import com.example.testbot.model.ConnectionHeader;
import com.example.testbot.model.VariableHeader;
import com.example.testbot.model.VariableType;

import java.util.ArrayList;

public class ItemConnectionFragment extends Fragment implements AddConnectionDialog.AddConnectionListener, ItemConnectionAdapter.OnConnectionClickedListener {

    RecyclerView recyclerView;

    public ItemConnectionFragment() {
        // Required empty public constructor
    }


    public static ItemConnectionFragment newInstance(String param1, String param2) {
        ItemConnectionFragment fragment = new ItemConnectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View page = inflater.inflate(R.layout.fragment_item_connection, container, false);
        recyclerView = page.findViewById(R.id.connection_list);

        DataAccessObject instance = DataAccessObject.getInstance(getContext());;

        ItemConnectionAdapter adapter = new ItemConnectionAdapter(getContext(), instance.connections, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        Button addBtn = page.findViewById(R.id.add_connection_button);
        addBtn.setOnClickListener(v -> {
            AddConnectionDialog dialog = new AddConnectionDialog();
            dialog.show(getChildFragmentManager(), "AddConnectionDialog");
        });


        return page;
    }
    @Override
    public void onConnectionAdded(String from, String to) {

        DataAccessObject db = DataAccessObject.getInstance(getContext());
        boolean foundSameConnection = false;
        for (ConnectionHeader connectionHeader : db.connections) {
            if (connectionHeader.getFrom().equals(from) && connectionHeader.getTo().equals(to)) {
                foundSameConnection = true;
            }
        }

        if (foundSameConnection) {
            Toast.makeText(getContext(), "Connection already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        if (from.equals(to)) {
            Toast.makeText(getContext(), "Connection needs two different variables", Toast.LENGTH_SHORT).show();
            return;
        }

        db.newConnection(new ConnectionHeader(from, to));
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void OnConnectionClicked(ConnectionHeader connection) {
        AnalyticsDialog dialog = new AnalyticsDialog(connection);
        dialog.show(getChildFragmentManager(), "Analytics");
    }
}