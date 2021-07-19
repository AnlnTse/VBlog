package org.anlntse.utils;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.mo.util.MorUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

import java.rmi.RemoteException;
import java.util.*;

public class VimUtils {

    // 返回MOR字符串 (type:id)
    public static String getMor(ManagedObjectReference mor) {
        return mor.getType() + ":" + mor.getVal();
    }

    // 返回MOR字符串 (vcid:type:id)
    public static String getMor(String vcid, ManagedObjectReference mor) {
        return vcid + ":" + mor.getType() + ":" + mor.getVal();
    }

    // 返回MOR类型
    public static String getMorType(String morid) throws RuntimeException {
        if (!StringUtils.contains(morid, ":") || morid.split(":").length < 2) {
            throw new RuntimeException("morid value is not valid. (morid = " + morid + ")");
        }
        return morid.split(":")[0];
    }

    // 返回MOR值
    public static String getMorValue(String morid) throws RuntimeException {
        if (!StringUtils.contains(morid, ":") || morid.split(":").length < 2) {
            throw new RuntimeException("morid value is not valid. (morid = " + morid + ")");
        }
        return morid.split(":")[1];
    }

    public static String getMorid(String morType, String morValue) {
        if (StringUtils.isEmpty(morType) || StringUtils.isEmpty(morValue)) {
            throw new RuntimeException("not valid parameter. (morType = " + morType + ", morValue = " + morValue + ")");
        }
        return morType + ":" + morValue;
    }

    // 创建MOR对象
    public static ManagedObjectReference createMor(String morid) {
        ManagedObjectReference mor = new ManagedObjectReference();
        mor.setType(getMorType(morid));
        mor.setVal(getMorValue(morid));
        return mor;
    }

    // 返回虚机的磁盘容量（KB）
    public static long getDiskSizeKB(VirtualMachine vm) {
        long diskInKb = 0;
        VirtualDevice[] vds = vm.getConfig().getHardware().getDevice();
        for (VirtualDevice vd : vds) {
            if (vd instanceof VirtualDisk) {
                VirtualDisk virDisk = (VirtualDisk) vd;
                diskInKb += virDisk.getCapacityInKB();
            }
        }
        return diskInKb;
    }

    // 返回虚机的磁盘列表
    public static List<VirtualDisk> getVmDisks(VirtualMachineConfigInfo configInfo) {
        List<VirtualDisk> disks = new ArrayList<VirtualDisk>();
        VirtualDevice[] devices = configInfo.getHardware().getDevice();
        for (int i = 0; i < devices.length; i++) {
            VirtualDevice device = devices[i];
            if (device instanceof VirtualDisk) {
                disks.add((VirtualDisk) device);
            }
        }
        return disks;
    }

    // 返回虚机的磁盘列表(按磁盘序号排序)
    public static List<VirtualDisk> getVmDisks(VirtualMachine vm) {
        List<VirtualDisk> disks = new ArrayList<VirtualDisk>();
        VirtualDevice[] devices = vm.getConfig().getHardware().getDevice();
        for (int i = 0; i < devices.length; i++) {
            VirtualDevice device = devices[i];
            if (device instanceof VirtualDisk) {
                disks.add((VirtualDisk) device);
            }
        }

        // Collections.sort(disks,new Comparator<VirtualDisk>(){
        // public int compare(VirtualDisk d1, VirtualDisk d2) {
        // return (d1.getUnitNumber() - d2.getUnitNumber());
        // }
        // });
        return disks;
    }

    // 返回虚机的网卡列表(按网卡序号排序)
    public static List<VirtualEthernetCard> getVmEthernetCards(VirtualMachineConfigInfo configInfo) {
        List<VirtualEthernetCard> ethernetCards = new ArrayList<VirtualEthernetCard>();
        VirtualDevice[] devices = configInfo.getHardware().getDevice();
        for (int i = 0; i < devices.length; i++) {
            VirtualDevice device = devices[i];
            if (device instanceof VirtualE1000) {
                ethernetCards.add((VirtualE1000) device);
            } else if (device instanceof VirtualE1000e) {
                ethernetCards.add((VirtualE1000e) device);
            } else if (device instanceof VirtualVmxnet2) {
                ethernetCards.add((VirtualVmxnet2) device);
            } else if (device instanceof VirtualVmxnet3) {
                ethernetCards.add((VirtualVmxnet3) device);
            }
        }

        return ethernetCards;
    }

    // 返回虚机的网卡列表(按网卡序号排序)
    public static List<VirtualEthernetCard> getVmEthernetCards(VirtualMachine vm) {
        List<VirtualEthernetCard> ethernetCards = new ArrayList<VirtualEthernetCard>();
        VirtualDevice[] devices = vm.getConfig().getHardware().getDevice();
        for (int i = 0; i < devices.length; i++) {
            VirtualDevice device = devices[i];
            if (device instanceof VirtualE1000) {
                ethernetCards.add((VirtualE1000) device);
            } else if (device instanceof VirtualE1000e) {
                ethernetCards.add((VirtualE1000e) device);
            } else if (device instanceof VirtualVmxnet2) {
                ethernetCards.add((VirtualVmxnet2) device);
            } else if (device instanceof VirtualVmxnet3) {
                ethernetCards.add((VirtualVmxnet3) device);
            }
        }

        return ethernetCards;
    }

