package com.course.course.Database;

import com.course.course.Models.User;

public class CurentUser {
    private static CurentUser instance;
    private User user;
    private CurentUser() {}

    public static CurentUser getInstance() {
        if (instance == null) {
            instance = new CurentUser();
        }
        return instance;
    }
    public void initSession(User user) {
        this.user = user;
    }

    public void clearSession() {
        this.user = null;
    }

    public User getUser() {
        return user;
    }
}

