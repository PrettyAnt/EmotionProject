package com.example.emotionproject.manager;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.emotionproject.AppReceiveCallBack;
import com.example.emotionproject.R;
import com.example.emotionproject.SoftKeyBoardListener;
import com.example.emotionproject.bean.ConstantFields;
import com.example.emotionproject.ui.fragment.EmotionFragment;
import com.example.emotionproject.ui.fragment.FunctionFragment;

/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 4:06 下午  24/08/21
 * PackageName : com.example.spdbsoappandroid.textonline.manager
 * describle : 软键盘管理器关键类
 */
public class KeyBoardManager implements View.OnClickListener, View.OnTouchListener, TextWatcher {

    private static final String SHARE_PREFERENCE_NAME              = "EmotionKeyboard";
    private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";
    private static final String SHARE_PREFERENCE_FRISTINSTALL = "fristInstall";

    private              AppCompatActivity   activity;
    private              View                rootKeyBoardView;
    private              View                contentView;
    private              CheckBox            inputMode;
    private              Button              speak;
    private              EditText            input;
    private              CheckBox            expression;
    private              CheckBox            add;
    private              TextView            send;
    private              FrameLayout         flKeyboard;
    private              InputMethodManager  mInputManager;
    private              SharedPreferences   sp;
    private              boolean             workState = true;
    private static final int                 EMOTION   = 0;//表情
    private static final int                 FUNCTION  = 1;//加号更多功能
    private              EmotionFragment     emotionFragment;
    private              FunctionFragment    functionFragment;
    private              FragmentTransaction fragmentTransaction;
    private              AppReceiveCallBack  appReceiveCallBack;


    public static KeyBoardManager with(AppCompatActivity activity) {
        KeyBoardManager keyBoardManager = new KeyBoardManager();
        keyBoardManager.activity = activity;
        keyBoardManager.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyBoardManager.sp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return keyBoardManager;
    }

    /**
     * 绑定软键盘
     *
     * @param view
     * @return
     */
    public KeyBoardManager bindKeyBoardView(View view) {
        this.rootKeyBoardView = view;
        return this;
    }

    /**
     * 绑定聊天内容布局 防止跳闪
     *
     * @param contentView
     * @return
     */
    public KeyBoardManager bindContentView(View contentView) {
        this.contentView = contentView;
        return this;
    }

    /**
     * 透传给发送表情的fragment
     *
     * @param appReceiveCallBack
     * @return
     */
    public KeyBoardManager bindAppReceiveCallBack(AppReceiveCallBack appReceiveCallBack) {
        this.appReceiveCallBack = appReceiveCallBack;
        return this;
    }

    /**
     * @return
     */
    public KeyBoardManager build() {
        initView();
        initEvent();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        hideEmotionLayout(true);//隐藏表情布局，显示软件盘
        addSoftKeyBoardListener();//添加软键盘的监听
        return this;
    }


    /**
     * 初始化控件
     */
    private void initView() {
        //输入方式 语音、文字
        inputMode = rootKeyBoardView.findViewById(R.id.cb_mode);
        //语音输入
        speak = rootKeyBoardView.findViewById(R.id.btn_click_speak);
        //聊天框
        input = rootKeyBoardView.findViewById(R.id.et_content);
        //表情
        expression = rootKeyBoardView.findViewById(R.id.cb_expression);
        //添加(相机、相册)
        add = rootKeyBoardView.findViewById(R.id.cb_add);
        //发送按钮
        send = rootKeyBoardView.findViewById(R.id.tv_send);
        //表情、相机面板
        flKeyboard = rootKeyBoardView.findViewById(R.id.fl_keyboard);
        enterWork();//初始化状态
        boolean fristInstall = sp.getBoolean(SHARE_PREFERENCE_FRISTINSTALL, true);
        if (fristInstall) {
            //第一次安装后首次打开页面时，强制打开软键盘，为了计算软件盘的高度，从而确定表情栏的高度
            //不打开也可以，表情栏的高度已经给了默认值，但第一次显示的时候高度可能不完全适配
            showSoftInput(500);
            sp.edit().putBoolean(SHARE_PREFERENCE_FRISTINSTALL, false).apply();
        }
    }

