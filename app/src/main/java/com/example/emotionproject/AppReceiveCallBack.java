package com.example.emotionproject;

/**
 * @author ChenYu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 11:25 AM  2020/7/8
 * PackageName : com.example.spdbsoappandroid.textonline.imp
 * describle : 点击表情的回调接口
 */
public interface AppReceiveCallBack {
    /**
     *
     * @param key 文件名
     * @param value  对应的中文含义
     */
    void onAppReceiveCallBack(String key, String value);
}
