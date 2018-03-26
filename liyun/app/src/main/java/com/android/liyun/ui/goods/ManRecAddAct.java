package com.android.liyun.ui.goods;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.adapter.SitesAdapter;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.bean.SiteBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.http.RequestWhatI;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;

public class ManRecAddAct extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.listview)
    ListView swipeTarget;
    private SiteBean siteBean;
    private SitesAdapter sitesAdapter;
    private int position;
    private String token;
    private String uid;
    private BaseBen baseBean;

    @Override
    public int getLayoutId() {
        return R.layout.act_add_management;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void initView() {
        mApi = new Api(handler, this);
        token = SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, "");
        uid = SPUtil.getString(UIUtils.getContext(), ConstValues.UID, "");
        swipeTarget.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("地址管理");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApi.addManagement(RequestWhatI.ADDMAN, SPUtil.getString(UIUtils.getContext(), ConstValues.UID, ""),
                SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.toolbar_add:
                startActivity(AddAddressAct.class);
                break;
            default:
                break;
        }
        return true;

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != -1) {
                switch (msg.what) {
                    case RequestWhatI.ADDMAN:
                        siteBean = new Gson().fromJson(msg.obj.toString(), SiteBean.class);
                        if (siteBean.getStatus().equals(ConstValues.ZERO) && siteBean.getAddress().size() > 0) {
                            sitesAdapter = new SitesAdapter(UIUtils.getContext(), siteBean.getAddress(), R.layout.item_add_list, new SitesAdapter.CallBack() {
                                @Override
                                public void click(View view) {
                                    switch (view.getId()) {
                                        case R.id.txt_del_add://删除收货地址
                                            position = (int) view.getTag();
                                            mApi.delAddress(RequestWhatI.DELADD, uid, token, siteBean.getAddress().get(position).getAddressid());
                                            Toast.makeText(UIUtils.getContext(), position + "", Toast.LENGTH_SHORT).show();
                                            break;
                                        case R.id.txt_edit://编辑收货地址
                                            position = (int) view.getTag();

                                            Intent intent = new Intent();
                                            startActivity(ModAddAct.class);
                                            Toast.makeText(UIUtils.getContext(), position + "", Toast.LENGTH_SHORT).show();
                                            break;
                                        case R.id.cb_default_address:
                                            position = (int) view.getTag();
                                            mApi.setDefault(RequestWhatI.SETDEFAULT, uid, token, siteBean.getAddress().get(position).getAddressid(), "1");
                                            Toast.makeText(UIUtils.getContext(), position + "", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            });
                            swipeTarget.setAdapter(sitesAdapter);
                        }
                        break;


                    case RequestWhatI.DELADD:
                        baseBean = new Gson().fromJson(msg.obj.toString(), BaseBen.class);
                        Toast.makeText(UIUtils.getContext(), baseBean.getMsg(), Toast.LENGTH_SHORT).show();
                        if (baseBean.getStatus().equals(ConstValues.ZERO)) {
                            sitesAdapter.getmList().remove(position);
                            sitesAdapter.notifyDataSetChanged();
                        }
                        break;

                    case RequestWhatI.SETDEFAULT:
                        baseBean = new Gson().fromJson(msg.obj.toString(), BaseBen.class);
                        Toast.makeText(UIUtils.getContext(), baseBean.getMsg(), Toast.LENGTH_SHORT).show();
                        if (baseBean.getStatus().equals(ConstValues.ZERO)) {
                            List<SiteBean.AddressBean> receivingAddressListBeans = sitesAdapter.getmList();
                            for (SiteBean.AddressBean receivingAddressListBean : receivingAddressListBeans) {
                                receivingAddressListBean.setIsdefault(ConstValues.ZERO);
                            }
                            receivingAddressListBeans.get(position).setIsdefault(ConstValues.ONE);
                            sitesAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        }
    };
}
