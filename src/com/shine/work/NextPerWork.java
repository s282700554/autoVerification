package com.shine.work;

import iqq.im.bean.QQMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.xml.PackagePerXmlOper;
import com.shine.qq.client.QqMessag;

public class NextPerWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(NextPerWork.class);

    /**
     * 
     * 执行推算验包人员命令.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-6-6 SGJ 新建
     * </pre>
     */
    @Override
    public void executCommand(QqMessag msgClient, QQMsg msg) throws Exception {
        logger.warn("更新验包为下一个人!");
        PackagePerXmlOper.updateNext();
        String personnel = PackagePerXmlOper.selectVerifyPersonnel();
        msgClient.send(msg, personnel + "今天验包,别告诉我又不在哦!", 74);
    }
}
