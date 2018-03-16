package com.android.liyun.ui.login;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.android.liyun.http.RequestWhatI.REGIST;
import static com.android.liyun.http.RequestWhatI.SENDCODE;

public class RegistAct extends BaseActivity implements TextWatcher {

    @BindView(R.id.txt_account)
    EditText txtAccount;//账号
    @BindView(R.id.txt_code)
    EditText txtCode;
    @BindView(R.id.txt_sentCode)
    TextView txtSentCode;
    @BindView(R.id.txt_pwd)
    EditText txtPwd;
    @BindView(R.id.txt_re_pwd)
    EditText txtRePwd;
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.cb_select)
    CheckBox cbSelect;
    private String phone;
    private String code;
    private String pwd;
    private String repwd;
    private BaseBen baseBen;

    @Override
    public int getLayoutId() {
        return R.layout.act_regist;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(R.string.title_activity_regist);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        txtAccount.addTextChangedListener(this);
        txtCode.addTextChangedListener(this);
        txtPwd.addTextChangedListener(this);
        txtRePwd.addTextChangedListener(this);
        mApi = new Api(handler, this);
    }

    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            if (null != txtCode) {
                txtSentCode.setText(millisUntilFinished / 1000 + getString(R.string.get_ag));
                txtSentCode.setEnabled(false);
            }
        }

        @Override
        public void onFinish() {
            if (null != txtCode) {
                txtSentCode.setEnabled(true);
                txtSentCode.setText(getString(R.string.action_get_code));
                txtSentCode.setTextColor(Color.parseColor("#ff721f"));
            }
        }
    };

    @OnClick({R.id.txt_sentCode, R.id.txt_login, R.id.cb_select})
    public void onClick(View view) {
        switch (view.getId()) {
            /**
             * 发送验证码
             */
            case R.id.txt_sentCode:
                phone = txtAccount.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    mApi.sentCode(SENDCODE, "sms", "send", phone, "1");
                } else {
                    Toast.makeText(UIUtils.getContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             * 登录
             */
            case R.id.txt_login:
                phone = txtAccount.getText().toString();
                code = txtCode.getText().toString();
                pwd = txtPwd.getText().toString();
                repwd = txtRePwd.getText().toString();
                if (pwd.equals(repwd)) {
                    mApi.userRegist(REGIST, "User", "register", phone, code, pwd);
                } else {
                    Toast.makeText(UIUtils.getContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                }

                break;
            /**
             * 选择
             */
            case R.id.cb_select:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


    @Override
    public void afterTextChanged(Editable editable) {
        //发送验证码是否可用
        if (txtAccount.getText().toString().length() > 0 && txtCode.getText().toString().length() > 0
                && txtPwd.getText().toString().trim().length() > 0 && txtRePwd.getText().toString().trim().length() > 0
                ) {
            txtLogin.setEnabled(true);
        } else {
            txtLogin.setEnabled(false);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != -1) {
                switch (msg.what) {
                    case SENDCODE:
                        baseBen = mGson.fromJson(msg.obj.toString(), BaseBen.class);
                        if (baseBen.getStatus().equals(ConstValues.ZERO)) {
                            Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        String s = msg.obj.toString();
                        timer.start();
                        System.out.println(s);
                        break;
                    case REGIST:
                        baseBen = mGson.fromJson(msg.obj.toString(), BaseBen.class);
                        Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };
}
