package org.anlntse.platform.vcenter;

import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.mo.ClusterComputeResource;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;

import java.util.HashMap;
import java.util.Map;

public class VcOperationCommon {

    public static final String UUID_SEPARATOR = "~";
    public static final String PREFIX_SEPARATOR = ":";
    public static final String SUB_UUID_SEPARATOR = "-";
    public static final String VM_DISKPATH_SEPARATOR = "/";
    public static final String VM_DISKNUM_SEPARATOR = "_";

    public static final String UUID_PREFIX_CATALOG_RESOURCE = "catr" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_CATALOG_ITEM = "ci" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_COMPUTE_RESOURCE = "cr" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_DEPOLYMENT = "dp" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_DEPOLYMENT_VM = "dpvm" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_RESERVATION = "resv" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_RESERVATION_STORAGE = "resvst" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_RESERVATION_NETWORK = "resvnet" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_ENDPOINT = "edpt" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_STORAGE = "st" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_COMPUTE_RESOURCE_STORAGE = "crst" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_NETWORK = "net" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_HOST = "host" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_DC = "dc" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_CATEGORY = "cg" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_BLUEPRINT = "bp" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_BLUEPRINT_MACHINE = "bpm" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_BLUEPRINT_MACHINE_DISK = "bpmd" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_BLUEPRINT_MACHINE_NETWORK = "bpmn" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_BLUEPRINT_RELATION = "bpr" + PREFIX_SEPARATOR;
    public static final String UUID_PREFIX_ALERT = "alert" + PREFIX_SEPARATOR;


    public static final String UUID_CATEGORY = UUID_PREFIX_CATEGORY + "vCenter";

    public static final String RequestID_Suffix_Execute_Script = ",execute-script";

    //  UUID FORMAT
    //  type:vc instance uuid~resource mor-subid
    //  type can be bp(blueprint), bpm(blueprintmachine), bpmd(blueprintmachinedisk), cr(computeresource), resv(reservation)

//    static String getSpBlueprintUuidPrefix(IResourceEntity e) {
//        return e.getSpUuid() + UUID_SEPARATOR;
//    }


    public static String extractMoridFromUUID(String uuid) {
        String moridWithVCUUID;
        if (uuid != null && uuid.indexOf(PREFIX_SEPARATOR) > -1) {
            moridWithVCUUID = uuid.substring(uuid.lastIndexOf(PREFIX_SEPARATOR), uuid.length());
        } else {
            moridWithVCUUID = uuid;
        }
        if (moridWithVCUUID != null && moridWithVCUUID.indexOf(UUID_SEPARATOR) > -1) {
            return moridWithVCUUID.substring(moridWithVCUUID.lastIndexOf(UUID_SEPARATOR) + 1, moridWithVCUUID.length());
        }
        return moridWithVCUUID;
    }

    public static void main(String[] args) {
        System.out.println(VcOperationCommon.extractMoridFromUUID("14ea2d4d-0bc7-4199-a1d6-5979f0b04157~vm-14018"));
    }

//    public static String getVmUUID(VirtualMachine vm) {
//        return VcOperationCommon.getUUIDFromMorid(vm, vm.getMOR().get_value());
//    }
//
//
//    public static String getUUIDFromMorid(VirtualMachine vm, String morid) {
//        return VcOperationCommon.getUUIDFromMorid(vm.getServerConnection().getServiceInstance(), morid);
//    }


    public static String getUUIDFromMorid(ServiceInstance si, String morid) {
        return si.getAboutInfo().getInstanceUuid() + UUID_SEPARATOR + morid;
    }

    public static String getUUID(ManagedEntity me) {
        return getUUIDFromMorid(me.getServerConnection().getServiceInstance(), me.getMOR().getVal());
    }

    public static ManagedEntity getTypedParent(Class<?> type, ManagedEntity entity) {
        if (entity != null) {
            ManagedEntity me = entity.getParent();
            while (me != null) {
                if (me.getClass().equals(type)) {
                    return me;
                }
                me = me.getParent();
            }
        }
        return null;
    }

    public static Datacenter getDatacenterFromCluster(ClusterComputeResource cluster) {
        return (Datacenter) getTypedParent(Datacenter.class, cluster);
    }

//    private static Map<VirtualMachinePowerState, SpVmPowerStatus> powerStateMap = new HashMap<>();

//    static {
//        powerStateMap.put(VirtualMachinePowerState.poweredOn, SpVmPowerStatus.power_on);
//        powerStateMap.put(VirtualMachinePowerState.poweredOff, SpVmPowerStatus.power_off);
//        powerStateMap.put(VirtualMachinePowerState.suspended, SpVmPowerStatus.suspended);
//    }

//    public static SpVmPowerStatus getSpPowerStatus(VirtualMachinePowerState state) {
//        return powerStateMap.get(state);
//    }
}
