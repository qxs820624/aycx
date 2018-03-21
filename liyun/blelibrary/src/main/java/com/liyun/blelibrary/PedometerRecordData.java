package com.liyun.blelibrary;

import java.io.Serializable;

/**
 * @author chen.qinlei
 * @create 2016-07-06 12:24.
 */
public class PedometerRecordData implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    int   id;
    int   time;
    short seconds;
    short walkSteps;
    short runSteps;
    short xActivity;
    short yActivity;
    short zActivity;

    public PedometerRecordData(int id, int time, short seconds, short walkSteps, short runSteps, short xActivity, short yActivity, short zActivity) {
        this.id = id;
        this.time = time;
        this.seconds = seconds;
        this.walkSteps = walkSteps;
        this.runSteps = runSteps;
        this.xActivity = xActivity;
        this.yActivity = yActivity;
        this.zActivity = zActivity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public short getSeconds() {
        return seconds;
    }

    public void setSeconds(short seconds) {
        this.seconds = seconds;
    }

    public short getWalkSteps() {
        return walkSteps;
    }

    public void setWalkSteps(short walkSteps) {
        this.walkSteps = walkSteps;
    }

    public short getRunSteps() {
        return runSteps;
    }

    public void setRunSteps(short runSteps) {
        this.runSteps = runSteps;
    }

    public short getXActivity() {
        return xActivity;
    }

    public void setXActivity(short xActivity) {
        this.xActivity = xActivity;
    }

    public short getYActivity() {
        return yActivity;
    }

    public void setYActivity(short yActivity) {
        this.yActivity = yActivity;
    }

    public short getZActivity() {
        return zActivity;
    }

    public void setZActivity(short zActivity) {
        this.zActivity = zActivity;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
