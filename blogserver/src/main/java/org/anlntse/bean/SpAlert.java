package org.anlntse.bean;

import org.anlntse.contants.SpResourceStatus;

public class SpAlert extends  BaseEntity {

    private SpEndpoint endpoint;

    private String message;

    private String time;

    private String entity;

    private Boolean acknowledged;

    private String acknowledgedByUser;

    private String acknowledgedTime;

    private String overallStatus;

    private Integer eventKey;

    private String ipAddress;

    private String targetType;

    private String spUuid;

    private String displayName;

    private SpTenant spTenant;

    private SpResourceStatus resourceStatus;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public SpEndpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(SpEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Boolean getAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(Boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public String getAcknowledgedByUser() {
        return acknowledgedByUser;
    }

    public void setAcknowledgedByUser(String acknowledgedByUser) {
        this.acknowledgedByUser = acknowledgedByUser;
    }

    public String getAcknowledgedTime() {
        return acknowledgedTime;
    }

    public void setAcknowledgedTime(String acknowledgedTime) {
        this.acknowledgedTime = acknowledgedTime;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }

    public Integer getEventKey() {
        return eventKey;
    }

    public void setEventKey(Integer eventKey) {
        this.eventKey = eventKey;
    }

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}



}
