package com.example.emotionproject.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emotionproject.bean.ConstantFields;
import com.example.emotionproject.bean.EmotionModel;
import com.example.emotionproject.R;
import com.example.emotionproject.ui.holder.KeyBoardEmotionHolder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 10:14 上午  27/08/21
 * PackageName : com.example.spdbsoappandroid.textonline.ui.adapter
 * describle :
 */
public class KeyBoardEmotionAdapter extends RecyclerView.Adapter<KeyBoardEmotionHolder> {
    private EmotionModel[] emotionModels;
    private Activity       activity;
    private ItemOnClickListener itemOnClickListener;

    public KeyBoardEmotionAdapter(Activity activity) {
        this.activity = activity;
        this.emotionModels = EmotionModel.values();
    }

    @NonNull
    @Override
    public KeyBoardEmotionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(activity)
                .inflate(R.layout.item_keyboard_emotion, parent, false);
        KeyBoardEmotionHolder keyBoardEmotionHolder = new KeyBoardEmotionHolder(inflate, itemOnClickListener);
        return keyBoardEmotionHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KeyBoardEmotionHolder holder, int position) {
        ImageView    iv_emotion_icon = holder.iv_emotion_icon;
        View         itemView        = holder.itemView;
        EmotionModel emotionModel    = emotionModels[position];
        String       key             = emotionModel.getKey();
        try {
            InputStream           open    = activity.getAssets().open("emoji/" + key);
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap                bitmap  = BitmapFactory.decodeStream(open, null, options);
            iv_emotion_icon.setImageBitmap(bitmap);
            open.close();
            DisplayMetrics displayMetrics  = activity.getResources().getDisplayMetrics();
            int            widthPixels     = displayMetrics.widthPixels;//软键盘宽度
            int            softInputHeight = ConstantFields.softInputHeight;//软键盘高度
            //为了使里面的icon更美观，所以icon的大小是根据宽高来匹配的
            int                    totalSize    = Math.min(softInputHeight, widthPixels);
            ViewGroup.LayoutParams layoutParams = iv_emotion_icon.getLayoutParams();
            layoutParams.width = (int) (totalSize / 4.5);
            iv_emotion_icon.setLayoutParams(layoutParams);

            int                            distance             = softInputHeight / 14;
            GridLayoutManager.LayoutParams itemViewLayoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
            itemViewLayoutParams.topMargin = distance;
            itemViewLayoutParams.height = (int) (distance * 5.5);
            itemViewLayoutParams.width = widthPixels / 4;
            itemView.setLayoutParams(itemViewLayoutParams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return emotionModels.length;
    }

    public interface ItemOnClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }
}
