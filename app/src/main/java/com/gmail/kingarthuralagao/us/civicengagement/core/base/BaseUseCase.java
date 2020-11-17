package com.gmail.kingarthuralagao.us.civicengagement.core.base;

import androidx.annotation.Nullable;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

abstract public class BaseUseCase<T, Params> {

    private Scheduler executorThread;
    private Scheduler uiThread;
    private DisposableObserver<T> disposableObserver;
    private CompositeDisposable disposable = new CompositeDisposable();

    public BaseUseCase(Scheduler e, Scheduler ui) {
        executorThread = e;
        uiThread = ui;
    }

    public void execute(@Nullable DisposableObserver<T> disposableObserver, Params params) {
        Observable<T> observable = this
                .createObservableUseCase(params)
                .subscribeOn(executorThread)
                .observeOn(uiThread);

        disposableObserver = observable.subscribeWith(disposableObserver);
        disposable.add(disposableObserver);
    }

    public void dispose() {
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    protected abstract Observable<T> createObservableUseCase(Params params);
}
