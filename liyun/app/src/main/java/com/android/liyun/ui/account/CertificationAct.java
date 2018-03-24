package com.android.liyun.ui.account;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.bean.LoginBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.http.RequestWhatI;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.liyun.http.RequestWhatI.IDENTIFICATION;
import static com.android.liyun.http.RequestWhatI.LOGIN;

public class CertificationAct extends BaseActivity implements TextWatcher {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_realname)
    EditText txtRealname;
    @BindView(R.id.txt_idcard)
    EditText txtIdcard;
    @BindView(R.id.txt_email)
    EditText txtEmail;
    @BindView(R.id.txt_phone)
    EditText txtPhone;
    @BindView(R.id.txt_confirm_submit)
    TextView txtConfirmSubmit;
    private String token;
    private String uid;

    @Override
    public int getLayoutId() {
        return R.layout.act_certification;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("实名认证");
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
        txtRealname.addTextChangedListener(this);
        txtIdcard.addTextChangedListener(this);
        txtEmail.addTextChangedListener(this);
        txtPhone.addTextChangedListener(this);
        token = SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, "");
        uid = SPUtil.getString(UIUtils.getContext(), ConstValues.UID, "");
        mApi = new Api(handler, this);
    }

    @OnClick(R.id.txt_confirm_submit)
    public void onClick() {

        String realname = txtRealname.getText().toString();
        String idcard = txtIdcard.getText().toString();
        String email = txtEmail.getText().toString();
        String phone = txtPhone.getText().toString();
        mApi.identiFication(IDENTIFICATION, uid, token, phone, realname, email, idcard);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (txtRealname.getText().toString().length() > 0 && txtIdcard.getText().toString().length() > 0 && txtEmail.getText().toString().length() > 0 && txtPhone.getText().toString().length() > 0) {
            txtConfirmSubmit.setEnabled(true);
        } else {
            txtConfirmSubmit.setEnabled(false);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != -1) {
                switch (msg.what) {
                    case IDENTIFICATION:
                        BaseBen baseBen = mGson.fromJson(msg.obj.toString(), BaseBen.class);
                        if (baseBen.getStatus().equals(ConstValues.ZERO)) {
                            UIUtils.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    CertificationAct.this.finish();
                                }
                            }, 1500);
                        }
                        Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };
}
