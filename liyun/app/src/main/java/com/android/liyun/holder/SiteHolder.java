package com.android.liyun.holder;

import android.view.View;
import android.widget.TextView;


import com.android.liyun.R;
import com.android.liyun.bean.SiteBean;
import com.android.liyun.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunwubin on 2017/11/1.
 */

public class SiteHolder extends BaseHolder<SiteBean.AddressBean> {
    @BindView(R.id.txt_consignee)
    TextView txtConsignee;
    @BindView(R.id.txt_telephone)
    TextView txtTelephone;
    @BindView(R.id.txt_deliveryAddress)
    TextView txtDeliveryAddress;
    @BindView(R.id.txt_del_add)
    TextView txtDelAdd;
    @BindView(R.id.txt_edit)
    TextView txtEdit;

    @Override
    protected View initView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_add_list, null);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    protected void refreshUI(SiteBean.AddressBean data) {
        String consignee = data.getName();
        String telephone = data.getTelephone();
        String deliveryAddress = data.getAddress();
        txtConsignee.setText(consignee);
        txtTelephone.setText(telephone);
        txtDeliveryAddress.setText(deliveryAddress);
    }

    public TextView getTxtDelAdd() {
        return txtDelAdd;
    }

    public TextView getTxtEdit() {
        return txtEdit;
    }

}
