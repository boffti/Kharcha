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
    private final ExpenseRecyclerViewInterface expenseRecyclerViewInterface;
    List<ExpenseModel> expenseList;
    Context context;

    public ExpenseListAdapter(List<ExpenseModel> expenseList, Context context, ExpenseRecyclerViewInterface expenseRecyclerViewInterface) {
        this.expenseList = expenseList;
        this.context = context;
        this.expenseRecyclerViewInterface = expenseRecyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view, expenseRecyclerViewInterface);
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

        public MyViewHolder(@NonNull View itemView, ExpenseRecyclerViewInterface expenseRecyclerViewInterface) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_tag = itemView.findViewById(R.id.tv_tag);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(expenseRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            expenseRecyclerViewInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(expenseRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            expenseRecyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
