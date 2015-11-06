package com.brandtnewtonsoftware.asle;

import java.util.ArrayList;

/**
 * Created by Brandt on 11/4/2015.
 */
public class User {

    private final int id;
    private final String name;
    private int loginCount;


    public User(int id, String name, int loginCount) {
        this.id = id;
        this.name = name;
        this.loginCount = loginCount;
    }



    public int getId() {
        return id;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void incrementLoginCount() {
        loginCount++;
    }

    public String getName() {
        return name;
    }
}
