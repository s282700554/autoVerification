package com.shine.work;

import iqq.im.bean.QQGroup;
import iqq.im.bean.QQMsg;
import iqq.im.bean.content.ContentItem;
import iqq.im.bean.content.FaceItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.Factory.workFactory;
import com.shine.TimerTask.StartTimerTask;
import com.shine.TimerTask.TaskUtils;
import com.shine.operation.bat.GenerationBat;
import com.shine.operation.bat.InvokeBat;
import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.operation.xml.JavaConfigXmlOper;
import com.shine.operation.xml.PackageLogXmlOper;
import com.shine.qq.client.QqMessag;
import com.shine.utils.EncrypAndDecrypUtils;

public class adminWork implements ExecutionWork {

    private static Logger logger = LoggerFactory.getLogger(adminWork.class);

    public static boolean systemLock = false;
    
    /**
     * 
     * 执行还原控制命令.
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
        String userMsg = msg.getText().trim();
        String oper = userMsg.substring(3, 5).trim();
        if ("日志".equals(oper)) {
            showLog(msgClient, msg);
        } else if ("用户".equals(oper)) {
            operatingUser(msgClient, msg);
        } else if ("功能".equals(oper)) {
            operatingMode(msgClient, msg);
        } else if ("提醒".equals(oper)) {
            TaskUtils.sendPer("今天你验包哦!");
        } else if ("消息".equals(oper)) {
            sendMsg(msgClient, msg);
        } else if ("聊天".equals(oper)) {
            controlOtherWork(msgClient, msg);
        } else if ("删除".equals(oper)) {
            delFileByVersion(msgClient, msg);
        } else if ("群号".equals(oper)) {
            setGroup(msgClient, msg);
        } else if ("加密".equals(oper)) {
            encrypData(msgClient, msg, "data");
        } else if ("解密".equals(oper)) {
            decrypData(msgClient, msg, "data");
        }
    }

    /**
     * 
     * 显示日志.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-8	SGJ	新建
     * </pre>
     */
    public static void showLog(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        String date = userMsg.substring(5, userMsg.length()).trim();
        // 显示日志
        logger.warn(date + " 日志");
        String log = PackageLogXmlOper.getLogByDate(date);
        msgClient.send(msg, log, 74);
    }

