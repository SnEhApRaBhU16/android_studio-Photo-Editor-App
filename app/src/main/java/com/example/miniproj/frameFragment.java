package com.example.miniproj;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class frameFragment extends BottomSheetDialogFragment implements frameAdapter.FrameAdapterListener {
 RecyclerView recycleFrame;
 Button addframe;
 int frame_selected=-1;
 AddFrameListener listener;
 public void setListener(AddFrameListener listener){
     this.listener=listener;
 }
 static frameFragment instance;

 public static frameFragment getInstance(){
     if(instance==null)
         instance=new frameFragment();
     return instance;

 }
 public frameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.fragment_frame, container, false);
        recycleFrame=(RecyclerView) itemView.findViewById(R.id.recyclerFrame);
        addframe=(Button) itemView.findViewById(R.id.addFrameBtn);
        recycleFrame.setHasFixedSize(true);
        recycleFrame.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recycleFrame.setAdapter(new frameAdapter(getContext(),this));
        addframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddFrame(frame_selected);

            }
        });
        return itemView;
    }
    public void onFrameSelected(int frame){
        frame_selected=frame;
    }
}