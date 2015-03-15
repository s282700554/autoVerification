package com.shine.work;

import iqq.im.bean.QQMsg;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.operation.bat.GenerationBat;
import com.shine.operation.bat.InvokeBat;
import com.shine.operation.eMail.sendEMail;
import com.shine.operation.file.OperFile;
import com.shine.operation.log.LogAnalysis;
import com.shine.operation.svn.UpdateSvn;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.operation.xml.DatabaseConfigXmlOper;
import com.shine.qq.client.QqMessag;

public class DatabaseWorkThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(DatabaseWorkThread.class);

    private QqMessag msgClient;

    private QQMsg msg;

    private String version;

    public DatabaseWorkThread(QqMessag msgClient, QQMsg msg, String version) {
        this.msgClient = msgClient;
        this.msg = msg;
        this.version = version;
    }

    /**
     * 
     * 运行线程,执行验包命令.
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
    public void run() {
        try {
            Map<String, String> map = DatabaseConfigXmlOper.getVersionInfo(version);
            if (map != null) {
                msgClient.send(msg, version + "版本数据库开始验包了。", 23);
                boolean bool = false;
                // 生成要使用的bat文件
                GenerationBat.createBat(map);
                for(String operation : map.get("EXECUTION_STEP").split(",")) {
                    // 如果是bat文件刚执行bat
                    if (operation.endsWith("bat")) {
                        InvokeBat invokeBat = new InvokeBat();
                        invokeBat.execution(map, operation);
                    }
                    // 操作svn
                    else if (operation.startsWith("svn")) {
                        UpdateSvn.updateOrCheckoutSvn(operation, map);
                    }
                    // 操作日志
                    else if (operation.startsWith("log")) {
                        bool = LogAnalysis.analysisLog(operation, map);
                    }
                    // 删除指定文件中的指定文件块
                    else if (operation.startsWith("del")) {
                        OperFile.removeCode(operation, map);
                    }
                    // 替换文件中的指定语句
                    else if (operation.startsWith("rep")) {
                        OperFile.replaceKeyWord(operation, map);
                    } else if (operation.startsWith("sleep")) {
                        String time = operation.split(":")[1];
                        sleep(Long.valueOf(time));
                    }
                }
                if (bool) {
                    long qq = msg.getFrom().getQQ();
                    String eMail = AuthorityControlXmlOper.getMail(qq);
                    sendEMail.sendLogFile(eMail, map);
                    msgClient.send(msg, version + "数据库验包出错,存在[ora-]错误, [sp2]错误或者[过程编译]错误,请手工干预,邮件已发送至[" + eMail + "]邮箱!请查收!", 5);
                } else {
                    msgClient.send(msg, version + "数据库验包完成,无[ora-、sp2、过程编译]错误,可以打包!", 74);
                }
                // 验完包,还原控制
                DatabasePackWork.YB_CONTROL = true;
            } else {
                msgClient.send(msg, "验包版本不正确,请输入正确的版本号", 101);
                DatabasePackWork.YB_CONTROL = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            try {
                msgClient.send(msg, "我生病了,这活你得自己干了!", 85);
                // 验完包,还原控制
                DatabasePackWork.YB_CONTROL = true;
            } catch (Exception e1) {
                DatabasePackWork.YB_CONTROL = true;
                logger.error(e1.getMessage());
            }
        }
        logger.warn("线程运行完成");
    }
}
