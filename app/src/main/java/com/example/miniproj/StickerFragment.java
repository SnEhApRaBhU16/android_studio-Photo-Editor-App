package com.example.miniproj;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class StickerFragment extends BottomSheetDialogFragment implements stickerAdapter.StickerAdapterListener {
    RecyclerView recycleSticker;
    Button addSticker;
    int Sticker_selected=-1;
    AddStickerListener listener;
    public void setListener(AddStickerListener listener){
        this.listener=listener;
    }
    static StickerFragment instance;

    public static StickerFragment getInstance(){
        if(instance==null)
            instance=new StickerFragment();
        return instance;

    }
    public StickerFragment() {
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
        View itemView= inflater.inflate(R.layout.fragment_sticker, container, false);
        recycleSticker=(RecyclerView) itemView.findViewById(R.id.recyclerSticker);
        addSticker=(Button) itemView.findViewById(R.id.addStickerBtn);
        recycleSticker.setHasFixedSize(true);
        recycleSticker.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recycleSticker.setAdapter(new stickerAdapter(getContext(),this));
        addSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddSticker(Sticker_selected);

            }
        });
        return itemView;
    }

    @Override
    public void onStickerSelected(int sticker) {
        Sticker_selected=sticker;
    }
}