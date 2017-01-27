package com.sharmastech.skillhouettes.callbacks;

public interface IItemHandler {

    void onFinish(Object results, int requestId);

    void onError(String errorCode, int requestId);

}