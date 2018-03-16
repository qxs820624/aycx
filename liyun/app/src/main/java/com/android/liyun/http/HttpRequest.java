package com.android.liyun.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.android.liyun.utils.JsonUtil;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.Map;

public class HttpRequest {

    private Handler handler;
    private ProgressDialog progressDialog;
    private Context context;

    public HttpRequest(Handler handler, Context context){
        this.handler = handler;
        progressDialog = new ProgressDialog(context);
        this.context = context;
    }

    public void postDataString(final String url, final int what, String tag, final Map<String,String> paramsMap, boolean isShowProgress){
        if (isShowProgress){
            progressDialog = ProgressDialog.show(context,"","加载中！");
        }
        OkGo.<String>post(url)
                .params(paramsMap,true)
                .tag(tag)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        progressDialog.dismiss();
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = response.body().toString().trim();
                        int a=100;
                        //判断返回数据是否为json，如果不是先把url和传参和token写入本地文件，方便调试
                        if (JsonUtil.isJsonValid(response.body().toString().trim())){
                            msg.obj = response.body().toString().trim();

                        }else {
                            msg.arg1 = -1;
                        }
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        progressDialog.dismiss();
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.arg1 = -1;
                        handler.sendMessage(msg);
                        super.onError(response);
                    }
                });
    }


    public void postAccumulationDataString(final String url, final int what, String tag, String encodeStr, boolean isShowProgress){
        if (isShowProgress){
            progressDialog = ProgressDialog.show(context,"","加载中！");
        }
        OkGo.<String>post(url)
                .upString(encodeStr)
                .tag(tag)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        progressDialog.dismiss();
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        //判断返回数据是否为json，如果不是先把url和传参和token写入本地文件，方便调试
                        if (JsonUtil.isJsonValid(response.body().toString().trim())){
                            msg.obj = response.body().toString().trim();
                            Log.i("测试 ，","ceshi js "+response.body().toString().trim());
                        }else {
                            msg.arg1 = -1;
                        }
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        progressDialog.dismiss();
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.arg1 = -1;
                        handler.sendMessage(msg);
                        super.onError(response);
                    }
                });
    }

    public void postDataString2(String url, final int what, String tag, Map paramsMap,boolean isShowProgress){

        if (isShowProgress){
            progressDialog = progressDialog.show(context,"","加载中！");
        }

        OkGo.<String>post(url)
                .params(paramsMap,true)
                .tag(tag)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        progressDialog.dismiss();
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = response.body().toString().trim();
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        progressDialog.dismiss();
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.arg1 = -1;
                        handler.sendMessage(msg);
                        super.onError(response);
                    }
                });

    }

    public void postDataString3(String url, final int what, String tag, String json,boolean isShowProgress){
        if (isShowProgress){
            progressDialog = progressDialog.show(context,"","加载中！");
        }
        OkGo.<String>post(url)
                .upJson(json)
                .tag(tag)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        progressDialog.dismiss();
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.obj = response.body().toString().trim();
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        progressDialog.dismiss();
                        Message msg = handler.obtainMessage();
                        msg.what = what;
                        msg.arg1 = -1;
                        handler.sendMessage(msg);
                        super.onError(response);
                    }
                });
    }

    public void downLoadFile(String url,final int what,String dir,String fileName,boolean isShowProgress){
        if (isShowProgress){
            progressDialog = progressDialog.show(context,"","加载中！");
        }
        dir = Environment.getExternalStorageDirectory().getPath()+dir;
        File file = new File(dir);
        if (!file.exists()){
            file.mkdirs();
        }
        OkGo.<File>get(url).execute(new FileCallback(dir, fileName) {
            @Override
            public void onSuccess(Response<File> response) {
                progressDialog.dismiss();
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.obj = response.body();
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Response<File> response) {
                progressDialog.dismiss();
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.arg1 = -1;
                handler.sendMessage(msg);
                super.onError(response);
            }
        });
    }

    public void getData(String url,final int what,boolean isShowProgress){
        if (isShowProgress){
            progressDialog = progressDialog.show(context,"","加载中！");
        }
        OkGo.<String>get(url).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                progressDialog.dismiss();
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.obj = response.body();
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Response<String> response) {
                progressDialog.dismiss();
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.arg1 = -1;
                handler.sendMessage(msg);
                super.onError(response);
            }
        });
    }

}
