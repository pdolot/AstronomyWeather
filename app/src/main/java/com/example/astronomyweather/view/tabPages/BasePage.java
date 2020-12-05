package com.example.astronomyweather.view.tabPages;

abstract public class BasePage<T extends Object> implements BasePageInterface {
    private T data;
    private BaseListener listener;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setListener(BaseListener listener) {
        this.listener = listener;
    }

    public BaseListener getListener() {
        return listener;
    }
}