    // 返回网卡类型
    // public static String getVmEthernetCardType(VirtualEthernetCard device) {
    // String deviceType = "";
    // if (device instanceof VirtualE1000) {
    // deviceType = CommonEnum.ETCARD_TYPE.e1000.toString();
    // }else if (device instanceof VirtualE1000e) {
    // deviceType = CommonEnum.ETCARD_TYPE.e1000e.toString();
    // }else if (device instanceof VirtualVmxnet2) {
    // deviceType = CommonEnum.ETCARD_TYPE.vmxnet2.toString();
    // }else if (device instanceof VirtualVmxnet3) {
    // deviceType = CommonEnum.ETCARD_TYPE.vmxnet3.toString();
    // }
    // return deviceType;
    // }

    // 根据MAC地址匹配虚机网卡信息
    public static GuestNicInfo findNicInfoByMacAddr(GuestNicInfo[] nicinfos, String macAddr) {
        for (GuestNicInfo nic : nicinfos) {
            if (StringUtils.equals(nic.getMacAddress(), macAddr)) {
                return nic;
            }
        }
        return null;
    }

    // 根据网卡设备key匹配虚机网卡信息
    public static GuestNicInfo findNicInfoByDeviceKey(GuestNicInfo[] nicinfos, int deviceKey) {
        if (nicinfos != null) {
            for (GuestNicInfo nic : nicinfos) {
                if (nic.getDeviceConfigId() == deviceKey) {
                    return nic;
                }
            }
        }
        return null;
    }

    // 返回磁盘的类型
    // public static String getVmDiskType(VirtualDisk vd) {
    // VirtualDiskFlatVer2BackingInfo backing =
    // (VirtualDiskFlatVer2BackingInfo)vd.getBacking();
    // if (backing.getThinProvisioned() != null && backing.getThinProvisioned()) {
    // return Const.DISK_TYPE_THIN;
    // }else {
    // if (backing.getEagerlyScrub() != null && backing.getEagerlyScrub()) {
    // return Const.DISK_TYPE_THICK_EAGER;
    // }else {
    // return Const.DISK_TYPE_THICK_LAZY;
    // }
    // }
    // }

    // 返回虚机的snapshot列表
    public static List<VirtualMachineSnapshotTree> getVmSnapshots(VirtualMachine vm) {
        List<VirtualMachineSnapshotTree> snapshotList = new ArrayList<VirtualMachineSnapshotTree>();
        VirtualMachineSnapshotInfo snapshotInfo = vm.getSnapshot();
        if (snapshotInfo != null) {
            VirtualMachineSnapshotTree[] snapshotTrees = snapshotInfo.getRootSnapshotList();
            retriveSnapshotTree(snapshotTrees, snapshotList);
        }
        return snapshotList;
    }

    // 递归遍历虚机的snapshot tree，返回snapshot列表
    public static void retriveSnapshotTree(VirtualMachineSnapshotTree[] snapshotTrees,
                                           List<VirtualMachineSnapshotTree> snapshotList) {
        if (snapshotTrees != null) {
            VirtualMachineSnapshotTree snapshotTree = snapshotTrees[0];
            snapshotList.add(snapshotTree);
            retriveSnapshotTree(snapshotTree.getChildSnapshotList(), snapshotList);
        }
    }

    // 计算存储池的磁盘使用率
    public static int calDiskUsage(Datastore ds) {
        DatastoreSummary summary = ds.getSummary();
        long freeSpace = summary.getFreeSpace();
        long capacity = summary.getCapacity();
        return Math.round((capacity - freeSpace) * 100 / capacity);
    }

    // 根据网卡分布式端口组key匹配端口组信息
    public static DistributedVirtualPortgroup findDvpGroupInfoByDeviceKey(List<Network> networks,
                                                                          String dvportgroupKey) {
        if (networks != null) {
            for (Network network : networks) {
                if (network instanceof DistributedVirtualPortgroup) {
                    DistributedVirtualPortgroup dvportgrp = (DistributedVirtualPortgroup) network;
                    if (StringUtils.equals(dvportgrp.getKey(), dvportgroupKey)) {
                        return dvportgrp;
                    }
                }
            }
        }
        return null;
    }

    // 返回最小使用率的存储池
    public static Datastore getMinUsedDatastore(Datastore[] datastores) {
        Datastore minDatastore = null;
        if (datastores != null) {
            minDatastore = getMinUsedDatastore(Arrays.asList(datastores));
        }
        return minDatastore;
    }

    // 返回最小使用率的存储池
    public static Datastore getMinUsedDatastore(List<Datastore> datastores) {
        float minUR = 100.0f;
        Datastore minDatastore = null;
        if (datastores != null) {
            for (Datastore ds : datastores) {
                float dsur = calUtilRate(ds);
                if (dsur < minUR) {
                    minUR = dsur;
                    minDatastore = ds;
                } else if (dsur == minUR) {
                    if (minDatastore != null
                            && ds.getSummary().getFreeSpace() > minDatastore.getSummary().getFreeSpace()) {
                        minDatastore = ds;
                    }
                }
            }
        }
        return minDatastore;
    }

