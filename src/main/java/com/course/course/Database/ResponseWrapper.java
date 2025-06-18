package com.course.course.Database;


public class ResponseWrapper {
    private static ResponseWrapper instance;
    private Object responseData;
    private boolean success;
    private String message;

    private ResponseWrapper() {} // Приватный конструктор

    public static synchronized ResponseWrapper getInstance() {
        if (instance == null) {
            instance = new ResponseWrapper();
        }
        return instance;
    }

    // Метод для обертки успешного результата
    public void wrapSuccess(Object data) {
        this.responseData = data;
        this.success = true;
        this.message = "Operation successful";
    }

    // Метод для обертки ошибки
    public void wrapError(String errorMessage) {
        this.responseData = null;
        this.success = false;
        this.message = errorMessage;
    }

    // Геттеры
    public Object getResponseData() {
        return responseData;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    // Очистка состояния
    public void clear() {
        this.responseData = null;
        this.success = false;
        this.message = null;
    }
}
