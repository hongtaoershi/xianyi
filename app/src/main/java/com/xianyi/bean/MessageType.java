package com.xianyi.bean;

import java.io.Serializable;

/**
 * ${todo}<我的闲置bean>
 *
 * @author lht
 * @data: on 15/12/22 17:00
 */
public class MessageType extends BaseBean implements Serializable {
    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;
    /**
     * 消息类型  0：小易提醒 1：留言 2：交易提醒
     */
    public int messageType;
    /**
     * 内容
     */
    public String messageContext;
    /**
     * 消息个数
     */
    public int  messageNum;
    /**
     * 消息时间
     */
    public String  messageTime;
}