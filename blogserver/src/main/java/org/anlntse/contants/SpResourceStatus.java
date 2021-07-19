package org.anlntse.contants;

public enum SpResourceStatus {

    Active("正常"), Disable("无效");

    private final String title;

    private SpResourceStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
