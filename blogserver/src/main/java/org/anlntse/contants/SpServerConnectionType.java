package org.anlntse.contants;

public enum SpServerConnectionType {
    vcenter("vCenter"),nsxt("NSXT");

    private final String title;

    private SpServerConnectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
