package com.example.emotionproject.bean;


/**
 * 表情
 */
public enum EmotionModel {
    /**
     * 文件名 -- 中文意思
     */
    angry("angry.webp", "(生气)"),
    anory("annoyed.webp", "(恼怒)"),
    hint("hint.webp", "(暗示!)"),
    kiss("kiss.webp", "(亲)"),
    oh("oh.webp", "(惊讶)"),
    scold("scold.webp", "(骂)"),
    stilly("stilly.webp", "(安静!)"),
    yun("yun.webp", "(晕?)");

    private final String key;
    private final String value;

    EmotionModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static EmotionModel getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (EmotionModel temp : EmotionModel.values()) {
            if (temp.getKey().equals(key)) {
                return temp;
            }
        }
        return null;
    }

    //根据value获取枚举
    public static EmotionModel getEnumByValue(String value) {
        if (null == value) {
            return null;
        }
        for (EmotionModel temp : EmotionModel.values()) {
            if (temp.getValue().equals(value)) {
                return temp;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
