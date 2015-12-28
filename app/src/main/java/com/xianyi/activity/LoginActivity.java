package com.xianyi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xianyi.R;
import com.xianyi.utils.Utils;

/**
 * ${todo}<登录页面>
 *
 * @author lht
 * @data: on 15/11/2 16:35
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private static final String LTAG = LoginActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 密码登录 **/
    private TextView mTVstat1;
    /** 动态密码登录 **/
    private TextView mTVstat2;
    /** 获取 **/
    private TextView mTVcode;
    /** 手机 **/
    private EditText mETPhone;
    /** 验证码 **/
    private EditText mETCode;
    /** 登录 **/
    private Button mBTLogin;
    /** 手机 **/
    private String mPhone;
    /** 验证码 **/
    private String mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

        initViews();
    }

    public void initViews() {
        mTVstat1 = (TextView) findViewById(R.id.tv_state1);
        mTVstat2 = (TextView) findViewById(R.id.tv_state2);
        mTVcode = (TextView) findViewById(R.id.tv_get_verify_code_again_resetpw);
        mETPhone = (EditText) findViewById(R.id.et_phone);
        mETCode = (EditText) findViewById(R.id.et_code);
        mBTLogin = (Button) findViewById(R.id.btn_login);

        mTVstat1.setOnClickListener(this);
        mTVstat2.setOnClickListener(this);
        mTVcode.setOnClickListener(this);
        mBTLogin.setOnClickListener(this);

        // 设置登录按钮
        setNextBtnState(true);

        showView();
    }

    /**
     * 显示数据
     */
    private void showView() {
//        // 设置手机号
//        mETPhone.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                // TODO Auto-generated method stub
//                String data = getData(mETPhone);
//                if (!Utils.isEmpty(data)) {
//                    mPhone = data;
//                }
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//                                      int arg3) {
//
//                String phone = getData(mETPhone);
//                String code = getData(mETCode);
//
//                if (!Utils.isEmpty(phone) && phone.length() > 10
//                        && !Utils.isEmpty(code) && code.length() > 5) {
//                    setNextBtnState(true);
//                } else {
//                    setNextBtnState(false);
//                }
//
//            }
//        });
//
//        // 设置手机号
//        mETCode.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                // TODO Auto-generated method stub
//                String data = getData(mETCode);
//                if (!Utils.isEmpty(data)) {
//                    mCode = data;
//                }
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//                                      int arg3) {
//
//                String phone = getData(mETPhone);
//                String code = getData(mETCode);
//
//                if (!Utils.isEmpty(phone) && phone.length() > 10
//                        && !Utils.isEmpty(code) && code.length() > 5) {
//                    setNextBtnState(true);
//                } else {
//                    setNextBtnState(false);
//                }
//
//            }
//        });
    }

    /**
     * 顶部布局--注册按钮事件监听
     */
    public class TitleRightOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
//            // 注册
//            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//            startActivity(intent);
//            finish();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            // 密码登录
            case R.id.tv_state1:
                mTVstat1.setBackground(getResources().getDrawable(R.drawable.login_state_1));
                mTVstat2.setBackground(getResources().getDrawable(R.drawable.login_state_2));
                mTVcode.setVisibility(View.GONE);
                break;

            // 动态密码登录
            case R.id.tv_state2:
                mTVstat1.setBackground(getResources().getDrawable(R.drawable.login_state_2));
                mTVstat2.setBackground(getResources().getDrawable(R.drawable.login_state_1));
                mTVcode.setVisibility(View.VISIBLE);
                break;

            // 获取
            case R.id.tv_get_verify_code_again_resetpw:

                break;

            // 登录
            case R.id.btn_login:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * 去除EditText的空格
     *
     * @param et
     * @return
     */
    public String getData(EditText et) {
        String s = et.getText().toString();
        return s.replaceAll(" ", "");
    }

    /**
     * 设置下一步按钮
     *
     * @param flag
     */
    private void setNextBtnState(boolean flag) {
        if (mBTLogin == null)
            return;
        mBTLogin.setEnabled(flag);
        mBTLogin.setClickable(flag);
    }
}
