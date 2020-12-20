package com.example.mask_app_java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mask_app_java.model.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private List<Store> mItems = new ArrayList<>();

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_store, parent, false);


        return new StoreViewHolder(view);
    }

    
    public void UpdateItems(List<Store> items){
        mItems = items;
        notifyDataSetChanged(); //UI 갱신
    }
    
    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = mItems.get(position);

        holder.name.setText(store.getName());
        holder.address.setText(store.getAddr());
        holder.remain.setText(store.getRemainStat());
        holder.count.setText("100개 이상");
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 아이템 뷰의 정보를 가지고 있는 클래스
    static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;
        TextView remain;
        TextView count;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_textView);
            address = itemView.findViewById(R.id.addr_textView);
            remain = itemView.findViewById(R.id.remain_textView);
            count = itemView.findViewById(R.id.count_textView);
        }
    }
}
