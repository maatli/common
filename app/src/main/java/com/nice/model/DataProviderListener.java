package com.nice.model;

import java.util.List;

/**
 * Created by libo on 15/9/25.
 */
public class DataProviderListener<T> {
    /**
     * 加载到的本地数据
     * @param t
     */
    public void onLocalDataLoaded(T t) {

    }

    public void onLocalDataLoaded(List<T> list) {

    }

    /**
     * 获取到的远程数据
     * @param t
     */
    public void onRemoteDataLoaded(T t) {

    }

    public void onRemodeDataLoaded(List<T> list) {

    }

    /**
     * 给出的错误信息
     * @param err
     */
    public void onLocalError(String err) {

    }

    /**
     * 给出的远程错误信息
     * @param err
     */
    public void onRemoteError(String err) {

    }
}
