package org.anlntse.platform.vcenter;

import com.vmware.vim25.VirtualMachineConfigInfo;

import java.util.Date;

/**
 * @program: VBlog
 * @description: overwrite
 * @author: Jun Xie
 * @create: 2021-07-20 22:41
 **/

class VirtualMachineConfigInfo1 extends VirtualMachineConfigInfo {

    private Date createDate;
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
