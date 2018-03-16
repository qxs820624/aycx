package com.android.liyun.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.liyun.R;
import com.android.liyun.utils.LoadingPage;
import com.android.liyun.utils.NetWorkUtils;
import com.android.liyun.utils.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

public abstract class BaseFragment extends Fragment {

    protected Map<String, String> parameterMap;
    protected Call call;
    protected LayoutInflater inflater;
    protected Unbinder unbinder;
    private ProgressDialog dialog;
    private LoadingPage mLoadingPage;
    private CompositeDisposable compositeDisposable;
    //是否可见
//    protected boolean isVisble;
//    // 标志位，标志Fragment已经初始化完成。
//    public boolean isPrepared = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parameterMap = new HashMap<>();
        this.inflater = inflater;
        mLoadingPage = new LoadingPage(UIUtils.getContext()) {

            @Override
            public int getLayoutId() {
                return getEmpLayoutId();
            }

            @Override
            public View onCreateSuccessView() {
                return BaseFragment.this.onCreateSuccessView();
            }

            @Override
            public void onLoad() {
                BaseFragment.this.onLoad();
            }

        };
        return mLoadingPage;
    }

    protected int getEmpLayoutId() {
        return R.layout.layout_empty;
    }


    // 由子类实现创建布局的方法
    public abstract View onCreateSuccessView();

    // 由子类实现加载网络数据的逻辑, 该方法运行在子线程
    public abstract void onLoad();

    // 开始加载网络数据
    public void loadData() {

        if (!NetWorkUtils.isNetConnected(UIUtils.getContext()) && !NetWorkUtils.isNetworkOnline()) {
            mLoadingPage.mCurrentState = LoadingPage.ResultState.STATE_ERROR.getState();
            mLoadingPage.showRightPage();
            return;
        }
        if (mLoadingPage != null) {
            mLoadingPage.loadData();
        }
    }

    /**
     * 校验数据的合法性,返回相应的状态
     *
     * @param data
     * @return
     */
    public LoadingPage.ResultState check(Object data) {
        if (data != null) {
            if (data instanceof List) {//判断当前对象是否是一个集合
                List list = (List) data;
                if (!list.isEmpty()) {//数据不为空,访问成功
                    return LoadingPage.ResultState.STATE_SUCCESS;
                } else {//空集合
                    return LoadingPage.ResultState.STATE_EMPTY;
                }
            }
        }
        return LoadingPage.ResultState.STATE_ERROR;
    }


    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void dispose() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }

    public void showLoading() {
//        if (dialog != null && dialog.isShowing()) return;
//        dialog = new ProgressDialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage("请求网络中...");
//        dialog.show();
    }

    public void dismissLoading() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose();
    }
}
