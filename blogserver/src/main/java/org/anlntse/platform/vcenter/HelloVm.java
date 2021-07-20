package org.anlntse.platform.vcenter;

import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.mo.*;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * @program: VBlog
 * @description: Test sdk
 * @author: Jun Xie
 * @create: 2021-07-20 20:42
 **/
public class HelloVm {

    private static final String  url = "https://172.16.48.23/sdk";
    private static final String  username = "administrator@vsphere.local";
    private static final String  password = "VMware1!";

    private static Logger logger = LoggerFactory.getLogger(HelloVm.class);
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        logger.info("start -> "+start);
        ServiceInstance si = null;
        try {
            si = new ServiceInstance(new URL(url), username, password, true);
        } catch (MalformedURLException | RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        //    ServiceInstance si = SampleUtil.createServiceInstance();
        long end = System.currentTimeMillis();
        Folder rootFolder = si.getRootFolder();
        logger.info("time taken:" + (end - start));
        String name = rootFolder.getName();
        logger.info("vc root folder:" + name);
        ManagedEntity[] mes = new ManagedEntity[0];
        try {
            mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (mes == null || mes.length == 0) {
            return;
        }

        VirtualMachine vm = (VirtualMachine) mes[0];
        Object vminfo = vm.getConfig();

//        BeanUtils.copyProperties(vm.getConfig(), new VirtualMachineConfigInfo1());
//        VirtualMachineConfigInfo1 vminfo = (VirtualMachineConfigInfo1) vm.getConfig();
//        Date aa = vminfo.getCreateDate();
        VirtualMachineCapability vmc = vm.getCapability();
        logger.info("Name: " + vm.getName());
        logger.info("IP: " + vm.getGuest().getIpAddress());
        logger.info("FQDN: " + vm.getGuest().getHostName());
        logger.info("MOID: " + vm.getMOR().val);
        logger.info("InstanceUUID: " + vm.getSummary().getConfig().getInstanceUuid());
//        logger.info("GuestOS: " + vminfo.getGuestFullName());
        logger.info("Multiple snapshot supported: " + vmc.isMultipleSnapshotsSupported());


//        System.out.println("vm time = " + ss.getTime());
        si.getServerConnection().logout();
    }


 /*   private static boolean migrateVM(String targetVMName, String newHostName,
                                     boolean tryAnotherVM, boolean tryAnotherHost) throws Exception {
        ServiceInstance si = new ServiceInstance(new URL(url), username, password, true);
        try {
            Folder rootFolder = si.getRootFolder();
            HostSystem newHost = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity("HostSystem", newHostName);
            return migrateVM(si, rootFolder, newHost, targetVMName, newHostName, tryAnotherVM, tryAnotherHost);
        } finally {
            si.getServerConnection().logout();
        }
    }
*/

}
