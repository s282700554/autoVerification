package com.shine.work;

import iqq.im.bean.QQMsg;

import com.shine.qq.client.QqMessag;

/**
 * 命令执行接口.
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-6-6	SGJ	新建
 * </pre>
 */
public interface ExecutionWork {

    /**
     * 
     * 执行命令.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-6	SGJ	新建
     * </pre>
     */
    public void executCommand(QqMessag msgClient, QQMsg msg) throws Exception;
    
}