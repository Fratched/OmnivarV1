package com.example.testbot.variable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbot.R;
import com.example.testbot.model.VariableHeader;
import com.example.testbot.model.VariableType;

import java.util.List;

public class ItemVariableAdapter extends RecyclerView.Adapter<ItemVariableAdapter.ItemVariableViewHolder> {

    public List<VariableHeader> variables;
    private LayoutInflater inflater;

    public ItemVariableAdapter(Context context, List<VariableHeader> variables) {
        this.variables = variables;
        this.inflater = LayoutInflater.from(context);
    }

    private int getIconForVariableType(VariableType type) {
        switch (type) {
            case BOOLEAN:
                return R.drawable.yimgyang;
            case TIME:
                return R.drawable.baseline_watch_later_24;
            case RANGE:
                return R.drawable.range;
            default:
                return R.drawable.baseline_watch_later_24;
        }
    }


    @NonNull
    @Override
    public ItemVariableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.variable_item_entry, parent, false);
        return new ItemVariableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemVariableViewHolder holder, int position) {
        VariableHeader v = variables.get(position);
        holder.nameView.setText(v.getName());
        holder.iconView.setImageResource(getIconForVariableType(v.getUnit()));



    }

    @Override
    public int getItemCount() {
        return variables.size();
    }


    public static class ItemVariableViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        ImageView iconView;

        public ItemVariableViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.variable_item_entry_name);
            iconView = itemView.findViewById(R.id.variable_item_entry_icon);
        }
    }
}
