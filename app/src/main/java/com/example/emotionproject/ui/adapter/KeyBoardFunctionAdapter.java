package com.example.emotionproject.ui.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emotionproject.bean.ChatFunctionModel;
import com.example.emotionproject.R;
import com.example.emotionproject.bean.ConstantFields;
import com.example.emotionproject.ui.holder.KeyBoardFunctionHolder;

import java.util.ArrayList;

/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 4:02 PM  25/05/21
 * PackageName : com.example.spdbsoappandroid.onlineservice.ui.adapter
 * describle :
 */
public class KeyBoardFunctionAdapter extends RecyclerView.Adapter<KeyBoardFunctionHolder> {
    private ArrayList<ChatFunctionModel> chatFunctionModels;
    private Activity                     activity;
    private ItemOnClickListener          itemOnClickListener;

    public KeyBoardFunctionAdapter(Activity activity, ArrayList<ChatFunctionModel> chatFunctionModels) {
        this.activity = activity;
        this.chatFunctionModels = chatFunctionModels;
    }

    @NonNull
    @Override
    public KeyBoardFunctionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(activity)
                .inflate(R.layout.item_keyboard_function, parent, false);
        KeyBoardFunctionHolder keyBoardFunctionHolder = new KeyBoardFunctionHolder(inflate, itemOnClickListener);
        return keyBoardFunctionHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KeyBoardFunctionHolder holder, int position) {
        ImageView         iv_function_icon  = holder.iv_function_icon;
        TextView          iv_function_name  = holder.tv_function_name;
        View              itemView          = holder.itemView;
        ChatFunctionModel chatFunctionModel = chatFunctionModels.get(position);
        int               icon              = chatFunctionModel.getIcon();
        String            name              = chatFunctionModel.getName();
        iv_function_icon.setImageResource(icon);
        iv_function_name.setText(name);
        DisplayMetrics displayMetrics  = activity.getResources().getDisplayMetrics();
        int            widthPixels     = displayMetrics.widthPixels;//软键盘宽度
        int            softInputHeight = ConstantFields.softInputHeight;//软键盘高度
        //为了使里面的icon更美观，所以icon的大小是根据宽高来匹配的
        int totalSize = Math.min(softInputHeight, widthPixels);

        ViewGroup.LayoutParams imgLayoutParams = iv_function_icon.getLayoutParams();
        imgLayoutParams.width = totalSize / 6;
        imgLayoutParams.height = totalSize / 6;
        iv_function_icon.setLayoutParams(imgLayoutParams);//图片的参数

        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        /**
         *     ┌┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┐
         *     ┆                                  ┆
         *     ┆                 x                ┆
         *     ┆                                  ┆
         *     ┆  ┌┈┈┈┈┈┈┈┐                       ┆
         *     ┆  ┆       ┆      3.5x             ┆
         *     ┆  ┆       ┆                       ┆
         *     ┆  └┈┈┈┈┈┈┈┘                       ┆
         *     ┆                                  ┆
         *     ┆                 x                ┆    总高度为softInputHeight即10x,则各个控件的高度如图所示
         *     ┆  ┌┈┈┈┈┈┈┈┐                       ┆
         *     ┆  ┆       ┆                       ┆
         *     ┆  ┆       ┆      3.5x             ┆
         *     ┆  └┈┈┈┈┈┈┈┘                       ┆
         *     ┆                                  ┆
         *     ┆                x                 ┆
         *     ┆                                  ┆
         *     └┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┘
         *
         *
         */
        int distance = softInputHeight / 10;
        layoutParams.topMargin = distance;
        layoutParams.height = (int) (distance * 3.5);
        layoutParams.width = widthPixels / 4;
        itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return chatFunctionModels.size();
    }

    public interface ItemOnClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
}
