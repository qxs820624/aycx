package com.android.liyun.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetAct extends BaseActivity implements TextWatcher {
    @BindView(R.id.txt_account)
    EditText txtAccount;
    @BindView(R.id.txt_code)
    EditText txtCode;
    @BindView(R.id.txt_sentCode)
    TextView txtSentCode;
    @BindView(R.id.txt_change_pwd)
    TextView txtChangePwd;

    @Override
    public int getLayoutId() {
        return R.layout.act_forget;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        txtAccount.addTextChangedListener(this);


    }
    @OnClick({R.id.txt_sentCode, R.id.txt_change_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_sentCode:
                break;
            case R.id.txt_change_pwd:
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

    }
}
