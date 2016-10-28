package org.progforge.taskexample.database;

import org.progforge.taskexample.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseFileConfiguration implements DatabaseConfiguration {

    private String user;
    private String password;
    private String url;

    public DatabaseFileConfiguration(File configuration){
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(configuration));

            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");

            if (! Util.notNull(url, user, password)) {
                throw new RuntimeException("Bad configuration file for org.progforge.taskexample.database!");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Can not open configuration file for org.progforge.taskexample.database: " + configuration.getAbsolutePath());
        }
    }

    public String getUser(){
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getURL() {
        return url;
    }

}
