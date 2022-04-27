package com.example.kharcha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CatListAdapter extends RecyclerView.Adapter<CatListAdapter.MyViewHolder> {
    private final CatRecyclerViewInterface catRecyclerViewInterface;
            List<String> catList;
            Context context;

    public CatListAdapter(List<String> catList, Context context, CatRecyclerViewInterface catRecyclerViewInterface) {
            this.catList = catList;
            this.context = context;
            this.catRecyclerViewInterface = catRecyclerViewInterface;
            }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view, catRecyclerViewInterface);
            return holder;
            }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tv_catName.setText(catList.get(position));
            }
    @Override
    public int getItemCount() {
        return catList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_catName;

        public MyViewHolder(@NonNull View itemView, CatRecyclerViewInterface catRecyclerViewInterface) {
            super(itemView);
            tv_catName = itemView.findViewById(R.id.tv_catName);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(catRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            catRecyclerViewInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(catRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            catRecyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
