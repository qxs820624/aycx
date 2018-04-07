package com.android.liyun.ui.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.bean.LoginBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;

import static com.android.liyun.http.RequestWhatI.LOGIN;

public class LoginAct extends BaseActivity implements TextWatcher {
    @BindView(R.id.txt_forget_password)
    TextView txtForgetPassword;
    @BindView(R.id.txt_regist)
    TextView txtRegist;
    @BindView(R.id.txt_account)
    EditText txtAccount;
    @BindView(R.id.txt_pwd)
    EditText txtPwd;
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.iv_wx)
    ImageView ivWx;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    private IWXAPI api;
    @Override
    public int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mApi = new Api(handler, this);
        txtAccount.addTextChangedListener(this);
        txtPwd.addTextChangedListener(this);
        api = WXAPIFactory.createWXAPI(this, ConstValues.APP_ID_WX,true);
        //将应用的appid注册到微信
        api.registerApp(ConstValues.APP_ID_WX);
    }

    @OnClick({R.id.txt_forget_password, R.id.txt_regist, R.id.txt_login, R.id.iv_wx, R.id.iv_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_forget_password:
                startActivity(ForgetAct.class);
                break;
            case R.id.txt_regist:
                startActivity(RegistAct.class);
                break;
            case R.id.txt_login:
                String phone = txtAccount.getText().toString();
                String pwd = txtPwd.getText().toString();
                mApi.login(LOGIN, "User", "login", phone, pwd);
                break;
            case R.id.iv_wx:
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
                req.state = "wechat_sdk_微信登录";
                api.sendReq(req);
                break;
            case R.id.iv_qq:

                PayReq request = new PayReq();

                request.appId = ConstValues.APP_ID_WX;

                request.partnerId = ConstValues.WX_PARTNER_ID;

                request.prepayId= "1101000000140415649af9fc314aa427";

                request.packageValue = "Sign=WXPay";

                request.nonceStr= "1101000000140429eb40476f8896f4c9";

                request.timeStamp= "1398746574";

                request.sign= ConstValues.WX_PRIVATE_KEY;

                api.sendReq(request);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0){
            String headUrl = data.getStringExtra("headUrl");
//            ViseLog.d("url:"+headUrl);
//            Glide.with(WXLoginActivity.this).load(headUrl).into(ivHead);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {


        if (txtAccount.getText().toString().length() > 0 && txtPwd.getText().toString().length() > 0) {
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
                    case LOGIN:
                        LoginBean baseBen = mGson.fromJson(msg.obj.toString(), LoginBean.class);
                        if (baseBen.getStatus().equals(ConstValues.ZERO)) {
                            SPUtil.putString(UIUtils.getContext(), ConstValues.TOKEN, baseBen.getToken());
                            SPUtil.putString(UIUtils.getContext(), ConstValues.UID, baseBen.getUid());

                            UIUtils.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    LoginAct.this.finish();
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
