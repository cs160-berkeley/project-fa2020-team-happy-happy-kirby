package com.gmail.kingarthuralagao.us.civicengagement.core.utils;

import androidx.lifecycle.MutableLiveData;

import com.gmail.kingarthuralagao.us.civicengagement.data.Resource;

public class StateLiveData<T> extends MutableLiveData<Resource<T>> {

    public void postLoading() {
        postValue(new Resource<T>().loading());
    }

    public void postSuccess(T data) {
        postValue(new Resource<T>().success(data));
    }

    public void postError(Throwable error) {
        postValue(new Resource<T>().error(error));
    }
}