    public static float calUtilRate(Datastore das) {
        DatastoreSummary summary = das.getSummary();
        long freeSpace = summary.getFreeSpace();
        long capacity = summary.getCapacity();
        return (float) Math.round((float) (capacity - freeSpace) * 100 / capacity);
    }

    public static boolean isThinProvisioned(VirtualDisk disk) {
        VirtualDeviceBackingInfo backing = disk.getBacking();
        if (backing != null) {
            if (backing instanceof VirtualDiskFlatVer2BackingInfo) {
                VirtualDiskFlatVer2BackingInfo diskBacking = (VirtualDiskFlatVer2BackingInfo) backing;
                return diskBacking.getThinProvisioned();
            }
        }
        return false;
    }

    public static String getDiskMode(VirtualDisk disk) {
        VirtualDeviceBackingInfo backing = disk.getBacking();
        if (backing != null) {
            if (backing instanceof VirtualDiskFlatVer2BackingInfo) {
                VirtualDiskFlatVer2BackingInfo diskBacking = (VirtualDiskFlatVer2BackingInfo) backing;
                return diskBacking.getDiskMode();
            } else if (backing instanceof VirtualDiskFlatVer1BackingInfo) {
                VirtualDiskFlatVer1BackingInfo diskBacking = (VirtualDiskFlatVer1BackingInfo) backing;
                return diskBacking.getDiskMode();
            } else if (backing instanceof VirtualDiskRawDiskMappingVer1BackingInfo) {
                VirtualDiskRawDiskMappingVer1BackingInfo diskBacking = (VirtualDiskRawDiskMappingVer1BackingInfo) backing;
                return diskBacking.getDiskMode();
            }
        }
        return "";
    }

    public static String getDiskFile(VirtualDisk disk) {
        VirtualDeviceBackingInfo backing = disk.getBacking();
        if (backing != null) {
            if (backing instanceof VirtualDeviceFileBackingInfo) {
                return ((VirtualDeviceFileBackingInfo) backing).getFileName();
            }
        }
        return "";
    }

    public static ManagedObjectReference getDiskStorage(VirtualDisk disk) {
        VirtualDeviceBackingInfo backing = disk.getBacking();
        if (backing != null) {
            if (backing instanceof VirtualDeviceFileBackingInfo) {
                return ((VirtualDeviceFileBackingInfo) backing).getDatastore();
            }
        }
        return null;
    }

    public static ManagedObjectReference getNwAdapterPortGroup(VirtualEthernetCard nwAdapter,
                                                               Map<String, ManagedObjectReference> netMap) {
        VirtualDeviceBackingInfo backing = nwAdapter.getBacking();
        if (backing != null) {
            if (backing instanceof VirtualEthernetCardNetworkBackingInfo) {
                return ((VirtualEthernetCardNetworkBackingInfo) backing).getNetwork();
            } else {
                // ManagedObjectReference mor = new ManagedObjectReference();
                // mor.setType("DistributedVirtualPortgroup");
                if (backing instanceof VirtualEthernetCardDistributedVirtualPortBackingInfo) {
                    ManagedObjectReference mor = netMap
                            .get(((VirtualEthernetCardDistributedVirtualPortBackingInfo) backing).getPort()
                                    .getPortgroupKey());
                    return (mor);
                }
            }
        }
        return null;
    }

    public static String getPortGroupName(VirtualMachine vm, ManagedObjectReference mor) {
        ManagedEntity me = MorUtil.createExactManagedEntity(vm.getServerConnection(), mor);
        return me.getName();
    }

    public static String[] getIpAddressOfNwdapter(GuestInfo guestInfo, int nwAdapterKey) {
        List<String> ipAddrs = new ArrayList<String>();
        if (guestInfo == null) {
            return new String[] {};
        }
        GuestNicInfo[] guestNicInfos = guestInfo.getNet();
        if (guestNicInfos != null && guestNicInfos.length > 0) {
            for (GuestNicInfo guestNicInfo : guestNicInfos) {
                int deviceConfigId = guestNicInfo.getDeviceConfigId();
                if (nwAdapterKey != deviceConfigId) {
                    continue;
                }
                String[] ipAddress = guestNicInfo.getIpAddress();
                if (ipAddress != null && ipAddress.length > 0) {
                    for (String address : ipAddress) {
                        if (IpUtils.isIp(address)) {
                            ipAddrs.add(address);
                        }
                    }
                }
            }
        }

        return ipAddrs.toArray(new String[ipAddrs.size()]);
    }

    public static String[] getIpAddressOfNwdapter(VirtualMachine vm, int nwAdapterKey) {
        List<String> ipAddrs = new ArrayList<String>();
        GuestInfo guestInfo = vm.getGuest();
        if (guestInfo == null) {
            return new String[] {};
        }
        GuestNicInfo[] guestNicInfos = guestInfo.getNet();
        if (guestNicInfos != null && guestNicInfos.length > 0) {
            for (GuestNicInfo guestNicInfo : guestNicInfos) {
                int deviceConfigId = guestNicInfo.getDeviceConfigId();
                if (nwAdapterKey != deviceConfigId) {
                    continue;
                }
                String[] ipAddress = guestNicInfo.getIpAddress();
                if (ipAddress != null && ipAddress.length > 0) {
                    for (String address : ipAddress) {
                        if (IpUtils.isIp(address)) {
                            ipAddrs.add(address);
                        }
                    }
                }
            }
        }

        return ipAddrs.toArray(new String[ipAddrs.size()]);
    }

