package com.nice.model;

/**
 * Created by libo on 15/9/25.
 */
public class BaseDataProvider<T> {

    protected DataProviderListener mDataListener = null;

    public BaseDataProvider() {

    }

    public BaseDataProvider(DataProviderListener listener) {
        this.mDataListener = listener;
    }

    public void setDataListener(DataProviderListener listener) {
        this.mDataListener = listener;
    }

}
