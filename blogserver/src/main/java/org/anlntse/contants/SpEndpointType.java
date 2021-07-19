package org.anlntse.contants;

public enum SpEndpointType {
    vcenter("vCenter"),nsxt("NSXT");

    private final String title;

    private SpEndpointType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
