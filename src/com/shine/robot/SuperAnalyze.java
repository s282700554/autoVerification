package com.shine.robot;

import org.json.JSONObject;

public interface SuperAnalyze {

    /**
     * 
     * ������Ϣ.
     * 
     * @param jsonObj
     * @return
     * @throws Exception
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-8-21	SGJ	�½�
     * </pre>
     */
    public String processMessage(JSONObject jsonObj) throws Exception;

}
