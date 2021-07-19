package org.anlntse.platform.vcenter;

import com.vmware.vim25.mo.ServiceInstance;
import org.anlntse.bean.SpTenant;
import org.anlntse.framework.spring.AppConfig;
import org.anlntse.utils.EncryptUtil;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VcCloudService implements ICloudService {
    private static final Map<String, Object[]> AuthenticationMap = new ConcurrentHashMap<>();
    private static final long ExpireTime = 25 * 60 * 1000; //should be a little shorter than the real expiration time.

    public static ServiceInstance getServiceInstance(SpTenant entity) throws Exception {

        try {
            String key = entity.getServerConnection().getServerUrl() + "<=>" + entity.getUsername() + "<=>" + entity.getPassword() + "<=>vCenter";
            Object[] values = AuthenticationMap.get(key);
            ServiceInstance si = null;
            if (values == null) {
                si = new ServiceInstance(new URL(entity.getServerConnection().getServerUrl()),
                        entity.getUsername(), EncryptUtil.decryptWithRSA(entity.getPassword()), true,
                        AppConfig.getViConnectTimeout(), AppConfig.getViReadTimeout());
                values = new Object[]{si, System.currentTimeMillis()};
                AuthenticationMap.put(key, values);
            }

            Long time = (Long) values[1];
            if (System.currentTimeMillis() - time > ExpireTime) {
                AuthenticationMap.remove(key);
                return getServiceInstance(entity);
            }
            return (ServiceInstance) values[0];
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

}