    public static String getMacAddressOfNwdapter(GuestInfo guestInfo, int nwAdapterKey) {
        if (guestInfo == null) {
            return "";
        }
        GuestNicInfo[] guestNicInfos = guestInfo.getNet();
        if (guestNicInfos != null && guestNicInfos.length > 0) {
            for (GuestNicInfo guestNicInfo : guestNicInfos) {
                int deviceConfigId = guestNicInfo.getDeviceConfigId();
                if (nwAdapterKey == deviceConfigId) {
                    return guestNicInfo.getMacAddress();
                }
            }
        }
        return "";
    }

    public static String getMacAddressOfNwdapter(VirtualMachine vm, int nwAdapterKey) {
        GuestInfo guestInfo = vm.getGuest();
        if (guestInfo == null) {
            return "";
        }
        GuestNicInfo[] guestNicInfos = guestInfo.getNet();
        if (guestNicInfos != null && guestNicInfos.length > 0) {
            for (GuestNicInfo guestNicInfo : guestNicInfos) {
                int deviceConfigId = guestNicInfo.getDeviceConfigId();
                if (nwAdapterKey == deviceConfigId) {
                    return guestNicInfo.getMacAddress();
                }
            }
        }
        return "";
    }

    public static List<String[]> getIpAddressOfNwdapter(VirtualMachine vm) {
        GuestInfo guestInfo = vm.getGuest();
        if (guestInfo == null) {
            return Collections.emptyList();
        }
        List<String[]> ipAddrs = new ArrayList<>();
        GuestNicInfo[] guestNicInfos = guestInfo.getNet();
        if (guestNicInfos != null && guestNicInfos.length > 0) {
            for (GuestNicInfo guestNicInfo : guestNicInfos) {
                StringBuilder ip = new StringBuilder();
                String[] ipAddress = guestNicInfo.getIpAddress();
                if (ipAddress != null && ipAddress.length > 0) {
                    for (String address : ipAddress) {
                        if (IpUtils.isIp(address)) {
                            ip.append(",").append(address);
                        }
                    }
                }
                String ipaddr = ip.toString();
                if (!ObjectUtils.isEmpty(ipaddr)) {
                    ipaddr = ipaddr.substring(1);
                }
                ipAddrs.add(new String[] { guestNicInfo.getNetwork(), ipaddr });
            }
        }
        return ipAddrs;
    }

    // public static boolean isIp(String addr) {
    // if(addr.length()<7 || addr.length()>15 || "".equals(addr)){
    // return false;
    // }
    // String rexp =
    // "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    // Pattern pat = Pattern.compile(rexp);
    // Matcher mat = pat.matcher(addr);
    // return mat.matches();
    // }

    public static String[] getDnsOfNwdapter(VirtualMachine vm, int nwAdapterKey) {
        List<String> dnss = new ArrayList<String>();
        GuestInfo guestInfo = vm.getGuest();
        if (guestInfo == null) {
            return new String[] {};
        }
        GuestNicInfo[] guestNicInfos = guestInfo.getNet();
        if (guestNicInfos != null && guestNicInfos.length > 0) {
            for (GuestNicInfo guestNicInfo : guestNicInfos) {
                int deviceConfigId = guestNicInfo.getDeviceConfigId();
                if (nwAdapterKey != deviceConfigId) {
                    continue;
                }
                NetDnsConfigInfo netDnsConfigInfo = guestNicInfo.getDnsConfig();
                if (netDnsConfigInfo != null) {
                    String[] dnsAddress = netDnsConfigInfo.getIpAddress();
                    if (dnsAddress != null && dnsAddress.length > 0) {
                        for (String address : dnsAddress) {
                            if (IpUtils.isIp(address)) {
                                dnss.add(address);
                            }
                        }
                    }
                }
            }
        }
        return dnss.toArray(new String[dnss.size()]);
    }

    public static String getDiskFileName(VirtualDeviceBackingInfo backingInfo) {
        String fileName = "";
        if (backingInfo instanceof VirtualDiskFlatVer1BackingInfo) {
            VirtualDiskFlatVer1BackingInfo backing = (VirtualDiskFlatVer1BackingInfo) backingInfo;
            fileName = backing.getFileName();
        } else if (backingInfo instanceof VirtualDiskFlatVer2BackingInfo) {
            VirtualDiskFlatVer2BackingInfo backing = (VirtualDiskFlatVer2BackingInfo) backingInfo;
            fileName = backing.getFileName();
        } else if (backingInfo instanceof VirtualDiskRawDiskMappingVer1BackingInfo) {
            VirtualDiskRawDiskMappingVer1BackingInfo backing = (VirtualDiskRawDiskMappingVer1BackingInfo) backingInfo;
            fileName = backing.getFileName();
        } else if (backingInfo instanceof VirtualDiskSeSparseBackingInfo) {
            VirtualDiskSeSparseBackingInfo backing = (VirtualDiskSeSparseBackingInfo) backingInfo;
            fileName = backing.getFileName();
        } else if (backingInfo instanceof VirtualDiskSparseVer1BackingInfo) {
            VirtualDiskSparseVer1BackingInfo backing = (VirtualDiskSparseVer1BackingInfo) backingInfo;
            fileName = backing.getFileName();
        } else if (backingInfo instanceof VirtualDiskSparseVer2BackingInfo) {
            VirtualDiskSparseVer2BackingInfo backing = (VirtualDiskSparseVer2BackingInfo) backingInfo;
            fileName = backing.getFileName();
        }
        return fileName;
    }

