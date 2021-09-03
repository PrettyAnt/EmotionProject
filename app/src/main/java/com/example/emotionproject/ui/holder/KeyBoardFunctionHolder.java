package com.example.emotionproject.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.emotionproject.R;
import com.example.emotionproject.ui.adapter.KeyBoardFunctionAdapter;

/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 4:03 PM  25/05/21
 * PackageName : com.example.spdbsoappandroid.onlineservice.ui.holder
 * describle :
 */
public class KeyBoardFunctionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private  KeyBoardFunctionAdapter.ItemOnClickListener itemOnClickListener;
    public ImageView                                     iv_function_icon;
    public TextView  tv_function_name;

    public KeyBoardFunctionHolder(View inflate, KeyBoardFunctionAdapter.ItemOnClickListener itemOnClickListener) {
        super(inflate);
        this.itemOnClickListener = itemOnClickListener;
        iv_function_icon = inflate.findViewById(R.id.iv_function_icon);
        tv_function_name = inflate.findViewById(R.id.tv_function_name);
        iv_function_icon.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_function_icon) {
            if (itemOnClickListener != null) {
                itemOnClickListener.onItemClick(iv_function_icon,getLayoutPosition());
            }
        }
    }
}
