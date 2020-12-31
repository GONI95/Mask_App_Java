package com.example.mask_app_java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mask_app_java.model.Store;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private List<Store> mItems = new ArrayList<>();

    Context context;
    public StoreAdapter(MainActivity mainActivity) {
        context = mainActivity;
    }

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
    
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = mItems.get(position);

        holder.name.setText(store.getName());
        holder.address.setText(store.getAddr());
        holder.distance.setText(Math.round(store.getDistance_unit()*100)/100.0 + "km");
        /*
         String.format("%.2f", store.getDistance_unit()) + "km"
         둘 다 반올림을 하지만 Math.round()는 소수점 마지막 숫자가 0이면 절삭한다.
         */
        
        // distance_unit은 double이기 때문에 ""+를 추가해줘야함

        String remainStat = "마스크 재고 수준";
        String mask_count = "마스크 재고 수량";
        int color;    // 재고 수준 색상으로 가시화

        switch (store.getRemainStat()){
            case "plenty":
                remainStat = "충분";
                mask_count = "100개 이상";
                color = ContextCompat.getColor(context, R.color._plenty);
                break;
            case "some":
                remainStat = "여유";
                mask_count = "30개 이상";
                color = ContextCompat.getColor(context, R.color._some);
                break;
            case "few":
                remainStat = "매진 임박";
                mask_count = "2개 이상";
                color = ContextCompat.getColor(context, R.color._few);
                break;
            case "empty":
                remainStat = "재고 부족";
                mask_count = "1개 이하";
                color = ContextCompat.getColor(context, R.color._empty);
                break;
            case "break":
                remainStat = "재고 없음";
                mask_count = "판매 중지";
                color = ContextCompat.getColor(context, R.color._break);
                break;
            default:
                remainStat = "재고 수준 오류";
                mask_count = "재고 수량 오류";
                color = ContextCompat.getColor(context, R.color._error);
                break;
                /*
                case "null":
                remainStat = "재고 파악불가";
                mask_count = "재고 파악불가";
                color = ContextCompat.getColor(context, R.color._error);
                break;
                 */

        }
        holder.remain.setText(remainStat);
        holder.remain.setTextColor(color);
        // @SuppressLint 추가해줘야함 (color 인식을 못함)
        holder.count.setText(mask_count);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 아이템 뷰의 정보를 가지고 있는 ViewHolder 클래스
    static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;
        TextView remain;
        TextView count;
        TextView distance;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_textView);
            address = itemView.findViewById(R.id.addr_textView);
            remain = itemView.findViewById(R.id.remain_textView);
            count = itemView.findViewById(R.id.count_textView);
            distance = itemView.findViewById(R.id.distance_textView);
        }
    }
}
