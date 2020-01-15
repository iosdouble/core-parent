package org.nh.core.log.eventlog.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Classname EventLogBean
 * @Description TODO
 * @Date 2019/10/16 4:11 PM
 * @Created by nihui
 */
@Getter
@Setter
public class EventLogBean implements Serializable {


    ///////////////////////////////////////////////////////////////

    @JsonProperty(value = "log_type")
    private String logType;//event、error、info、thirdParty

    @JsonProperty(value = "hostname")
    private String hostname;//主机设置的名称

    @JsonProperty(value = "timetamp")
    private String timetamp;//日志生成的时间戳（毫秒）

    ///////////////////////////////////////////////////////////////

    @JsonProperty(value = "component")
    private String component;//设计家平台

    @JsonProperty(value = "sub_system")
    private String subSystem;//design-app

    @JsonProperty(value = "framework")
    private String framework;//J2EE

    @JsonProperty(value = "client_ip")
    private String clientIp;

    ///////////////////////////////////////////////////////////////

    @JsonProperty(value = "event_log_level")
    private String eventLogLevel;//info、exception、error、info

    @JsonProperty(value = "event_log_type")
    private String eventLogType;//api.execute

    @JsonProperty(value = "event_log_id")
    private String eventLogId;

    @JsonProperty(value = "event_log_execution_time")
    private long eventLogExecutionTime;//日志执行时长

    ///////////////////////////////////////////////////////////////

    @JsonProperty(value = "http_method")
    private String httpMethod;

    @JsonProperty(value = "clazz_name")
    private String clazzName;

    @JsonProperty(value = "method_name")
    private String methodName;

    @JsonProperty(value = "api_name")
    private String apiName;

    @JsonProperty(value = "api_version")
    private String apiVersion;

    @JsonProperty(value = "api_params")
    private String apiParams;

    @JsonProperty(value = "api_execution_time")
    private long apiExecutionTime;//API执行时长

    @JsonProperty(value = "http_status")
    private String httpStatus;

    @JsonProperty(value = "error_id")
    private String errorId;

    @JsonProperty(value = "error_code")
    private String errorCode;

    @JsonProperty(value = "error_msg")
    private String errorMsg;

    ///////////////////////////////////////////////////////////////

    @JsonProperty(value = "member_id")
    private String memberId;

    @JsonProperty(value = "session_id")
    private String sessionId;

}
