package com.example.miniproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class stickerAdapter extends RecyclerView.Adapter<stickerAdapter.stickerViewHolder> {
    Context context;
    List<Integer> stickerList;
    StickerAdapterListener listener;
    int row_selected=-1;

    public stickerAdapter(Context context, StickerAdapterListener listener) {
        this.context = context;
        this.stickerList = getStickerList();
        this.listener = listener;

}
    public List<Integer>getStickerList(){
        List<Integer> result=new ArrayList<>();
        result.add(R.drawable.sticker1);
        result.add(R.drawable.sticker2);
        result.add(R.drawable.sticker3);
        result.add(R.drawable.sticker4);
        result.add(R.drawable.sticker5);
        result.add(R.drawable.sticker6);
        result.add(R.drawable.sticker7);
        result.add(R.drawable.sticker8);
        result.add(R.drawable.sticker9);
        result.add(R.drawable.sticker10);
        result.add(R.drawable.sticker11);
        result.add(R.drawable.sticker12);
        result.add(R.drawable.sticker13);


        return result;


    }
    @NonNull
    @Override
    public stickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.activity_sticker_adapter,parent,false);
        return new stickerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull stickerAdapter.stickerViewHolder holder, int position) {
        if(row_selected==position){
            holder.img_check.setVisibility(View.VISIBLE);
        }else{
            holder.img_check.setVisibility(View.INVISIBLE);
        }
        holder.img_frame.setImageResource(stickerList.get(position));
    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    public class stickerViewHolder extends RecyclerView.ViewHolder {
        ImageView img_check,img_frame;
        public stickerViewHolder(@NonNull View itemView) {
            super(itemView);
            img_check=itemView.findViewById(R.id.img_StickerCheck);
            img_frame=itemView.findViewById(R.id.img_StickerFrame);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStickerSelected(stickerList.get(getAdapterPosition()));
                    row_selected=getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
    public interface StickerAdapterListener{
        void onStickerSelected(int sticker);

    }
}