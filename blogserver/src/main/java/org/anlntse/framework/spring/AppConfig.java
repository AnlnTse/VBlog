package org.anlntse.framework.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppConfig {
    private static int viConnectTimeout;
    private static int viReadTimeout;

    public static int getViConnectTimeout() {
        return viConnectTimeout;
    }

    @Value("${vi_connect_timeout}")
    public void setViConnectTimeout(int viConnectTimeout) {
        AppConfig.viConnectTimeout = viConnectTimeout;
    }

    public static int getViReadTimeout() {
        return viReadTimeout;
    }

    @Value("${vi_read_timeout}")
    public void setViReadTimeout(int viReadTimeout) {
        AppConfig.viReadTimeout = viReadTimeout;
    }


/*
    private static String region;
    private static String namesrvAddr;
    private static String producerGroupGlobal;
    private static String consumerGroupGlobal;
    private static String topicGlobal;
    private static String producerGroupLocal;
    private static String consumerGroupLocal;
    private static String topicLocal;
    private static int consumeThreadMin;
    private static int consumeThreadMax;
    private static int consumeMessageBatchMaxSize;
    private static String mqDisable;
    private static boolean mqCleanAndSkip;
    private static String mqSyncRegionsFromGlobal;
    private static String vcTemplateFolder;
    private static String localTemplateFolder;
    private static String vcEsxiManagementVmk;
    private static boolean perfDbOff;// chenfu 20200720
    private static int perfCollectThreads;
    private static String logLevel;
    private static String fmExportFile;
    private static String cmVmExportFile;
    private static String mqMessageToDB;



    public static String getCmVmExportFile() {
		return cmVmExportFile;
	}

    @Value("${cm_vm_export_file}")
	public void setCmVmExportFile(String cmVmExportFile) {
		AppConfig.cmVmExportFile = cmVmExportFile;
	}

	public static String getFmExportFile() {
		return fmExportFile;
	}

    @Value("${fm_export_file}")
	public void setFmExportFile(String fmExportFile) {
		AppConfig.fmExportFile = fmExportFile;
	}

	@Value("${region}")
    public void setRegion(String region) {
        AppConfig.region = region;
    }

    public static String getRegion() {
        return region;
    }

    public static String getNamesrvAddr() {
        return namesrvAddr;
    }

    @Value("${mq.namesrvAddr}")
    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public static String getProducerGroupGlobal() {
        return producerGroupGlobal;
    }

    @Value("${mq.producerGroup.global}")
    public void setProducerGroupGlobal(String producerGroupGlobal) {
        this.producerGroupGlobal = producerGroupGlobal;
    }

    public static String getConsumerGroupGlobal() {
        return consumerGroupGlobal;
    }

    @Value("${mq.consumerGroup.global}")
    public void setConsumerGroupGlobal(String consumerGroupGlobal) {
        this.consumerGroupGlobal = consumerGroupGlobal;
    }

    public static String getTopicGlobal() {
        return topicGlobal;
    }

    @Value("${mq.topic.global}")
    public void setTopicGlobal(String topicGlobal) {
        this.topicGlobal = topicGlobal;
    }

    public static String getProducerGroupLocal() {
        return producerGroupLocal;
    }

    @Value("${mq.producerGroup.local}")
    public void setProducerGroupLocal(String producerGroupLocal) {
        this.producerGroupLocal = producerGroupLocal;
    }

    public static String getConsumerGroupLocal() {
        return consumerGroupLocal;
    }

    @Value("${mq.consumerGroup.local}")
    public void setConsumerGroupLocal(String consumerGroupLocal) {
        this.consumerGroupLocal = consumerGroupLocal;
    }

    public static String getTopicLocal() {
        return topicLocal;
    }

    @Value("${mq.topic.local}")
    public void setTopicLocal(String topicLocal) {
        this.topicLocal = topicLocal;
    }

    public static int getConsumeThreadMin() {
        return consumeThreadMin;
    }

    @Value("${mq.consumeThreadMin}")
    public void setConsumeThreadMin(int consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    public static int getConsumeThreadMax() {
        return consumeThreadMax;
    }

    @Value("${mq.consumeThreadMax}")
    public void setConsumeThreadMax(int consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }

    public static int getConsumeMessageBatchMaxSize() {
        return consumeMessageBatchMaxSize;
    }

    @Value("${mq.consumeMessageBatchMaxSize}")
    public void setConsumeMessageBatchMaxSize(int consumeMessageBatchMaxSize) {
        this.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
    }

    public static String getMqDisable() {
        return mqDisable;
    }

    @Value("${mq.disable}")
    public void setMqDisable(String mqDisable) {
        this.mqDisable = mqDisable;
    }

    public static boolean isGlobal() {
        return "global".equalsIgnoreCase(AppConfig.getRegion());
    }

    public static boolean isMQDisable() {
        return "true".equalsIgnoreCase(AppConfig.getMqDisable());
    }

    public static boolean getMqCleanAndSkip() {
        return mqCleanAndSkip;
    }

    @Value("${mq.cleanAndSkip}")
    public void setMqCleanAndSkip(String mqCleanAndSkip) {
        AppConfig.mqCleanAndSkip = "true".equalsIgnoreCase(mqCleanAndSkip);
    }

    public static String getMqSyncRegionsFromGlobal() {
        return mqSyncRegionsFromGlobal;
    }

    @Value("${mq.syncRegionsFromGlobal}")
    public void setMqSyncRegionsFromGlobal(String mqSyncRegionsFromGlobal) {
        AppConfig.mqSyncRegionsFromGlobal = mqSyncRegionsFromGlobal;
    }

    public static String getVcTemplateFolder() {
		if (vcTemplateFolder == null || vcTemplateFolder.length()==0) {
			return "template";
		}
		return vcTemplateFolder;
	}

	@Value("${vc_template_folder}")
	public void setVcTemplateFolder(String vcTemplateFolder) {
		this.vcTemplateFolder = vcTemplateFolder;
	}

	public static String getVcEsxiManagementVmk() {
		return vcEsxiManagementVmk;
	}

	@Value("vc_esxi_management_vmk")
	public void setVcEsxiManagementVmk(String vcEsxiManagementVmk) {
		this.vcEsxiManagementVmk = vcEsxiManagementVmk;
	}

	public static String getLocalTemplateFolder() {
		return localTemplateFolder;
	}

	@Value("${local_template_folder}")
	public void setLocalTemplateFolder(String localTemplateFolder) {
		AppConfig.localTemplateFolder = localTemplateFolder;
	}

	@Value("${perfdb.off}")
	public void setPerfDbOff(String perfDbOff) {
		AppConfig.perfDbOff = "true".equalsIgnoreCase(perfDbOff);
	}

	public static boolean getPerfDbOff() {
		return perfDbOff;
	}

	@Value("${perfdb.collectThreads}")
	public static void setPerfCollectThreads(String perfCollectThreads) {
		int num = Optional.ofNullable(perfCollectThreads).map(t -> Integer.parseInt(t)).orElse(0);
		if (num <= 0) {
			num = Runtime.getRuntime().availableProcessors();
		}
		AppConfig.perfCollectThreads = num;
	}

	public static int getPerfCollectThreads() {
		return perfCollectThreads;
	}

    public static String getLogLevel() {
        return logLevel;
    }

    @Value("${log_level}")
    public void setLogLevel(String logLevel) {
        AppConfig.logLevel = logLevel;
    }



    public static String getMqMessageToDB() {
        return mqMessageToDB;
    }

    @Value("${mq.messageToDb}")
    public void setMqMessageToDB(String mqMessageToDB) {
        this.mqMessageToDB = mqMessageToDB;
    }

    public static boolean isMQMessageToDB() {
        return !"false".equalsIgnoreCase(AppConfig.getMqMessageToDB());
    }*/
}
