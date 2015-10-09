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
     * ִ�л�ԭ��������.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-6-6 SGJ �½�
     * </pre>
     */
    @Override
    public void executCommand(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        String oper = userMsg.substring(3, 5).trim();
        if ("��־".equals(oper)) {
            showLog(msgClient, msg);
        } else if ("�û�".equals(oper)) {
            operatingUser(msgClient, msg);
        } else if ("����".equals(oper)) {
            operatingMode(msgClient, msg);
        } else if ("����".equals(oper)) {
            TaskUtils.sendPer("���������Ŷ!");
        } else if ("��Ϣ".equals(oper)) {
            sendMsg(msgClient, msg);
        } else if ("����".equals(oper)) {
            controlOtherWork(msgClient, msg);
        } else if ("ɾ��".equals(oper)) {
            delFileByVersion(msgClient, msg);
        } else if ("Ⱥ��".equals(oper)) {
            setGroup(msgClient, msg);
        } else if ("����".equals(oper)) {
            encrypData(msgClient, msg, "data");
        } else if ("����".equals(oper)) {
            decrypData(msgClient, msg, "data");
        }
    }

    /**
     * 
     * ��ʾ��־.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-8	SGJ	�½�
     * </pre>
     */
    public static void showLog(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        String date = userMsg.substring(5, userMsg.length()).trim();
        // ��ʾ��־
        logger.warn(date + " ��־");
        String log = PackageLogXmlOper.getLogByDate(date);
        msgClient.send(msg, log, 74);
    }

    /**
     * 
     * �����û�.
     * 
     * @�����û���ѯ�Ϲٽ� controlXmlUtils.getControlByUserName("�Ϲٽ�");
     * @�����û����282700554:�Ϲٽ�:0 controlXmlUtils.addXMLNode("282700554", "�Ϲٽ�", "0");
     * @�����û�ɾ���Ϲٽ� controlXmlUtils.delXMLNodeByUserName("�Ϲٽ�");
     * @�����û��޸��Ϲٽ�:0 controlXmlUtils.updateXMLNodeByUserName("�Ϲٽ�", "0");
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-8	SGJ	�½�
     * </pre>
     */
    public static void operatingUser(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        // ���282700554:�Ϲٽ�:0
        String userOperating = userMsg.substring(5, userMsg.length()).trim();
        // ���
        String operating = userOperating.substring(0, 2);
        // 282700554:�Ϲٽ�:0
        String userInfo = userOperating.substring(2, userOperating.length());
        if ("��ѯ".equals(operating)) {
            String userLevel = AuthorityControlXmlOper.getControlByUserName(userInfo);
            msgClient.send(msg, userLevel, 74);
        } else if ("���".equals(operating)) {
            String[] userInfoSet = userInfo.split(":");
            String userQq = userInfoSet[0].trim();
            String userName = userInfoSet[1].trim();
            String userLevel = userInfoSet[2].trim();
            if (StringUtils.isNotBlank(userQq) && StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(userLevel)) {
                AuthorityControlXmlOper.addXMLNode(userQq, userName, userLevel);
                msgClient.send(msg, "qq:" + userQq + ",����:" + userName + ",����:" + userLevel + "��ӳɹ�!", 74);
            }
        } else if ("ɾ��".equals(operating)) {
            AuthorityControlXmlOper.delXMLNodeByUserName(userInfo);
            msgClient.send(msg, userInfo + "ɾ���ɹ�!", 74);
        } else if ("�޸�".equals(operating)) {
            String[] userInfoSet = userInfo.split(":");
            String userName = userInfoSet[0].trim();
            String userLevel = userInfoSet[1].trim();
            if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(userLevel)) {
                AuthorityControlXmlOper.updateXMLNodeByUserName(userName, userLevel);
                msgClient.send(msg, "����:" + userName + ",����ɹ��޸�Ϊ:" + userLevel, 74);
            }
        }
    }

    /**
     * 
     * 
     * @�����ܲ�ѯ��� controlXmlUtils.getModeInfoModeName("���");
     * @������������2 controlXmlUtils.addModeXMLNode("���", "2");
     * @������ɾ����� controlXmlUtils.delModeXMLNode("���");
     * @�������޸����2 controlXmlUtils.updateModeXMLNode("���", "2");
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-8	SGJ	�½�
     * </pre>
     */
    public static void operatingMode(QqMessag msgClient, QQMsg msg) throws Exception {
        String userMsg = msg.getText().trim();
        // ������:2
        String modeOperating = userMsg.substring(5, userMsg.length()).trim();
        // ���
        String operating = modeOperating.substring(0, 2);
        // ���:2
        String modeInfo = modeOperating.substring(2, modeOperating.length());
        if ("��ѯ".equals(operating)) {
            String modeLevel = AuthorityControlXmlOper.getModeInfoModeName(modeInfo);
            msgClient.send(msg, modeLevel, 74);
        } else if ("���".equals(operating)) {
            String[] modeInfoSet = modeInfo.split(":");
            String modeName = modeInfoSet[0].trim();
            String modeLevel = modeInfoSet[1].trim();
            if (StringUtils.isNotBlank(modeName) && StringUtils.isNotBlank(modeLevel)) {
                AuthorityControlXmlOper.addModeXMLNode(modeName, modeLevel);
                msgClient.send(msg, "ģ��:" + modeName + ",����:" + modeLevel + "��ӳɹ�!", 74);
            }
        } else if ("ɾ��".equals(operating)) {
            AuthorityControlXmlOper.delModeXMLNode(modeInfo);
            msgClient.send(msg, modeInfo + "ɾ���ɹ�!", 74);
        } else if ("�޸�".equals(operating)) {
            String[] modeInfoSet = modeInfo.split(":");
            String modeName = modeInfoSet[0].trim();
            String modeLevel = modeInfoSet[1].trim();
            if (StringUtils.isNotBlank(modeName) && StringUtils.isNotBlank(modeLevel)) {
                AuthorityControlXmlOper.updateModeXMLNode(modeName, modeLevel);
                msgClient.send(msg, "ģ��:" + modeName + ",����ɹ��޸�Ϊ:" + modeLevel, 74);
            }
        }
    }

    /**
     * 
     * ��Ⱥ�﷢����Ϣ.
     * 
     * @param msg
     * @param face
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-9	SGJ	�½�
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
        // ��װQQ��Ϣ���ͻ�ȥ
        QQMsg qqMsg = new QQMsg();
        qqMsg.setGroup(StartTimerTask.g); // QQ����UIN
        qqMsg.setType(QQMsg.Type.GROUP_MSG); // ��������Ϊ����
        msgClient.send(qqMsg, sendInfo[0], faceId);
    }

    /**
     * 
     * �ı䷢��Ϣ��Ⱥ.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-8-8	SGJ	�½�
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
        msgClient.send(msg, "Ⱥ����Ϊ[" + groupName + "]�ɹ�!", 74);
    }

    /**
     * 
     * �������칦�ܿ���.
     * 
     * @�������쿪
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-11	SGJ	�½�
     * </pre>
     */
    public static void controlOtherWork(QqMessag msgClient, QQMsg msg) throws Exception {
        String msgInfo = msg.getText().trim().substring(5, msg.getText().trim().length());
        if ("��".equals(msgInfo)) {
            workFactory.isOn = true;
        } else if ("��".equals(msgInfo)) {
            workFactory.isOn = false;
        } else if ("���翪".equals(msgInfo)) {
            workFactory.isInternet = true;
        } else if ("�����".equals(msgInfo)) {
            workFactory.isInternet = false;
        }
        msgClient.send(msg, "���칦��" + workFactory.isOn, 74);
    }

    /**
     * 
     * ɾ�������ļ�.
     * 
     * @param msgClient
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-13	SGJ	�½�
     * </pre>
     */
    public static void delFileByVersion(QqMessag msgClient, QQMsg msg) throws Exception {
        String version = msg.getText().trim().substring(5, msg.getText().trim().length());
        Map<String, String> map = JavaConfigXmlOper.getVersionInfo(version);
        map.put("EXECUTION_STEP", "delAllbat");
        // ɾ���ļ�bat
        GenerationBat.createBat(map);
        // ִ��bat
        InvokeBat invokeBat = new InvokeBat();
        invokeBat.execution(map, "delAllbat");
        msgClient.send(msg, version + "�汾�� ��ɾ���ɹ�!", 74);
    }
    
    /**
     * 
     * ����data�ļ�.
     * 
     * @param path
     * @throws Exception
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-3-16	SGJ	�½�
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
                    logger.info("�����ļ�:" + filePath);
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
            msgClient.send(msg, "ϵͳ���ݼ������!", 74);
        }
    }
    
    /**
     * 
     * ����data�ļ�.
     * 
     * @param path
     * @throws Exception
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-3-16	SGJ	�½�
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
                    logger.info("�����ļ�:" + filePath);
                    // �õ����ܺ������
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
            msgClient.send(msg, "ϵͳ���ݽ������!", 74);
        }
    }
}
