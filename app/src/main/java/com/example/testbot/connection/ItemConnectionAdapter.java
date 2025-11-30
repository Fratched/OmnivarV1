package com.example.testbot.connection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbot.R;
import com.example.testbot.model.ConnectionHeader;
import com.example.testbot.model.VariableHeader;
import com.example.testbot.model.VariableType;

import java.util.List;

public class ItemConnectionAdapter extends RecyclerView.Adapter<ItemConnectionAdapter.ItemConnectionViewHolder> {

    public List<ConnectionHeader> connections;
    private LayoutInflater inflater;

    private OnConnectionClickedListener listener;

    public ItemConnectionAdapter(Context context, List<ConnectionHeader> connections, OnConnectionClickedListener listener) {
        this.connections = connections;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public interface OnConnectionClickedListener {
        void OnConnectionClicked(ConnectionHeader connection);
    }

    @NonNull
    @Override
    public ItemConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.connection_item_entry, parent, false);
        return new ItemConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemConnectionViewHolder holder, int position) {
        ConnectionHeader v = connections.get(position);
        holder.itemView.setOnClickListener(e -> listener.OnConnectionClicked(v));
        holder.name1.setText(v.getFrom());
        holder.name2.setText(v.getTo());
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }


    public static class ItemConnectionViewHolder extends RecyclerView.ViewHolder {

        TextView name1;
        TextView name2;

        public ItemConnectionViewHolder(@NonNull View itemView) {
            super(itemView);
            name1 = itemView.findViewById(R.id.connection_item_name1);
            name2 = itemView.findViewById(R.id.connection_item_name2);
        }
    }
}
