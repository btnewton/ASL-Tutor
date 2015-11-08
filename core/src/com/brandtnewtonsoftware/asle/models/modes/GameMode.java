package com.brandtnewtonsoftware.asle.models.modes;

import com.brandtnewtonsoftware.asle.util.Database;

import java.sql.Connection;

/**
 * Created by Brandt on 11/8/2015.
 */
public abstract class GameMode {
    private final String name;

    protected GameMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
