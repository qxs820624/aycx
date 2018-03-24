package com.android.liyun.ui.goods;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sunwubin on 2017/11/2.
 * 修改地址
 */
public class ModAddAct extends BaseActivity implements TextWatcher {
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_consignee)
    EditText txtConsignee;
    @BindView(R.id.txt_telephone)
    EditText txtTelephone;
    @BindView(R.id.rlyt_select_pro)
    RelativeLayout rlytSelectPro;
    @BindView(R.id.txt_deliveryAddress)
    EditText txtDeliveryAddress;
    @BindView(R.id.txt_affirm)
    TextView txtAffirm;
    @BindView(R.id.txt_des_area)
    TextView txtArea;
    private String province;
    private String provinceId;
    private String city;
    private String cityId;
    private String county;
    private String countyId;
    private String id;
    /**
     * 是否选择修改地址
     */
    private boolean isSelPro;
    private String province_;
    private String provinceId_;
    private String city_;
    private String cityId_;
    private String county_;
    private String countyId_;

    @Override
    public int getLayoutId() {
        return R.layout.act_add_address;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void initView() {
        txtConsignee.addTextChangedListener(this);
        txtTelephone.addTextChangedListener(this);
        txtDeliveryAddress.addTextChangedListener(this);
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("修改地址");
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

    @OnClick({R.id.rlyt_select_pro, R.id.txt_affirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlyt_select_pro:
                Intent intent = new Intent();
                intent.putExtra("Modify", "Modify");

                startActivityForResult(intent, 1000);
                isSelPro = true;
                break;
            case R.id.txt_affirm:
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
        if (txtConsignee.getText().toString().length() > 0 && txtTelephone.getText().toString().length() > 0 && txtDeliveryAddress.getText().toString().length() > 0
                ) {
            txtAffirm.setEnabled(true);
        } else {
            txtAffirm.setEnabled(false);

        }
    }
}
