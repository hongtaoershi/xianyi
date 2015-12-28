package com.xianyi.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.xianyi.R;
import com.xianyi.customviews.residelayout.SlidingMenu;
import com.xianyi.fragment.BaseFragment;
import com.xianyi.fragment.ClassifyFragment;
import com.xianyi.fragment.FindFragment;
import com.xianyi.fragment.IntegralFragment;
import com.xianyi.fragment.MessageFragment;
import com.xianyi.localalbum.ui.DynamicPost;
import com.xianyi.utils.LogUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    private SlidingMenu mMenu;
    Context mContext;
    Handler mHandler = new Handler() {
    };

    // 分类
    public static final int HOME_TAB_INDEX_0 = 0;
    // 发现
    public static final int HOME_TAB_INDEX_1 = 1;
    // 消息
    public static final int HOME_TAB_INDEX_2 = 2;
    // 积分
    public static final int HOME_TAB_INDEX_3 = 3;

    private ClassifyFragment mHomeFragment0;
    private FindFragment mHomeFragment1;
    private MessageFragment mHomeFragment2;
    private IntegralFragment mHomeFragment3;
    private FragmentManager mFragmentManager;

    BaseFragment curFragment;
    TextView tab0, tab1, tab2, tab3, tab4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initTabMenuDrawable();
        mMenu = (SlidingMenu) findViewById(R.id.id_menu);
        tab0 = (TextView) findViewById(R.id.tab0);
        tab1 = (TextView) findViewById(R.id.tab1);
        tab2 = (TextView) findViewById(R.id.tab2);
        tab3 = (TextView) findViewById(R.id.tab3);
        tab4 = (TextView) findViewById(R.id.tab4);

        mMenu = (SlidingMenu) findViewById(R.id.id_menu);

        mFragmentManager = getSupportFragmentManager();
        onTabSelected(0);

        initLeftMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    Drawable drawable_tab0_normal, drawable_tab1_normal, drawable_tab3_normal, drawable_tab4_normal;
    Drawable drawable_tab0_pressed, drawable_tab1_pressed, drawable_tab3_pressed, drawable_tab4_pressed;

    public void initTabMenuDrawable() {
        drawable_tab0_normal = getResources().getDrawable(R.drawable.tab_sort_normal);
        drawable_tab1_normal = getResources().getDrawable(R.drawable.tab_find_normal);
        drawable_tab3_normal = getResources().getDrawable(R.drawable.tab_message_normal);
        drawable_tab4_normal = getResources().getDrawable(R.drawable.tab_score_normal);
        drawable_tab0_pressed = getResources().getDrawable(R.drawable.tab_sort_pressed);
        drawable_tab1_pressed = getResources().getDrawable(R.drawable.tab_find_pressed);
        drawable_tab3_pressed = getResources().getDrawable(R.drawable.tab_message_pressed);
        drawable_tab4_pressed = getResources().getDrawable(R.drawable.tab_score_pressed);
    }

    public void initFragment() {
        mHomeFragment0 = new ClassifyFragment();
        mHomeFragment1 = new FindFragment();
        mHomeFragment2 = new MessageFragment();
        mHomeFragment3 = new IntegralFragment();

    }

    public void toggleMenu(View view) {
        mMenu.toggle();
    }

    public void initLeftMenu() {
        findViewById(R.id.im_menu_icon).setOnClickListener(this);
        findViewById(R.id.ly_my_collect).setOnTouchListener(this);
        findViewById(R.id.ly_my_xianyi).setOnTouchListener(this);
        findViewById(R.id.ly_trade).setOnTouchListener(this);
        findViewById(R.id.ly_browse_history).setOnTouchListener(this);
        findViewById(R.id.ly_shop).setOnTouchListener(this);
        findViewById(R.id.ly_account_management).setOnTouchListener(this);
        findViewById(R.id.ly_setting).setOnTouchListener(this);
        ;
    }


    public void onTabSelected(int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (index) {
            case HOME_TAB_INDEX_0:
                hideFragments(transaction);
                if (null == mHomeFragment0) {
                    mHomeFragment0 = new ClassifyFragment();
                    transaction.add(R.id.center_layout, mHomeFragment0, "0");
                } else {
                    transaction.show(mHomeFragment0);
                }
                setFragmentVerisiable(mHomeFragment0, 0);
                curFragment = mHomeFragment0;
                break;

            case HOME_TAB_INDEX_1:
                hideFragments(transaction);
                if (null == mHomeFragment1) {
                    mHomeFragment1 = new FindFragment();
                    transaction.add(R.id.center_layout, mHomeFragment1, "1");
                } else {
                    transaction.show(mHomeFragment1);
                }
                setFragmentVerisiable(mHomeFragment1, 1);
                curFragment = mHomeFragment1;
                break;

            case HOME_TAB_INDEX_2:
                hideFragments(transaction);
                if (null == mHomeFragment2) {
                    mHomeFragment2 = new MessageFragment();
                    transaction.add(R.id.center_layout, mHomeFragment2, "2");
                } else {
                    transaction.show(mHomeFragment2);
                }
                setFragmentVerisiable(mHomeFragment2, 2);
                curFragment = mHomeFragment2;
                break;
            case HOME_TAB_INDEX_3:
                hideFragments(transaction);
                if (null == mHomeFragment3) {
                    mHomeFragment3 = new IntegralFragment();
                    transaction.add(R.id.center_layout, mHomeFragment3, "3");
                } else {
                    transaction.show(mHomeFragment3);
                }
                setFragmentVerisiable(mHomeFragment3, 3);
                curFragment = mHomeFragment3;
                break;

            default:
                break;
        }
        transaction.commitAllowingStateLoss();
        changeTabMenu(index);
    }

    /**
     * 更改底部菜单背景
     *
     * @param index
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void changeTabMenu(int index) {
        switch (index) {
            case HOME_TAB_INDEX_0:
                tab0.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab0_pressed, null, null);
                tab1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab1_normal, null, null);
                tab3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab3_normal, null, null);
                tab4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab4_normal, null, null);
                break;
            case HOME_TAB_INDEX_1:
                tab0.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab0_normal, null, null);
                tab1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab1_pressed, null, null);
                tab3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab3_normal, null, null);
                tab4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab4_normal, null, null);
                break;
            case HOME_TAB_INDEX_2:
                tab0.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab0_normal, null, null);
                tab1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab1_normal, null, null);
                tab3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab3_pressed, null, null);
                tab4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab4_normal, null, null);
                break;
            case HOME_TAB_INDEX_3:
                tab0.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab0_normal, null, null);
                tab1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab1_normal, null, null);
                tab3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab3_normal, null, null);
                tab4.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable_tab4_pressed, null, null);
                break;
            default:
                break;
        }


    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != mHomeFragment0) {
            transaction.hide(mHomeFragment0);
        }
        if (null != mHomeFragment1) {
            transaction.hide(mHomeFragment1);
        }
        if (null != mHomeFragment2) {
            transaction.hide(mHomeFragment2);
        }
        if (null != mHomeFragment3) {
            transaction.hide(mHomeFragment3);
        }
    }

    public void setFragmentVerisiable(final BaseFragment fagment, final int index) {
        if (mHandler == null) {
            mHandler = new Handler() {
            };
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (fagment != null) {
//                    fagment.setVisible(true);
//                }

                switch (index) {
                    case 0:
                        if (mHomeFragment0 != null) {
                            mHomeFragment0.setVisible(true);
                        }
                        if (mHomeFragment1 != null) {
                            mHomeFragment1.setVisible(false);
                        }
                        if (mHomeFragment2 != null) {
                            mHomeFragment2.setVisible(false);
                        }
                        if (mHomeFragment3 != null) {
                            mHomeFragment3.setVisible(false);
                        }
                        break;

                    case 1:
                        if (mHomeFragment0 != null) {
                            mHomeFragment0.setVisible(false);
                        }
                        if (mHomeFragment1 != null) {
                            mHomeFragment1.setVisible(true);
                        }
                        if (mHomeFragment2 != null) {
                            mHomeFragment2.setVisible(false);
                        }
                        if (mHomeFragment3 != null) {
                            mHomeFragment3.setVisible(false);
                        }
                        break;

                    case 2:
                        if (mHomeFragment0 != null) {
                            mHomeFragment0.setVisible(false);
                        }
                        if (mHomeFragment1 != null) {
                            mHomeFragment1.setVisible(false);
                        }
                        if (mHomeFragment2 != null) {
                            mHomeFragment2.setVisible(true);
                        }
                        if (mHomeFragment3 != null) {
                            mHomeFragment3.setVisible(false);
                        }
                        break;

                    case 3:
                        if (mHomeFragment0 != null) {
                            mHomeFragment0.setVisible(false);
                        }
                        if (mHomeFragment1 != null) {
                            mHomeFragment1.setVisible(false);
                        }
                        if (mHomeFragment2 != null) {
                            mHomeFragment2.setVisible(false);
                        }
                        if (mHomeFragment3 != null) {
                            mHomeFragment3.setVisible(true);
                        }
                        break;
                }
            }
        }, 400);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            String tag = (String) v.getTag();
            if (tag.equals("tab0")) {
                onTabSelected(0);
            } else if (tag.equals("tab1")) {
                onTabSelected(1);
            } else if (tag.equals("tab2")) {//发布
                Intent mIntent = new Intent(mContext, PublishActivity.class);
                startActivity(mIntent);
//                Intent mIntent=new Intent(mContext,DynamicPost.class);
//                startActivity(mIntent);

            } else if (tag.equals("tab3")) {
                onTabSelected(2);
            } else if (tag.equals("tab4")) {
                onTabSelected(3);
            }
        }
        Intent intents;
        switch (v.getId()) {
            case R.id.im_menu_icon://个人资料
                intents = new Intent(MainActivity.this, PersonalActivity.class);
                startActivity(intents);
                break;
            default:
                break;
        }
    }

    float moveXY;
    float lastX = 0.0f;
    float lastY = 0.0f;
    boolean isNeedUp;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isNeedUp = true;
                lastX = event.getX();
                lastY = event.getY();
                leftMenuTouch(v, true);
                break;

            case MotionEvent.ACTION_MOVE:
                float cx = event.getX();
                float cy = event.getY();
                moveXY = Math.abs(cx - lastX) + Math.abs(cy - lastY);
                LogUtil.d("moveXY:" + moveXY);
                if (moveXY > 30) {
                    leftMenuTouch(v, false);
                    isNeedUp = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isNeedUp) {
                    leftMenuTouch(v, false);
                }
                break;
        }
        return false;
    }

    public void leftMenuTouch(View v, boolean isPressed) {
        if (isPressed) {
            switch (v.getId()) {
                case R.id.ly_my_collect:
                    ((ImageView) (v.findViewById(R.id.im_my_collect))).setImageResource(R.drawable.menu_im_my_collect);
                    ((TextView) (v.findViewById(R.id.tv_my_collect))).setTextColor(getResources().getColor(R.color.menu_pass_text_bg));
                    break;
                case R.id.ly_my_xianyi:
                    ((ImageView) (v.findViewById(R.id.im_my_xianyi))).setImageResource(R.drawable.menu_im_xianyi);
                    ((TextView) (v.findViewById(R.id.tv_my_xianyi))).setTextColor(getResources().getColor(R.color.menu_pass_text_bg));
                    break;
                case R.id.ly_trade:
                    ((ImageView) (v.findViewById(R.id.im_trade))).setImageResource(R.drawable.menu_im_trade);
                    ((TextView) (v.findViewById(R.id.tv_trade))).setTextColor(getResources().getColor(R.color.menu_pass_text_bg));
                    break;
                case R.id.ly_browse_history:
                    ((ImageView) (v.findViewById(R.id.im_browse_history))).setImageResource(R.drawable.menu_im_browse_history);
                    ((TextView) (v.findViewById(R.id.tv_browse_history))).setTextColor(getResources().getColor(R.color.menu_pass_text_bg));
                    break;
                case R.id.ly_shop:
                    ((ImageView) (v.findViewById(R.id.im_shop))).setImageResource(R.drawable.menu_im_coins_shop);
                    ((TextView) (v.findViewById(R.id.tv_shop))).setTextColor(getResources().getColor(R.color.menu_pass_text_bg));
                    break;
                case R.id.ly_account_management:
                    ((ImageView) (v.findViewById(R.id.im_account_management))).setImageResource(R.drawable.menu_im_account_management);
                    ((TextView) (v.findViewById(R.id.tv_account_management))).setTextColor(getResources().getColor(R.color.menu_pass_text_bg));
                    break;
                case R.id.ly_setting:
                    ((ImageView) (v.findViewById(R.id.im_setting))).setImageResource(R.drawable.menu_im_setting);
                    ((TextView) (v.findViewById(R.id.tv_setting))).setTextColor(getResources().getColor(R.color.menu_pass_text_bg));
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.ly_my_collect:
                    ((ImageView) (v.findViewById(R.id.im_my_collect))).setImageResource(R.drawable.menu_im_my_collect_normal);
                    ((TextView) (v.findViewById(R.id.tv_my_collect))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.ly_my_xianyi:
                    ((ImageView) (v.findViewById(R.id.im_my_xianyi))).setImageResource(R.drawable.menu_im_xianyi_normal);
                    ((TextView) (v.findViewById(R.id.tv_my_xianyi))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.ly_trade:
                    ((ImageView) (v.findViewById(R.id.im_trade))).setImageResource(R.drawable.menu_im_trade_normal);
                    ((TextView) (v.findViewById(R.id.tv_trade))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.ly_browse_history:
                    ((ImageView) (v.findViewById(R.id.im_browse_history))).setImageResource(R.drawable.menu_im_browse_history_normal);
                    ((TextView) (v.findViewById(R.id.tv_browse_history))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.ly_shop:
                    ((ImageView) (v.findViewById(R.id.im_shop))).setImageResource(R.drawable.menu_im_coins_shop_normal);
                    ((TextView) (v.findViewById(R.id.tv_shop))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.ly_account_management:
                    ((ImageView) (v.findViewById(R.id.im_account_management))).setImageResource(R.drawable.menu_im_account_management_normal);
                    ((TextView) (v.findViewById(R.id.tv_account_management))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.ly_setting:
                    ((ImageView) (v.findViewById(R.id.im_setting))).setImageResource(R.drawable.menu_im_setting_normal);
                    ((TextView) (v.findViewById(R.id.tv_setting))).setTextColor(getResources().getColor(R.color.white));
                    break;
            }
            leftMenuClick(v);
        }

    }

    public void leftMenuClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ly_my_collect://我的收藏
                intent = new Intent(MainActivity.this, MyCollectActivity.class);
                startActivity(intent);
                break;

            case R.id.ly_my_xianyi://我的闲置
                intent = new Intent(MainActivity.this, MyXianZhiActivity.class);
                startActivity(intent);
                break;

            case R.id.ly_trade://交易管理
                intent = new Intent(MainActivity.this, MyTradeActivity.class);
                startActivity(intent);
                break;

            case R.id.ly_browse_history://浏览历史
                intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                break;

            case R.id.ly_shop://积分商城
                mMenu.toggle();
                onTabSelected(HOME_TAB_INDEX_3);
                break;

            case R.id.ly_account_management://账号管理
                mMenu.toggle();
                intent = new Intent(MainActivity.this, SetPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.ly_setting://设置
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;

            default:
                break;

        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMenu != null) {
                    mMenu.closeMenu();
                }
            }
        },1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("main--onDestroy");
        if (curFragment != null) {
            curFragment.setVisible(false);
        }
    }
}