    public static String getDiskUuid(VirtualDeviceBackingInfo backingInfo) {
        String uuid = "";
        if (backingInfo instanceof VirtualDiskPartitionedRawDiskVer2BackingInfo) {
            VirtualDiskPartitionedRawDiskVer2BackingInfo backing = (VirtualDiskPartitionedRawDiskVer2BackingInfo) backingInfo;
            uuid = backing.getUuid();
        } else if (backingInfo instanceof VirtualDiskFlatVer2BackingInfo) {
            VirtualDiskFlatVer2BackingInfo backing = (VirtualDiskFlatVer2BackingInfo) backingInfo;
            uuid = backing.getUuid();
        } else if (backingInfo instanceof VirtualDiskRawDiskMappingVer1BackingInfo) {
            VirtualDiskRawDiskMappingVer1BackingInfo backing = (VirtualDiskRawDiskMappingVer1BackingInfo) backingInfo;
            uuid = backing.getUuid();
        } else if (backingInfo instanceof VirtualDiskSeSparseBackingInfo) {
            VirtualDiskSeSparseBackingInfo backing = (VirtualDiskSeSparseBackingInfo) backingInfo;
            uuid = backing.getUuid();
        } else if (backingInfo instanceof VirtualDiskRawDiskVer2BackingInfo) {
            VirtualDiskRawDiskVer2BackingInfo backing = (VirtualDiskRawDiskVer2BackingInfo) backingInfo;
            uuid = backing.getUuid();
        } else if (backingInfo instanceof VirtualDiskSparseVer2BackingInfo) {
            VirtualDiskSparseVer2BackingInfo backing = (VirtualDiskSparseVer2BackingInfo) backingInfo;
            uuid = backing.getUuid();
        }
        return uuid;
    }

    public static String getDiskpathOfDisk(GuestInfo guestInfo, int diskNumber) {
        if (guestInfo == null) {
            return "";
        }
        GuestDiskInfo[] guestDiskInfos = guestInfo.getDisk();
        if (guestDiskInfos != null && guestDiskInfos.length > 0) {
            if (diskNumber < guestDiskInfos.length) {
                GuestDiskInfo guestDiskInfo = guestDiskInfos[diskNumber];
                if (guestDiskInfo != null) {
                    return guestDiskInfo.getDiskPath();
                }
            }
        }
        return "";
    }

    public static String getDiskpathOfDisk(VirtualMachine vm, int diskNumber) {
        GuestInfo guestInfo = vm.getGuest();
        if (guestInfo == null) {
            return "";
        }
        GuestDiskInfo[] guestDiskInfos = guestInfo.getDisk();
        if (guestDiskInfos != null && guestDiskInfos.length > 0) {
            if (diskNumber < guestDiskInfos.length) {
                GuestDiskInfo guestDiskInfo = guestDiskInfos[diskNumber];
                if (guestDiskInfo != null) {
                    return guestDiskInfo.getDiskPath();
                }
            }
        }
        return "";
    }

    public static ManagedEntity createMe(ServiceInstance si, String targetMor) {
        return MorUtil.createExactManagedEntity(si.getServerConnection(), VimUtils.createMor(targetMor));

    }

    public static ManagedObject createMo(ServiceInstance si, String targetMor) {
        return MorUtil.createExactManagedObject(si.getServerConnection(), VimUtils.createMor(targetMor));

    }

    public static ManagedEntity findTarget(ServiceInstance si, String type, String name) throws Exception {
        Folder rootFolder = si.getRootFolder();
        ManagedEntity me = null;
        me = (ManagedEntity) new InventoryNavigator(rootFolder).searchManagedEntity(type, name);
        return me;
    }

    public static ManagedEntity[] findTargets(ServiceInstance si, String type) throws Exception {
        Folder rootFolder = si.getRootFolder();
        ManagedEntity[] mes = null;
        mes = (ManagedEntity[]) new InventoryNavigator(rootFolder).searchManagedEntities(type);
        return mes;
    }

    public static ManagedEntity[] findTargets(ServiceInstance si, String type, String name, boolean ignoreCase) throws Exception {
        List<ManagedEntity> mesList = new ArrayList<ManagedEntity>();
        ManagedEntity[] mes = findTargets(si, type);
        if (mes != null) {
            for (ManagedEntity me : mes) {
                if (ignoreCase) {
                    if (name.equalsIgnoreCase(me.getName())) {
                        mesList.add(me);
                    }
                }else {
                    if (name.equals(me.getName())) {
                        mesList.add(me);
                    }
                }
            }
        }

        return mesList.stream().toArray(ManagedEntity[]::new);
    }