    /**
     * 进人工时,显示表情、加号按钮
     */
    public void enterWork() {
        workState = true;
        send.setVisibility(View.GONE);
        expression.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
    }

    /**
     * 退人工(自助状态)时，只显示发送按钮
     */
    public void exitWork() {
        workState = false;
        send.setVisibility(View.VISIBLE);
        expression.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
    }

    /**
     * 点击聊天条目设置文字
     *
     * @param menuName
     */
    public void setText(String menuName) {
        if (TextUtils.isEmpty(menuName)) {
            return;
        }
        input.setText(menuName);
        //光标靠后
        input.setSelection(input.length());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        inputMode.setOnClickListener(this);
        expression.setOnClickListener(this);
        add.setOnClickListener(this);
        send.setOnClickListener(this);
        input.setOnTouchListener(this);
        speak.setOnTouchListener(this);
        input.setOnKeyListener(new View.OnKeyListener() {
            /**
             * 注意此处! 按下软件盘的发送按钮，该方法会执行两次ACTION_DOWN、ACTION_UP，
             * 所以为了点击软键盘不让他收起，则两次的事件都要给消费掉，即return true
             * @param v
             * @param keyCode
             * @param event
             * @return
             */
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Toast.makeText(activity, input.getText(), Toast.LENGTH_SHORT).show();
                    input.setText("");
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });
        input.addTextChangedListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.et_content) {

            if (event.getAction() == MotionEvent.ACTION_UP && flKeyboard.isShown()) {
                expression.setChecked(false);
                add.setChecked(false);
                lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                hideEmotionLayout(true);//隐藏表情布局，显示软件盘

                //软件盘显示后，释放内容高度
                input.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unlockContentHeightDelayed();
                    }
                }, 200L);
            }
        } else if (v.getId() == R.id.btn_click_speak) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    speak.setText("松开 结束");
                    break;

                case MotionEvent.ACTION_UP:
                    speak.setText("按住 说话");
                    break;
                default:
                    break;
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_send) {
            Toast.makeText(activity, input.getText(), Toast.LENGTH_SHORT).show();
            input.setText("");
        } else if (v.getId() == R.id.cb_mode) {
            //输入方式 语音、文本
            if (inputMode.isChecked()) {
                expression.setChecked(false);
                add.setChecked(false);
                input.setVisibility(View.GONE);//输入框
                speak.setVisibility(View.VISIBLE);//按住说话
                hideEmotionLayout(false);

            } else {
                input.setVisibility(View.VISIBLE);
                speak.setVisibility(View.GONE);
                showSoftInput(0);
            }

        } else if (v.getId() == R.id.cb_expression) {
            //表情
            speak.setVisibility(View.GONE);
            input.setVisibility(View.VISIBLE);
            input.requestFocus();
            if (expression.isChecked()) {
                chooseKeyBoardPage(EMOTION);
                inputMode.setChecked(false);
                add.setChecked(false);
                if (isSoftInputShown()) {
                    lockContentHeight();
                    showEmotionLayout();
                    unlockContentHeightDelayed();
                } else {
                    showEmotionLayout();
                }
            } else {
                lockContentHeight();
                hideEmotionLayout(true);
                unlockContentHeightDelayed();
            }


        } else if (v.getId() == R.id.cb_add) {
            //相机
            speak.setVisibility(View.GONE);
            input.setVisibility(View.VISIBLE);
            if (add.isChecked()) {
                chooseKeyBoardPage(FUNCTION);
                expression.setChecked(false);
                inputMode.setChecked(false);
                if (isSoftInputShown()) {
                    lockContentHeight();
                    showEmotionLayout();
                    unlockContentHeightDelayed();
                } else {
                    showEmotionLayout();
                }
            } else {
                lockContentHeight();
                hideEmotionLayout(true);
                unlockContentHeightDelayed();
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() > 6000) {
            input.setText(s.toString().substring(0, 6000));
            input.setSelection(input.getText().toString().length());
        }
        if (s.length() > 0) {
            //显示发送按钮
            showSendBtn();
        } else {
            //隐藏发送按钮
            hideSendBtn();
        }
    }

    private void chooseKeyBoardPage(int type) {
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        fragmentTransaction = supportFragmentManager.beginTransaction();
        hintFragment(fragmentTransaction);
        switch (type) {
            case EMOTION:
                if (emotionFragment == null) {
                    emotionFragment = new EmotionFragment();
                    emotionFragment.setAppReceiveCallBack(appReceiveCallBack);
                    fragmentTransaction.add(R.id.fl_keyboard, emotionFragment);
                } else {
                    fragmentTransaction.show(emotionFragment);
                }
                break;
            case FUNCTION:
                if (functionFragment == null) {
                    functionFragment = new FunctionFragment();
                    fragmentTransaction.add(R.id.fl_keyboard, functionFragment);
                } else {
                    fragmentTransaction.show(functionFragment);
                }
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private void hintFragment(androidx.fragment.app.FragmentTransaction fragmentTransaction) {
        if (emotionFragment != null) {
            fragmentTransaction.hide(emotionFragment);
        }
        if (functionFragment != null) {
            fragmentTransaction.hide(functionFragment);
        }
    }

    /**
     * 发送按钮
     */
    private void hideSendBtn() {
        if (workState) {//人工
            add.setVisibility(View.VISIBLE);
            send.setVisibility(View.GONE);
        } else {
            add.setVisibility(View.GONE);
            send.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 发送按钮
     */
    private void showSendBtn() {
        if (workState) {//人工
            send.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
        } else {
            add.setVisibility(View.GONE);
            send.setVisibility(View.VISIBLE);
        }


    }


    /**
     * 是否显示软件盘
     *
     * @return
     */
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private void showSoftInput(int delayMillis) {
        input.requestFocus();
        input.postDelayed(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(input, 0);
            }
        }, delayMillis);
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    /**
     * 显示表情布局
     */
    private void showEmotionLayout() {
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = getKeyBoardHeight();
            flKeyboard.post(new Runnable() {
                @Override
                public void run() {
                    ConstantFields.softInputHeight = flKeyboard.getHeight();//保存一份到本地
                }
            });
        }
        hideSoftInput();
        flKeyboard.getLayoutParams().height = softInputHeight;
        flKeyboard.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    private void hideEmotionLayout(boolean showSoftInput) {
        flKeyboard.setVisibility(View.GONE);
        if (showSoftInput) {
            showSoftInput(0);
        } else {
            hideSoftInput();
        }
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
        softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        //存一份到本地
        if (softInputHeight > 0) {
            sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
            ConstantFields.softInputHeight = softInputHeight;//保存一份到本地
        }
        return softInputHeight;
    }

    /**
     * 添加软键盘的监听
     * 此方法非必要
     * 根据自己的业务来看需不需要该方法(我们项目弹出软键盘会收起其他菜单)
     * 也可以通过此方法来获取软键盘的高度
     */
    private void addSoftKeyBoardListener() {
        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(activity);
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Log.i("PrettyAnt", "keyBoardShow->软键盘高度" + height);
            }

            @Override
            public void keyBoardHide(int height) {
                Log.i("PrettyAnt", "keyBoardHide->软键盘高度" + height);
            }
        });
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        params.height = contentView.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    private void unlockContentHeightDelayed() {
        input.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) contentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 获取软键盘高度，由于第一次直接弹出表情时会出现小问题
     *
     * @return
     */
    private int getKeyBoardHeight() {
        return sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 1300);

    }


}
