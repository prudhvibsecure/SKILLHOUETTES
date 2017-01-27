package com.sharmastech.skillhouettes.callbacks;


public interface IRequestCallback {

    void onRequestInitiated();

    void onRequestComplete(String data, String mimeType);

    void onRequestFailed(String errorData, int errorcode);

    void onRequestCancelled(String extraInfo);

    void onRequestProgress(Long... values);

}
