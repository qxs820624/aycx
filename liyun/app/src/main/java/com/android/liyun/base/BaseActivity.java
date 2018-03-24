package com.android.liyun.base;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.Window;

import com.android.liyun.R;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.StatusBarUtil;
import com.android.liyun.utils.UIUtils;
import com.android.liyun.widget.LoadingDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.CookieStore;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends AppCompatActivity {
    public T mPresenter;
    public E mModel;
    public Context mContext;
    protected Toolbar toolbar;
    protected Map<String, String> parameterMap;
    protected Call call;
    protected Api mApi;
    protected Gson mGson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mContext = this;
        parameterMap = new HashMap<>();
        this.initPresenter();
        this.initView();
        mGson = new Gson();
        Log.i("BaseActivity", getClass().getSimpleName());
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // 默认不显示原生标题
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

    }

    /**
     * 设置layout前配置
     */
    private void doBeforeSetcontentView() {
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 默认着色状态栏
        //SetStatusBarColor();
        setStatusBar();

    }

    /*********************子类实现*****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //初始化view
    public abstract void initView();


    private CompositeDisposable compositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void dispose() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ProgressDialog dialog;

    public void showLoading() {


//        View view=(LinearLayout) getLayoutInflater().inflate(R.layout.custom_progress_layout,null);
//        AlertDialog.Builder builder =new AlertDialog.Builder(this);
//        builder.setView(view);                                           //这里添加上这个view
//        builder.create().show();
        LoadingDialog.showDialogForLoading(this, "加载中...", true);


//        if (dialog != null && dialog.isShowing()) return;
//        dialog = new ProgressDialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage("请求网络中...");
//        dialog.show();
    }

    public void dismissLoading() {
        LoadingDialog.cancelDialogForLoading();
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
    }

    protected boolean isLogin() {
        String token = SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, "");
        String uid = SPUtil.getString(UIUtils.getContext(), ConstValues.UID, "");
        if (TextUtils.isEmpty(token) && TextUtils.isEmpty(uid)) {
            return false;
        } else {
            return true;
        }

    }


}
