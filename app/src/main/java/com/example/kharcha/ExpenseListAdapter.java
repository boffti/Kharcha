package com.example.kharcha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.MyViewHolder> {

    List<ExpenseModel> expenseList;
    Context context;

    public ExpenseListAdapter(List<ExpenseModel> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_name.setText(expenseList.get(position).getName());
        holder.tv_amount.setText("$ "+String.valueOf(expenseList.get(position).getAmount()));
        holder.tv_tag.setText(expenseList.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_amount;
        TextView tv_tag;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_tag = itemView.findViewById(R.id.tv_tag);
        }
    }
}
