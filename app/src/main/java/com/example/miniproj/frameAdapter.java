package com.example.miniproj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class frameAdapter extends RecyclerView.Adapter<frameAdapter.FrameViewHolder> {
    Context context;
    List<Integer> frameList;
    FrameAdapterListener listener;
int row_selected=-1;

    public frameAdapter(Context context,  FrameAdapterListener listener) {
        this.context = context;
        this.frameList = getFrameList();
        this.listener = listener;
    }

    public List<Integer>getFrameList(){
        List<Integer> result=new ArrayList<>();
        result.add(R.drawable.frame1);
        result.add(R.drawable.frame2);
        result.add(R.drawable.frame3);
        result.add(R.drawable.frame4);
        result.add(R.drawable.frame5);
        result.add(R.drawable.frzme6);
        result.add(R.drawable.frame7);
        result.add(R.drawable.frame8);
        result.add(R.drawable.frame9);
        return result;


    }
    @NonNull
    @Override
    public FrameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.activity_frame_adapter,parent,false);
        return new FrameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FrameViewHolder holder, int position) {
  if(row_selected==position){
      holder.img_check.setVisibility(View.VISIBLE);
  }else{
      holder.img_check.setVisibility(View.INVISIBLE);
  }
  holder.img_frame.setImageResource(frameList.get(position));
    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }

    public class FrameViewHolder extends RecyclerView.ViewHolder {
ImageView img_check,img_frame;
        public FrameViewHolder(@NonNull View itemView) {
            super(itemView);
            img_check=itemView.findViewById(R.id.img_check);
            img_frame=itemView.findViewById(R.id.img_frame);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFrameSelected(frameList.get(getAdapterPosition()));
                    row_selected=getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
 public interface FrameAdapterListener{
        void onFrameSelected(int Frame);

 }
}