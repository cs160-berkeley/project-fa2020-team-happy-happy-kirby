package com.gmail.kingarthuralagao.us.civicengagement.core.networking;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public abstract class DisposableObserverWrapper<T> extends DisposableObserver<T> {

    protected abstract void onFail(String error);

    protected abstract void onHttpException(JsonElement error);

    protected abstract void onSuccess(T t);

    @Override
    public void onError(Throwable t) {
        if (t instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) t).response().errorBody();
            if (((HttpException) t).response().code() != 401) {
                try {
                    String responseBodyJson = responseBody.string();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.fromJson(responseBodyJson, JsonElement.class);
                    onHttpException(jsonElement);
                } catch (Exception e) {
                    onFail(((HttpException) t).response().code() + ": " + e.getMessage());
                }
            }
        } else
            onFail(t.getMessage());
    }


    @Override
    public void onNext(T t) {
        if (t instanceof Response && ((Response) t).code() != 200) {
            onError(new HttpException((Response) t));
        } else {
            onSuccess(t);
        }
    }

    @Override
    public void onComplete() {

    }
}