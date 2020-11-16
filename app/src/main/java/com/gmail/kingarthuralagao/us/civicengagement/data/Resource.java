package com.gmail.kingarthuralagao.us.civicengagement.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class Resource<T> {

    @NonNull
    private Status status;

    @Nullable
    private T data;

    @Nullable
    private Throwable error;

    public Resource() {
        this.status = Status.CREATED;
        this.data = null;
        this.error = null;
    }

    public Resource<T> success(@NonNull T data) {
        this.status = Status.SUCCESS;
        this.data = data;
        this.error = null;
        return this;
    }

    public Resource<T> error(Throwable error) {
        this.status = Status.ERROR;
        this.data = null;
        this.error = error;
        return this;
    }

    public Resource<T> loading() {
        this.status = Status.LOADING;
        this.data = null;
        this.error = null;
        return this;
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }
}