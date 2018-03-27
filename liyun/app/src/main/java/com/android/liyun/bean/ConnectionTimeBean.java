package com.android.liyun.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2018/3/21.
 */

public class ConnectionTimeBean extends RealmObject implements Serializable {
    @PrimaryKey
    private long id;
    private String createTime;
    private boolean isUpload;
    private double energyNum; //金币数量

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public double getEnergyNum() {
        return energyNum;
    }

    public void setEnergyNum(double energyNum) {
        this.energyNum = energyNum;
    }
}
