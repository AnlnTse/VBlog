package org.anlntse.bean;

import org.anlntse.contants.SpEndpointType;
import org.anlntse.contants.SpResourceStatus;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


/**
 * @program: VBlog
 * @description: 资源端点
 * @author: Jun Xie
 * @create: 2021-07-19 19:37
 **/
public class SpEndpoint {

    public SpEndpoint() {
    }

    private SpEndpointType type;

    private String serverUrl;

    private String username;

    private String password;

    private String instanceUuid;

    private String computeManagersInstanceUuid;

    private String version;

    private SpTenant spTenant;

    private SpResourceStatus resourceStatus;

    public SpEndpointType getType() {
        return type;
    }

    public void setType(SpEndpointType type) {
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

    public String getInstanceUuid() {
        return instanceUuid;
    }

    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = instanceUuid;
    }

    public String getComputeManagersInstanceUuid() {
        return computeManagersInstanceUuid;
    }

    public void setComputeManagersInstanceUuid(String computeManagersInstanceUuid) {
        this.computeManagersInstanceUuid = computeManagersInstanceUuid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public SpTenant getSpTenant() {
        return spTenant;
    }

    public void setSpTenant(SpTenant spTenant) {
        this.spTenant = spTenant;
    }

    public SpResourceStatus getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(SpResourceStatus resourceStatus) {
        this.resourceStatus = resourceStatus;
    }
}
