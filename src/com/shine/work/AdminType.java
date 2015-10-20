package com.shine.work;

public enum AdminType {
    //
    LOG("日志"),
    //
    USER("用户"),
    //
    FUNC("功能"),
    //
    REMIND("提醒"),
    //
    MESSAGE("消息"),
    //
    TALK("聊天"),
    //
    DEL("删除"),
    //
    GROUPNUM("群号"),
    //
    ENCRY("加密"),
    //
    DECRY("解密");

    private String commd;

    AdminType(String commd) {
        this.commd = commd;
    }

    public String getCommd() {
        return this.commd;
    }

    public static AdminType getEnum(String commd) {
        for (AdminType tmpExecType : AdminType.values()) {
            if (tmpExecType.getCommd().equals(commd)) {
                return tmpExecType;
            }
        }
        return null;
    }
}
