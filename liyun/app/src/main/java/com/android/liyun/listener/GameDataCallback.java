package com.android.liyun.listener;

/**
* 
*@author hzx
*created at 2018/3/20 12:16
*/
public interface GameDataCallback {
    void saveGameData(String gameName, String gameData);

    void saveSennoIndex(double index);

    void requestGameData(String gameName);

    void pickPhoto();

    void shareGameResult();

    void closeGame();
}