    public static ManagedEntity[] findTargets(Datacenter dc, String type, String name, boolean ignoreCase) throws Exception {
        List<ManagedEntity> mesList = new ArrayList<ManagedEntity>();
        ManagedEntity[] mes = findTargets(dc.getServerConnection().getServiceInstance(), type);
        if (mes != null) {
            for (ManagedEntity me : mes) {
                Datacenter meDc = VimUtils.getDatacenter(me);
                if (meDc == null || !StringUtils.equals(VimUtils.getMor(meDc.getMOR()), VimUtils.getMor(dc.getMOR()))) {
                    continue;
                }
                if (ignoreCase) {
                    if (name.equalsIgnoreCase(me.getName())) {
                        mesList.add(me);
                    }
                } else {
                    if (name.equals(me.getName())) {
                        mesList.add(me);
                    }
                }
            }
        }

        return mesList.stream().toArray(ManagedEntity[]::new);
    }

    public static ManagedEntity findTarget(Folder parentFolder, String type, String name) throws Exception {
        ManagedEntity me = null;
        me = findTarget(parentFolder.getServerConnection().getServiceInstance(), type, name);
        if (me != null && me.getParent() != null
                && me.getParent().getMOR().getVal().equals(parentFolder.getMOR().getVal())) {
            return me;
        } else {
            return null;
        }
    }

