package com.android.liyun.ui.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.base.BaseFragment;
import com.android.liyun.bean.UserCenterBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.ui.account.AllOrderListAct;
import com.android.liyun.ui.account.CartListAct;
import com.android.liyun.ui.account.CertificationAct;
import com.android.liyun.ui.account.FavoritesListAct;
import com.android.liyun.ui.account.SetAct;
import com.android.liyun.ui.manager.DeviceListAct;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.android.liyun.http.RequestWhatI.USERCENTER;

public class MyFrag extends BaseFragment {
    @BindView(R.id.llyt_all_orders)
    RelativeLayout llytAllOrders;
    @BindView(R.id.rlyt_dri_score)
    RelativeLayout rlytDriScore;
    @BindView(R.id.rlyt_my_rank)
    RelativeLayout rlytMyRank;
    @BindView(R.id.rlyt_his_rec)
    RelativeLayout rlytHisRec;
    @BindView(R.id.rlyt_anth)
    RelativeLayout rlytAnth;
    @BindView(R.id.rlyt_my_favorite)
    RelativeLayout rlytMyFavorite;
    @BindView(R.id.txt_gold)
    TextView txtGold;
    @BindView(R.id.txt_masonry)
    TextView txtMasonry;
    @BindView(R.id.txt_silver)
    TextView txtSilver;
    @BindView(R.id.txt_jifen)
    TextView txtJifen;

    @BindView(R.id.txt_nickname)
    TextView txt_nickname;
    private String uid;
    private String token;

    @Override
    protected int getLayoutResource() {
        return R.layout.fra_my;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mApi = new Api(handler, UIUtils.getContext());
        uid = SPUtil.getString(UIUtils.getContext(), ConstValues.UID, "");
        token = SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, "");
    }

    @Override
    public void onResume() {
        super.onResume();
        mApi.userCenter(USERCENTER, uid, token);
    }

    @OnClick({R.id.llyt_all_orders, R.id.rlyt_shop_cart, R.id.rlyt_dri_score, R.id.rlyt_my_rank, R.id.rlyt_his_rec, R.id.rlyt_anth,
            R.id.rlyt_my_favorite,
            R.id.rlyt_set,
            R.id.rlyt_my_device_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyt_all_orders:
                startActivity(AllOrderListAct.class);
                break;
            case R.id.rlyt_shop_cart:
                startActivity(CartListAct.class);
            case R.id.rlyt_dri_score:
                break;
            case R.id.rlyt_my_rank:
                break;
            case R.id.rlyt_his_rec:
                break;
            case R.id.rlyt_anth:
                startActivity(CertificationAct.class);
                break;
            case R.id.rlyt_my_favorite:
                startActivity(FavoritesListAct.class);
                break;
            case R.id.rlyt_my_device_list:
                startActivity(DeviceListAct.class);
                break;
            case R.id.rlyt_set:
                startActivity(SetAct.class);
                break;
        }
    }

    /**
     * 初始化个人中心数据
     *
     * @param s
     */
    private void initUserCenter(String s) {
        UserCenterBean userCenterBean = mGson.fromJson(s, UserCenterBean.class);
        if (userCenterBean.getStatus().equals(ConstValues.ZERO)) {
            String gold = userCenterBean.getGold();
            String jifen = userCenterBean.getJifen();
            String silver = userCenterBean.getSilver();
            String masonry = userCenterBean.getMasonry();
            String nickname = userCenterBean.getNickname();
            txtGold.setText(gold);
            txtJifen.setText(jifen);
            txtSilver.setText(silver);
            txtMasonry.setText(masonry);
            txt_nickname.setText(nickname);
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != -1) {
                switch (msg.what) {
                    case USERCENTER:
                        initUserCenter(msg.obj.toString());
                        break;
                }
            }
        }
    };
}
