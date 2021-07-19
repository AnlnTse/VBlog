package org.anlntse.contants;

public enum RecordStatus {
    active("活动"), inactive("无效"), archive("归档"), system("系统"), deleted("删除");

    private final String title;

    private RecordStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static boolean isNormal(RecordStatus status) {
        return status != null && (RecordStatus.active.equals(status) || RecordStatus.system.equals(status));
    }
}
