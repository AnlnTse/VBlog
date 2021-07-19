package org.anlntse.platform.vcenter;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.mo.util.MorUtil;
import org.anlntse.bean.SpAlert;
import org.anlntse.bean.SpEndpoint;
import org.anlntse.utils.VimUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class VcAlertOperations {

    protected static final Logger logger = LoggerFactory.getLogger(VcAlertOperations.class);

    public static List list(SpEndpoint endpoint) throws Exception {
        ServiceInstance si = VcCloudService.getServiceInstance(endpoint.getSpTenant());
        try {
            si.getSessionManager().setLocale("zh_CN");
        }catch (Exception e) {
            logger.info("set session locale failed.", e);
        }
        Folder folder = si.getRootFolder();
        List result = new ArrayList<>();
        if (ObjectUtils.isEmpty(folder.getTriggeredAlarmState())) {
            return result;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (AlarmState alarmState : folder.getTriggeredAlarmState()) {
            ManagedObjectReference mor = alarmState.alarm;
            if (mor != null) {
                AlarmInfo info = ((Alarm) VimUtils.createMo(si, "Alarm:" + mor.val)).getAlarmInfo();
                if (info != null) {
                    //String uuid = VcOperationCommon.UUID_PREFIX_ALERT + VcOperationCommon.getUUIDFromMorid(si, alarmState.key);
                    String uuid = VcOperationCommon.getUUIDFromMorid(si, alarmState.key + "-" + alarmState.getTime().getTimeInMillis());
                    SpAlert alert = new SpAlert();
                    alert.setSpUuid(uuid);
                    alert.setEndpoint(endpoint);
                    alert.setSpTenant(endpoint.getSpTenant());
                    alert.setName(info.name);
                    alert.setEntity(alarmState.getEntity().val);
                    String ip = null;
                    String entity = null;
                    ManagedEntity me = MorUtil.createExactManagedEntity(si.getServerConnection(), alarmState.getEntity());
                    if (me instanceof VirtualMachine) {
                        VirtualMachine vm = (VirtualMachine) me;
                        List<String[]> ips = VimUtils.getIpAddressOfNwdapter(vm);
                        if (ips.size() > 0) {
                            ip = ips.get(0)[1];
                        }
                        entity = vm.getConfig().getName();
                        alert.setTargetType("VirtualMachine");
                    } else if (me instanceof HostSystem) {
                    	alert.setTargetType("HostSystem");
                        HostSystem host = (HostSystem) me;
                        HostConfigInfo configInfo = host.getConfig();
                        HostNetworkInfo networkInfo = configInfo != null ? configInfo.getNetwork() : null;
                        if (networkInfo != null) {
                            // host vnic
                            HostVirtualNic[] hostVirtualNics = networkInfo.getVnic();
                            if (!ObjectUtils.isEmpty(hostVirtualNics)) {
                                for (HostVirtualNic hostVirtualNic : hostVirtualNics) {
                                    HostVirtualNicSpec spec = hostVirtualNic.getSpec();
                                    if (spec != null) {
                                        HostIpConfig ipConfig = spec.getIp();
                                        if (ipConfig != null) {
                                            ip = ipConfig.getIpAddress();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        entity = host.getName();
                    } else if (me instanceof Datastore) {
                    	alert.setTargetType("Datastore");
                        Datastore ds = (Datastore) me;
                        DatastoreSummary ds_info = ds.getSummary();
                        entity = ds_info.getName();
                    } else if (me instanceof ClusterComputeResource) {
                    	alert.setTargetType("ClusterComputeResource");
                        ClusterComputeResource ds = (ClusterComputeResource) me;
                        entity = ds.getName();
                        ClusterConfigInfo clusterConfigInfo = ds.getConfiguration();
                    } else if (me instanceof Folder) {
                    	alert.setTargetType("Folder");
                        Folder ds = (Folder) me;
                        entity = ds.getName();
                    }
                    if (StringUtils.isNotBlank(entity)) {
                        alert.setEntity(entity);
                    }
                    alert.setIpAddress(ip);
                    alert.setMessage(info.description);
                    alert.setTime(format.format(alarmState.getTime().getTime()));
                    alert.setAcknowledged(alarmState.getAcknowledged());
                    alert.setAcknowledgedByUser(alarmState.getAcknowledgedByUser());
                    Calendar cal = alarmState.getAcknowledgedTime();
                    if (cal != null)
                        alert.setAcknowledgedTime(format.format(cal.getTime()));
                    alert.setEventKey(alarmState.getEventKey());
                    alert.setOverallStatus(alarmState.getOverallStatus().name());
                    result.add(alert);
                }
            }
        }
        Collections.sort(result, (SpAlert o1, SpAlert o2) -> {
            return o1.getName().compareTo(o2.getName());
        });
        return result;
    }

}
