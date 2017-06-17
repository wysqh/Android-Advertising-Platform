package com.example.gtk.platform.model;

public class BaseResult<T> {
    private String message;
    private boolean success;
    private T data;

    public BaseResult() {
    }

    public BaseResult(boolean isSuccess, String message, T data) {
        this.success = isSuccess;
        this.message = message;
        this.data = data;
    }


    public BaseResult(T data, String isSuccessMessage, String failedMessage) {
        if (data != null){
            this.success = true;
            this.message = isSuccessMessage;
        } else {
            this.success = false;
            this.message = failedMessage;
        }
        this.data = data;
    }


    public BaseResult(T data) {
        if (data != null){
            this.success = true;
            this.message = "查询成功";
            this.data = data;
        } else {
            this.success = false;
            this.message = "查询失败";
            this.data = null;
        }
    }

    public BaseResult(boolean isSuccess, String message) {
        this.success = isSuccess;
        this.message = message;
    }

    public void construct(T data){
        if (data != null){
            this.success = true;
            this.message = "查询成功";
            this.data = data;
        } else {
            this.success = false;
            this.message = "查询失败";
            this.data = null;
        }
    }

    public void construct(T data,boolean isSuccess){
        if (isSuccess){
            this.success = isSuccess;
            this.message = "查询成功";
            this.data = data;
        } else {
            this.success = isSuccess;
            this.message = "查询失败";
            this.data = null;
        }
    }

    public void construct(T data, String isSuccessMessage, String failedMessage) {
        if (data != null){
            this.success = true;
            this.message = isSuccessMessage;
        } else {
            this.success = false;
            this.message = failedMessage;
        }
        this.data = data;
    }

    public void construct(T data,boolean isSuccess, String message){
        this.success = isSuccess;
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
