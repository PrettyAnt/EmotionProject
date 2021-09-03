package com.example.emotionproject.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emotionproject.AppReceiveCallBack;
import com.example.emotionproject.bean.EmotionModel;
import com.example.emotionproject.R;
import com.example.emotionproject.ui.adapter.KeyBoardEmotionAdapter;

/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 6:12 下午  25/08/21
 * PackageName : com.example.spdbsoappandroid.textonline.ui.fragment
 * describle : 表情页面，如果表情过多，像微信那样超过一页数据，则可以使用指示器 TabLayout+viewpage+fragment来实现分页
 */
public class EmotionFragment extends Fragment implements KeyBoardEmotionAdapter.ItemOnClickListener {

    private KeyBoardEmotionAdapter keyBoardEmotionAdapter;
    private AppReceiveCallBack     appReceiveCallBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emotion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View view) {
        RecyclerView      emotionRecyclerView = view.findViewById(R.id.rv_keyboard_emotion);
        GridLayoutManager gridLayoutManager   = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        keyBoardEmotionAdapter = new KeyBoardEmotionAdapter(getActivity());
        emotionRecyclerView.setLayoutManager(gridLayoutManager);
        emotionRecyclerView.setAdapter(keyBoardEmotionAdapter);
        keyBoardEmotionAdapter.setItemOnClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.iv_emotion_icon) {
            EmotionModel[] emotionModels = EmotionModel.values();
            EmotionModel   emotionModel  = emotionModels[position];
            String         emotionModelKey   = emotionModel.getKey();
            String         emotionModelValue = emotionModel.getValue();
            String         value           = emotionModelValue.replace("(", "").replace(")", "");
            appReceiveCallBack.onAppReceiveCallBack(emotionModelKey, value);
        }
    }

    public void setAppReceiveCallBack(AppReceiveCallBack appReceiveCallBack) {
        this.appReceiveCallBack = appReceiveCallBack;
    }
}
