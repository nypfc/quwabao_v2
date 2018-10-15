package com.gedoumi.quwabao.common.base;

/**
 * 类名：MsgConfig
 * 功能：消息配置文件
 */
public interface MsgConfig {

    /**
     * dwz 状态
     * 值：statusCode
     */
    String STATUSCODE = "statusCode";

    /**
     * dwz信息
     * 值：message
     */
    String MESSAGE = "message";

    /**
     * dwz message 占位符赋值  {1}{2}   多个值用，隔开
     * 值：messageValues
     */
    String MESSAGEVALUES = "messageValues";

    /**
     * 操作成功 响应码
     * 值：200
     */
    Integer CODE_SUCCESS = 200;

    /**
     * 操作失败 响应码
     * 值：300
     */
    Integer CODE_FAIL = 300;

    /**
     * 登录超时 响应码
     * 值：301
     */
    Integer CODE_OVERTIME = 301;

    /**
     * ajaxJson 页面取值key
     * 值：msginfo
     */
    String MSGINFO = "msginfo";

    /**
     * messages_zh.properties 配置文件key  操作成功
     * 值：msg.operation.success
     */
    String MSG_KEY_SUCCESS = "msg.operation.success";

    /**
     * messages_zh.properties 配置文件key  操作失败
     * 值：msg.operation.failure
     */
    String MSG_KEY_FAIL = "msg.operation.failure";

    /**
     * messages_zh.properties 配置文件key  数据不存在，已被删除
     * 值：msg.data.hasdelete
     */
    String MSG_KEY_NODATA = "msg.data.hasdelete";

    /**
     * messages_zh.properties 配置文件key  无操作权限
     * 值：msg.request.nopower
     */
    String MSG_KEY_NOPOWER = "msg.request.nopower";

    /**
     * ajaxDone 页面路径
     * 值：ajaxDone
     */
    String PAGE_AJAXDONE = "ajaxDone";
    /**
     * ajaxMessage 页面路径
     * 值：ajaxMessage
     */
    String PAGE_AJAXMESSAGE = "ajaxMessage";

    /**
     * ajaxJson 页面路径
     * 值：ajaxJson
     */
    String PAGE_AJAXJSON = "ajaxJson";

    /**
     * 查询列表总数
     */
    String TOTLE = "totle";

}