    public static List<VirtualDisk> getDisks(VirtualMachine vm) {
        List<VirtualDisk> disks = new ArrayList<VirtualDisk>();
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualDisk) {
                        disks.add((VirtualDisk)device);
                    }
                }
            }
        }
        return disks;
    }

    public static int getDiskControllerKey(VirtualMachine vm) {
        int controllerKey = 0;
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualDisk) {
                        VirtualDisk disk = (VirtualDisk) device;
                        controllerKey = disk.getControllerKey();
                        break;
                    }
                }
            }
        }
        return controllerKey;
    }

    public static int getMaxDiskUnitNumber(VirtualMachine vm) {
        int maxUnitNumber = 0;
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualDisk) {
                        VirtualDisk disk = (VirtualDisk) device;
                        int unitNumber = disk.getUnitNumber();
                        if (unitNumber > maxUnitNumber) {
                            maxUnitNumber = unitNumber;
                        }
                    }
                }
            }
        }
        return maxUnitNumber;
    }

    public static int getMaxDiskKey(VirtualMachine vm) {
        int maxKey = 0;
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualDisk) {
                        VirtualDisk disk = (VirtualDisk) device;
                        int key = disk.getKey();
                        if (key > maxKey) {
                            maxKey = key;
                        }
                    }
                }
            }
        }
        return maxKey;
    }

    public static int getDiskNumber(VirtualMachine vm) {
        int diskNumber = 0;
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualDisk) {
                        diskNumber++;
                    }
                }
            }
        }
        return diskNumber;
    }

    public static int getMaxControllerUnitNumber(VirtualMachine vm) {
        int maxUnitNumber = 0;
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualSCSIController) {
                        VirtualController controller = (VirtualController) device;
                        if (controller.getUnitNumber() == null) {
                            continue;
                        }
                        int unitNumber = controller.getUnitNumber();
                        if (unitNumber > maxUnitNumber) {
                            maxUnitNumber = unitNumber;
                        }
                    }
                }
            }
        }
        return maxUnitNumber;
    }

    public static int getMaxControllerKey(VirtualMachine vm) {
        int maxControllerKey = 0;
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualSCSIController) {
                        VirtualController controller = (VirtualController) device;
                        int controllerKey = controller.getKey();
                        if (controllerKey > maxControllerKey) {
                            maxControllerKey = controllerKey;
                        }
                    }
                }
            }
        }
        return maxControllerKey;
    }

    public static int getMaxControllerBusNumber(VirtualMachine vm) {
        int maxControllerBusNumber = 0;
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualSCSIController) {
                        VirtualController controller = (VirtualController) device;
                        int busNumber = controller.getBusNumber();
                        if (busNumber > maxControllerBusNumber) {
                            maxControllerBusNumber = busNumber;
                        }
                    }
                }
            }
        }
        return maxControllerBusNumber;
    }

    public static VirtualDisk getDisk(VirtualMachine vm, int index) {
        int diskNumber = 0;
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualDisk) {
                        if (diskNumber == index) {
                            return (VirtualDisk) device;
                        }
                        diskNumber++;
                    }
                }
            }
        }
        return null;
    }


    public static boolean isValidVm(VirtualMachine vm) {
        if (vm == null) {
            return false;
        }
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config == null) {
            return false;
        }
        if (StringUtils.isEmpty(config.getInstanceUuid())) {
            return false;
        }
        return true;
    }

    public static Datacenter getDatacenter(ManagedEntity me) {
        ManagedEntity p = me.getParent();
        if (p == null) {
            return null;
        } else if (p instanceof Datacenter) {
            return (Datacenter) p;
        } else {
            return getDatacenter(p);
        }
    }

    public static ClusterComputeResource getCluster(ManagedEntity me) {
        ManagedEntity p = me.getParent();
        if (p == null) {
            return null;
        } else if (p instanceof ClusterComputeResource) {
            return (ClusterComputeResource) p;
        } else {
            return getCluster(p);
        }
    }

    public static String getValidIpaddress(GuestInfo guest) {
        String ipAddr = "";
        if (guest != null) {
            ipAddr = guest.getIpAddress();
            if (!IpUtils.isIp(ipAddr)) {
                ipAddr = "";
            }
        }
        return ipAddr;
    }

    public static boolean isWindows(VirtualMachine vm) {
        if (StringUtils.startsWithIgnoreCase(vm.getGuest().getGuestId(), "win")) {
            return true;
        }
        return false;
    }

    public static ClusterComputeResource getCluster(ServerConnection sc, VirtualMachineSummary summary) {
        if (summary != null) {
            ManagedObjectReference mor = summary.getRuntime().getHost();
            ManagedEntity me = MorUtil.createExactManagedEntity(sc, mor);
            while (me != null) {
                me = me.getParent();
                if (me instanceof ClusterComputeResource) {
                    return (ClusterComputeResource) me;
                }
            }
        }
        return null;
    }

    public static ClusterComputeResource getCluster(VirtualMachine vm) {
        if (vm != null) {
            ManagedObjectReference mor = vm.getSummary().getRuntime().getHost();
            ManagedEntity me = MorUtil.createExactManagedEntity(vm.getServerConnection(), mor);
            while (me != null) {
                me = me.getParent();
                if (me instanceof ClusterComputeResource) {
                    return (ClusterComputeResource) me;
                }
            }
        }
        return null;
    }

    public static Datastore getDatastore(VirtualMachine vm, VirtualDisk disk) {
        ManagedObjectReference mor = null;
        if (disk != null) {
            VirtualDeviceBackingInfo backing = disk.getBacking();
            if (backing instanceof VirtualDiskFlatVer2BackingInfo) {
                mor = ((VirtualDiskFlatVer2BackingInfo) backing).getDatastore();
            } else if (backing instanceof VirtualDiskFlatVer1BackingInfo) {
                mor = ((VirtualDiskFlatVer1BackingInfo) backing).getDatastore();
            } else if (backing instanceof VirtualDiskRawDiskMappingVer1BackingInfo) {
                mor = ((VirtualDiskRawDiskMappingVer1BackingInfo) backing).getDatastore();
            }
        }
        if (mor != null) {
            return (Datastore) MorUtil.createExactManagedEntity(vm.getServerConnection(), mor);
        }
        return null;
    }

    /*public static String getTargetUUID(ReqVm reqVm) throws Exception, InvalidProperty, RuntimeFault, RemoteException {
        String targetUUID;
        targetUUID = reqVm.getBlueprintMachine().getSpUuid();
        String clusterMorid = VcOperationCommon.extractMoridFromUUID(reqVm.getReservation().getSpUuid());
        ServiceInstance si = VcCloudService.getServiceInstance(reqVm.getSpTenant());

        ClusterComputeResource cluster = (ClusterComputeResource) VimUtils.createMo(si,
                "ClusterComputeResource:" + clusterMorid);
        String morid = "VirtualMachine:"
                + VcOperationCommon.extractMoridFromUUID(reqVm.getBlueprintMachine().getSpUuid());
        VirtualMachine vm = (VirtualMachine) VimUtils.createMe(si, morid);
        Folder tempaltesFolder = (Folder) VimUtils.findTarget(si, "Folder", AppConfig.getVcTemplateFolder());
        if (tempaltesFolder == null) {
            return targetUUID;
        }
        ManagedEntity[] templates = tempaltesFolder.getChildEntity();
        String templateNameForFastDeploy = vm.getName() + "-" + cluster.getName();
        for (ManagedEntity me : templates) {
            if (StringUtils.equalsIgnoreCase(me.getName(), templateNameForFastDeploy)) {
                vm = (VirtualMachine) me;
//                targetUUID = VcOperationCommon.UUID_PREFIX_BLUEPRINT_MACHINE
//                        + VcOperationCommon.getUUIDFromMorid(vm, vm.getMOR().get_value());
                targetUUID = VcOperationCommon.UUID_PREFIX_BLUEPRINT_MACHINE + VcOperationCommon.getUUID(vm);
                break;
            }
        }
        return targetUUID;
    }*/

    public static Folder findChildFolder(Folder parentFolder, String childFolderName) {
        try {
            for (ManagedEntity me : parentFolder.getChildEntity()) {
                if (StringUtils.equals(me.getMOR().getType(), "Folder")
                        && StringUtils.equals(me.getName(), childFolderName)) {
                    return (Folder) me;
                }
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static VirtualMachine findChildVirtualMachine(Folder parentFolder, String childVirtualMachineName) {
        try {
            for (ManagedEntity me : parentFolder.getChildEntity()) {
                if (StringUtils.equals(me.getMOR().getType(), "VirtualMachine")
                        && StringUtils.equals(me.getName(), childVirtualMachineName)) {
                    return (VirtualMachine) me;
                }
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static ManagedObjectReference getSnapshotReference(VirtualMachine vm, String snapshotName) throws Exception {
        VirtualMachineSnapshotInfo snapInfo = vm.getSnapshot();
        ManagedObjectReference snapmor = null;

        if (snapInfo != null) {
            VirtualMachineSnapshotTree[] snapTree = snapInfo.getRootSnapshotList();
            snapmor = traverseSnapshotInTree(snapTree, snapshotName);
        }
        return snapmor;
    }

    public static ManagedObjectReference traverseSnapshotInTree(VirtualMachineSnapshotTree[] snapTree,
                                                                String snapshotName) {
        if (snapTree == null || snapshotName == null)
            return null;

        ManagedObjectReference snapmor = null;
        for (int i = 0; i < snapTree.length && snapmor == null; i++) {
            VirtualMachineSnapshotTree node = snapTree[i];
            if (node.getName().equals(snapshotName)) {
                snapmor = node.getSnapshot();
            } else {
                VirtualMachineSnapshotTree[] childTree = snapTree[i].getChildSnapshotList();
                snapmor = traverseSnapshotInTree(childTree, snapshotName);
            }
        }
        return snapmor;
    }

    public static String parseGuestFullName(String guestFullName) {
        if (guestFullName != null) {
            guestFullName = guestFullName.replaceAll("Microsoft Windows Server", "Win");
            guestFullName = guestFullName.replaceAll("Enterprise Linux ", "");
        }
        return guestFullName;
    }

    public static Map<String, String> getUserPassFromVMAnnotation(ServiceInstance si, VirtualMachine vm) {
        String annotation = vm.getConfig().getAnnotation();
        Map<String, String> map = new HashMap<>();
        if (annotation != null && annotation.indexOf("\n") > -1 && annotation.indexOf("tpluser") > -1
                && annotation.indexOf("tplpass") > -1) {
            String[] lines = annotation.split("\n");
            for (String line : lines) {
                if (line.indexOf("tpluser") > -1 || line.indexOf("tplpass") > -1) {
                    String[] unp = line.split(" ");
                    map.put(unp[0], unp[1]);
                }
            }
        }
        return map;
    }

    public static HostSystem getHost(ServiceInstance si, VirtualMachine vm) {
        VirtualMachineRuntimeInfo runtime = vm.getRuntime();
        if (runtime == null) {
            return null;
        }
        ManagedObjectReference host = runtime.getHost();
        if (host == null) {
            return null;
        }
        return (HostSystem) VimUtils.createMe(si, VimUtils.getMor(host));
    }

    public static List<Folder> findFolders(ServiceInstance si, List<String> folderPaths)  throws Exception {
        //folderPaths format: template,pso-sh::/模板目录1/模板目录2,pso-sh::/Discovered virtual machine/vm模板/vm模板2020
        List<Folder> folders = new ArrayList<>();
        if (!ObjectUtils.isEmpty(folderPaths)) {
            for (String vcFolder : folderPaths) {
                ManagedEntity[] folders1;
                if (StringUtils.contains(vcFolder, "::")) {
                    String vcFolderName = StringUtils.contains(vcFolder,"/") ? StringUtils.substringAfterLast(vcFolder,"/") : vcFolder;
                    String dc = StringUtils.substringBefore(vcFolder, "::");
                    Datacenter datacenter = (Datacenter) VimUtils.findTarget(si, "Datacenter", dc);
                    folders1 = VimUtils.findTargets(datacenter, "Folder", vcFolderName, true);
                }else {
                    String vcFolderName = StringUtils.contains(vcFolder,"/") ? StringUtils.substringAfterLast(vcFolder,"/") : vcFolder;
                    folders1 = VimUtils.findTargets(si, "Folder", vcFolderName, true);
                }
                if (folders1 != null && folders1.length > 0) {
                    for (ManagedEntity folder1 : folders1) {
                        String fullPath = VimUtils.getFolderPath(folder1);
                        if (StringUtils.contains(vcFolder, "::") && vcFolder.equals(fullPath)) {
                            folders.add((Folder)folder1);
                        }else if (!StringUtils.contains(vcFolder, "::")
                                && (vcFolder.equals(StringUtils.substringAfter(fullPath,"::")) || vcFolder.equals(StringUtils.substringAfter(fullPath,"::/")))) {
                            folders.add((Folder)folder1);
                        }
                    }
                }
            }
        }
        return folders;
    }

    public static String getFolderPath(ManagedEntity me) {
        ManagedEntity p = me.getParent();
        if (p == null) {
            return "";
        }
        //System.out.println(p.getMOR().getVal()+":::"+p.getMOR().get_value());
        if (p.getMOR().getVal().equals("group-d1")) {
            return "";
        }
        if (p instanceof Datacenter) {
            return ((Datacenter) p).getName() + "::";
        }
        if ("vm".equals(p.getName()) && p instanceof Folder && p.getParent() != null && !(p.getParent() instanceof Folder)) {
            return VimUtils.getDatacenter(p).getName() + "::" + "/" + me.getName();
        }

        String folderPath = getFolderPath(p);
        if (StringUtils.isBlank(folderPath)) {
            return p.getName();
        }
        if (me instanceof Folder) {
            return folderPath + "/" + me.getName();
        }
        return folderPath;
    }
}
