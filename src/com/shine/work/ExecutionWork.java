package com.shine.work;

import iqq.im.bean.QQMsg;

import com.shine.qq.client.QqMessag;

/**
 * ����ִ�нӿ�.
 * 
 * 
 * <pre>
 * �޸�����		�޸���	�޸�ԭ��
 * 2014-6-6	SGJ	�½�
 * </pre>
 */
public interface ExecutionWork {

    /**
     * 
     * ִ������.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-6	SGJ	�½�
     * </pre>
     */
    public void executCommand(QqMessag msgClient, QQMsg msg) throws Exception;
    
}