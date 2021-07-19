package org.anlntse.bean;

import org.anlntse.contants.SpServerConnectionType;

/**
 * @program: VBlog
 * @description: 资源服务
 * @author: Jun Xie
 * @create: 2021-07-19 19:39
 **/
public class SpServerConnection {

    private SpServerConnectionType type;

    private String serverUrl;

    private String username;

    private String password;

    private String thumbprint;

    public SpServerConnectionType getType() {
        return type;
    }

    public void setType(SpServerConnectionType type) {
        this.type = type;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
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

    public String getThumbprint() {
        return thumbprint;
    }

    public void setThumbprint(String thumbprint) {
        this.thumbprint = thumbprint;
    }
}
