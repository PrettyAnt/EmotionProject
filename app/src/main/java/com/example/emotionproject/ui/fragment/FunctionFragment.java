package com.example.emotionproject.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emotionproject.bean.ChatFunctionModel;
import com.example.emotionproject.R;
import com.example.emotionproject.ui.adapter.KeyBoardFunctionAdapter;

import java.util.ArrayList;

/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 6:24 下午  25/08/21
 * PackageName : com.example.spdbsoappandroid.textonline.ui.fragment
 * describle : 拍照、相机、收藏等，如果业务过多，像微信那样超过一页数据，则可以使用viewpage+fragment来实现分页
 */
public class FunctionFragment extends Fragment implements KeyBoardFunctionAdapter.ItemOnClickListener {
    private              ArrayList<ChatFunctionModel> chatFunctionModels = new ArrayList<>();
    private              RecyclerView                 functionRecylerView;
    private              KeyBoardFunctionAdapter      keyBoardFunctionAdapter;
    private static final String                       id_photo           = "photo";
    private static final String                       id_camera          = "camera";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_function, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initData() {
        chatFunctionModels.clear();
        /**
         * 同EmotionFragment，也可以通过枚举来构造数据
         */
        ChatFunctionModel cameraModel = new ChatFunctionModel();
        cameraModel.setName("相机");
        cameraModel.setIcon(R.drawable.icon_camera);
        cameraModel.setIconId(id_camera);

        ChatFunctionModel photoModel = new ChatFunctionModel();
        photoModel.setName("相册");
        photoModel.setIcon(R.drawable.icon_chat_photo);
        photoModel.setIconId(id_photo);

        chatFunctionModels.add(cameraModel);
        chatFunctionModels.add(photoModel);
    }

    private void initView(View view) {
        functionRecylerView =view.findViewById(R.id.rv_keyboard_function);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        keyBoardFunctionAdapter = new KeyBoardFunctionAdapter(getActivity(), chatFunctionModels);
        functionRecylerView.setLayoutManager(gridLayoutManager);
        functionRecylerView.setAdapter(keyBoardFunctionAdapter);
        keyBoardFunctionAdapter.setItemOnClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.iv_function_icon) {
            ChatFunctionModel chatFunctionModel = chatFunctionModels.get(position);
            String            iconId            = chatFunctionModel.getIconId();
            switch (iconId) {
                case id_camera:
                    Toast.makeText(getActivity(),"拍照",Toast.LENGTH_SHORT).show();
                    break;
                case id_photo:
                    Toast.makeText(getActivity(),"打开相册",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

}
