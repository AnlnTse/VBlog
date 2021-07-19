package org.anlntse.bean;

import java.util.Objects;

/**
 * @program: VBlog
 * @description: 资源租户
 * @author: Jun Xie
 * @create: 2021-07-19 19:37
 **/
public class SpTenant  {

    public SpTenant() {
    }

    private String spUuid;

    private String displayName;

    private SpServerConnection serverConnection;

    private String username;

    private String password;

    @Override
    public String toString() {
        return "SpTenant{" +
                "spUuid='" + spUuid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", serverConnection=" + serverConnection +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public SpTenant(String spUuid, String displayName, SpServerConnection serverConnection, String username, String password) {
        this.spUuid = spUuid;
        this.displayName = displayName;
        this.serverConnection = serverConnection;
        this.username = username;
        this.password = password;
    }


    public String getSpUuid() {
        return spUuid;
    }

    public void setSpUuid(String spUuid) {
        this.spUuid = spUuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public SpServerConnection getServerConnection() {
        return serverConnection;
    }

    public void setServerConnection(SpServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
