package com.shine.work;

public enum AdminType {
    //
    LOG("��־"),
    //
    USER("�û�"),
    //
    FUNC("����"),
    //
    REMIND("����"),
    //
    MESSAGE("��Ϣ"),
    //
    TALK("����"),
    //
    DEL("ɾ��"),
    //
    GROUPNUM("Ⱥ��"),
    //
    ENCRY("����"),
    //
    DECRY("����");

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