    /**
     * 
     * 操作用户.
     * 
     * @管理用户查询上官剑 controlXmlUtils.getControlByUserName("上官剑");
     * @管理用户添加282700554:上官剑:0 controlXmlUtils.addXMLNode("282700554", "上官剑", "0");
     * @管理用户删除上官剑 controlXmlUtils.delXMLNodeByUserName("上官剑");
     * @管理用户修改上官剑:0 controlXmlUtils.updateXMLNodeByUserName("上官剑", "0");
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-8	SGJ	新建
     * </pre>
     */
    public static void operatingUser(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        // 添加282700554:上官剑:0
        String userOperating = userMsg.substring(5, userMsg.length()).trim();
        // 添加
        String operating = userOperating.substring(0, 2);
        // 282700554:上官剑:0
        String userInfo = userOperating.substring(2, userOperating.length());
        if ("查询".equals(operating)) {
            String userLevel = AuthorityControlXmlOper.getControlByUserName(userInfo);
            msgClient.send(msg, userLevel, 74);
        } else if ("添加".equals(operating)) {
            String[] userInfoSet = userInfo.split(":");
            String userQq = userInfoSet[0].trim();
            String userName = userInfoSet[1].trim();
            String userLevel = userInfoSet[2].trim();
            if (StringUtils.isNotBlank(userQq) && StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(userLevel)) {
                AuthorityControlXmlOper.addXMLNode(userQq, userName, userLevel);
                msgClient.send(msg, "qq:" + userQq + ",姓名:" + userName + ",级别:" + userLevel + "添加成功!", 74);
            }
        } else if ("删除".equals(operating)) {
            AuthorityControlXmlOper.delXMLNodeByUserName(userInfo);
            msgClient.send(msg, userInfo + "删除成功!", 74);
        } else if ("修改".equals(operating)) {
            String[] userInfoSet = userInfo.split(":");
            String userName = userInfoSet[0].trim();
            String userLevel = userInfoSet[1].trim();
            if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(userLevel)) {
                AuthorityControlXmlOper.updateXMLNodeByUserName(userName, userLevel);
                msgClient.send(msg, "姓名:" + userName + ",级别成功修改为:" + userLevel, 74);
            }
        }
    }

    /**
     * 
     * 
     * @管理功能查询验包 controlXmlUtils.getModeInfoModeName("验包");
     * @管理功能添加验包2 controlXmlUtils.addModeXMLNode("验包", "2");
     * @管理功能删除验包 controlXmlUtils.delModeXMLNode("验包");
     * @管理功能修改验包2 controlXmlUtils.updateModeXMLNode("验包", "2");
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-8	SGJ	新建
     * </pre>
     */
    public static void operatingMode(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        // 添加验包:2
        String modeOperating = userMsg.substring(5, userMsg.length()).trim();
        // 添加
        String operating = modeOperating.substring(0, 2);
        // 验包:2
        String modeInfo = modeOperating.substring(2, modeOperating.length());
        if ("查询".equals(operating)) {
            String modeLevel = AuthorityControlXmlOper.getModeInfoModeName(modeInfo);
            msgClient.send(msg, modeLevel, 74);
        } else if ("添加".equals(operating)) {
            String[] modeInfoSet = modeInfo.split(":");
            String modeName = modeInfoSet[0].trim();
            String modeLevel = modeInfoSet[1].trim();
            if (StringUtils.isNotBlank(modeName) && StringUtils.isNotBlank(modeLevel)) {
                AuthorityControlXmlOper.addModeXMLNode(modeName, modeLevel);
                msgClient.send(msg, "模块:" + modeName + ",级别:" + modeLevel + "添加成功!", 74);
            }
        } else if ("删除".equals(operating)) {
            AuthorityControlXmlOper.delModeXMLNode(modeInfo);
            msgClient.send(msg, modeInfo + "删除成功!", 74);
        } else if ("修改".equals(operating)) {
            String[] modeInfoSet = modeInfo.split(":");
            String modeName = modeInfoSet[0].trim();
            String modeLevel = modeInfoSet[1].trim();
            if (StringUtils.isNotBlank(modeName) && StringUtils.isNotBlank(modeLevel)) {
                AuthorityControlXmlOper.updateModeXMLNode(modeName, modeLevel);
                msgClient.send(msg, "模块:" + modeName + ",级别成功修改为:" + modeLevel, 74);
            }
        }
    }

    /**
     * 
     * 往群里发送消息.
     * 
     * @param msg
     * @param face
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-9	SGJ	新建
     * </pre>
     */
    public static void sendMsg(QqMessag msgClient, QQMsg msg) throws Exception {
        String[] sendInfo = msg.getText().trim().substring(5, msg.getText().trim().length()).split("\\[");
        int faceId = -1;
        List<ContentItem> items = msg.getContentList();
        for (ContentItem item : items) {
            if (item.getType() == ContentItem.Type.FACE) {
                faceId = ((FaceItem) item).getId();
            }
        }
        // 组装QQ消息发送回去
        QQMsg qqMsg = new QQMsg();
        qqMsg.setGroup(StartTimerTask.g); // QQ好友UIN
        qqMsg.setType(QQMsg.Type.GROUP_MSG); // 发送类型为好友
        msgClient.send(qqMsg, sendInfo[0], faceId);
    }

    /**
     * 
     * 改变发消息的群.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-8-8	SGJ	新建
     * </pre>
     */
    public static void setGroup(QqMessag msgClient, QQMsg msg) throws Exception {
        String msgInfo = msg.getText().trim().substring(5, msg.getText().trim().length());
        String groupName = null;
        for (QQGroup tempG : StartTimerTask.client.getGroupList()) {
            if (tempG.getName().startsWith(msgInfo)) {
                StartTimerTask.g = tempG;
                groupName = StartTimerTask.g.getName();
                break;
            }
        }
        msgClient.send(msg, "群设置为[" + groupName + "]成功!", 74);
    }

    /**
     * 
     * 控制聊天功能开关.
     * 
     * @管理聊天开
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-11	SGJ	新建
     * </pre>
     */
    public static void controlOtherWork(QqMessag msgClient, QQMsg msg) throws Exception {
        String msgInfo = msg.getText().trim().substring(5, msg.getText().trim().length());
        if ("开".equals(msgInfo)) {
            workFactory.isOn = true;
        } else if ("关".equals(msgInfo)) {
            workFactory.isOn = false;
        } else if ("网络开".equals(msgInfo)) {
            workFactory.isInternet = true;
        } else if ("网络关".equals(msgInfo)) {
            workFactory.isInternet = false;
        }
        msgClient.send(msg, "聊天功能" + workFactory.isOn, 74);
    }

    /**
     * 
     * 删除所有文件.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-13	SGJ	新建
     * </pre>
     */
    public static void delFileByVersion(QqMessag msgClient, QQMsg msg) throws Exception {
        String version = msg.getText().trim().substring(5, msg.getText().trim().length());
        Map<String, String> map = JavaConfigXmlOper.getVersionInfo(version);
        map.put("EXECUTION_STEP", "delAllbat");
        // 删除文件bat
        GenerationBat.createBat(map);
        // 执行bat
        InvokeBat invokeBat = new InvokeBat();
        invokeBat.execution(map, "delAllbat");
        msgClient.send(msg, version + "版本文 件删除成功!", 74);
    }
    
    /**
     * 
     * 加密data文件.
     * 
     * @param path
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2015-3-16	SGJ	新建
     * </pre>
     */
    public static void encrypData(QqMessag msgClient, QQMsg msg, String path) throws Exception {
        File file = new File(path);
        if (file.canRead()) {
            if (file.isDirectory()) {
                for (String pathTemp : file.list()) {
                    encrypData(null, null, file + "/" + pathTemp);
                }
            } else {
                String filePath = file.getAbsolutePath();
                if (filePath.endsWith(".xml")) {
                    logger.info("加密文件:" + filePath);
                    FileInputStream fi2 = new FileInputStream(new File(filePath));
                    byte encryptedData[] = new byte[fi2.available()];
                    fi2.read(encryptedData);
                    fi2.close();
                    (new File(filePath)).delete();
                    String newFile = filePath.substring(0, filePath.lastIndexOf(".")) + ".dat";
                    EncrypAndDecrypUtils.encrypDateToFile(new String(encryptedData, "GBK"), newFile);
                }
            }
        }
        systemLock = false;
        if (msgClient != null && msg != null) {
            msgClient.send(msg, "系统数据加密完成!", 74);
        }
    }
    
    /**
     * 
     * 解密data文件.
     * 
     * @param path
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2015-3-16	SGJ	新建
     * </pre>
     */
    public static void decrypData(QqMessag msgClient, QQMsg msg, String path) throws Exception {
        systemLock = true;
        File file = new File(path);
        if (file.canRead()) {
            if (file.isDirectory()) {
                for (String pathTemp : file.list()) {
                    decrypData(null, null, file + "/" + pathTemp);
                }
            } else {
                String filePath = file.getAbsolutePath();
                if (filePath.endsWith(".dat")) {
                    logger.info("解密文件:" + filePath);
                    // 得到解密后的数据
                    byte[] bytes = EncrypAndDecrypUtils.decrypDataToByte(filePath);
                    (new File(filePath)).delete();
                    String newFile = filePath.substring(0, filePath.lastIndexOf(".")) + ".xml";
                    FileOutputStream fo = new FileOutputStream(new File(newFile));
                    fo.write(bytes);
                    fo.close();
                }
            }
        }
        if (msgClient != null && msg != null) {
            msgClient.send(msg, "系统数据解密完成!", 74);
        }
    }
}
