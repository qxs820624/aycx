package com.android.liyun.ui.goods;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.adapter.RecyclerAdapter;
import com.android.liyun.base.AddBean;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.bean.CommendBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.http.RequestWhatI;
import com.android.liyun.utils.AddressPickTask;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;

import static com.android.liyun.http.RequestWhatI.ADDRESS;
import static com.android.liyun.http.RequestWhatI.TUIJIAN;


/**
 * 添加地址
 */
public class AddAddressAct extends BaseActivity implements TextWatcher {

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
    private AddBean addBean;


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
        addBean = new AddBean();
        addBean.setAction("add");
        addBean.setModule("Address");
        addBean.setUid(SPUtil.getString(UIUtils.getContext(), ConstValues.UID, ""));
        addBean.setToken(SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, ""));
        mApi = new Api(handler, UIUtils.getContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("添加地址");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AddAddressAct.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.rlyt_select_pro, R.id.txt_affirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlyt_select_pro:
                onAddressPicker();
                break;
            case R.id.txt_affirm:
                addBean.setName(txtConsignee.getText().toString().trim());
                addBean.setTelephone(txtTelephone.getText().toString().trim());
                addBean.setAddress(txtDeliveryAddress.getText().toString().trim());
                mApi.addAddress(RequestWhatI.ADDRESS, addBean);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void onAddressPicker() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
//                showToast("数据初始化失败");
                Toast.makeText(UIUtils.getContext(), "数据初始化失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    addBean.setProvince(province.getAreaId());
                    addBean.setCity(city.getAreaId());
                    Toast.makeText(UIUtils.getContext(), province.getAreaName(), Toast.LENGTH_SHORT).show();
                } else {
                    addBean.setProvince(province.getAreaId());
                    addBean.setCity(city.getAreaId());
                    addBean.setCountry(county.getAreaId());
                }
            }
        });
        task.execute("贵州", "毕节", "纳雍");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != -1) {
                switch (msg.what) {
                    case ADDRESS:
                        BaseBen baseBen = mGson.fromJson(msg.obj.toString(), BaseBen.class);
                        if (baseBen.getStatus().equals(ConstValues.ZERO)) {
                            UIUtils.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AddAddressAct.this.finish();
                                }
                            }, 1500);
                            Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();

                        }

                        break;
                }
            }
        }
    };
}
