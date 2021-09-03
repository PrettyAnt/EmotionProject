package com.example.emotionproject.ui.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.emotionproject.R;
import com.example.emotionproject.ui.adapter.KeyBoardEmotionAdapter;

/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 10:15 上午  27/08/21
 * PackageName : com.example.spdbsoappandroid.textonline.ui.holder
 * describle :
 */
public class KeyBoardEmotionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private  KeyBoardEmotionAdapter.ItemOnClickListener itemOnClickListener;
    public ImageView                                    iv_emotion_icon;

    public KeyBoardEmotionHolder(View inflate, KeyBoardEmotionAdapter.ItemOnClickListener itemOnClickListener) {
        super(inflate);
        this.itemOnClickListener = itemOnClickListener;
        iv_emotion_icon = inflate.findViewById(R.id.iv_emotion_icon);
        iv_emotion_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_emotion_icon) {
            if (itemOnClickListener != null) {
                itemOnClickListener.onItemClick(iv_emotion_icon,getLayoutPosition());
            }
        }
    }
}
