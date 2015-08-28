package com.shine.qq.client;

import iqq.im.QQActionListener;
import iqq.im.QQClient;
import iqq.im.QQException;
import iqq.im.WebQQClient;
import iqq.im.actor.ThreadActorDispatcher;
import iqq.im.bean.QQMsg;
import iqq.im.bean.QQStatus;
import iqq.im.bean.content.FaceItem;
import iqq.im.bean.content.FontItem;
import iqq.im.bean.content.TextItem;
import iqq.im.core.QQConstants;
import iqq.im.event.QQNotifyEvent;
import iqq.im.event.QQNotifyEventArgs;
import iqq.im.event.QQNotifyHandler;
import iqq.im.event.QQNotifyHandlerProxy;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.authority.ExecutionTask;
import com.shine.qq.listener.LoginActionListener;
import com.shine.ui.login.CaptchaPanel;
import com.shine.utils.SystemInfo;
import com.shine.work.adminWork;

public class QqMessag {

    private static Logger logger = LoggerFactory.getLogger(QqMessag.class);

    // �ͻ���
    QQClient client;

    // ǰһ������ʱ��
    private static long date = 0L;

    /**
     * ��ʼ��
     * 
     * @param user
     * @param pwd
     */
    public QqMessag(String user, String pwd) throws Exception {
        client = new WebQQClient(user, pwd, new QQNotifyHandlerProxy(this), new ThreadActorDispatcher());
    }

    /**
     * 
     * ���������Ϣ.
     * 
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-5-26	SGJ	�½�
     * </pre>
     */
    @QQNotifyHandler(QQNotifyEvent.Type.NET_ERROR)
    public void reLoggin() throws Exception {
        logger.warn("�������,���µ�½!");
        client.relogin(QQStatus.ONLINE, new LoginActionListener(client));
    }

    /**
     * 
     * ������Ϣ֪ͨ��ʹ�����ע������յ�QQ��Ϣ
     * 
     * ���յ���ϢȻ����װ��Ϣ���ͻ�ȥ
     * 
     * @param event
     * @throws QQException
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-5-26	SGJ	�½�
     * </pre>
     */
    @QQNotifyHandler(QQNotifyEvent.Type.CHAT_MSG)
    public void processBuddyMsg(QQNotifyEvent event) throws QQException {
        QQMsg msg = (QQMsg) event.getTarget();
        logger.warn("[��Ϣ] " + msg.getFrom().getNickname() + "˵:" + msg.getText());
        logger.warn("\n");
        String message = msg.getText();
        if (checkCommand(msg, message)) {
            try {
                ExecutionTask.verifyAuthority(this, msg);
            } catch (Exception e) {
                try {
                    send(msg, "ִ������ʧ��:" + e.getMessage(), 5);
                } catch (Exception e1) {
                    logger.warn(e1.getMessage());
                }
                logger.warn(e.getMessage());
            }
        }
    }

    /**
     * 
     * ���ϵͳ�Ƿ�ɲ���.
     * 
     * @param msg
     * @param message
     * @return
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-6-17	SGJ	�½�
     * </pre>
     */
    private boolean checkCommand(QQMsg msg, String message) {
        if (message.startsWith("@")) {
            long newDate = new Date().getTime();
            if ((newDate - date) > 5000) {
                date = new Date().getTime();
                if (!message.startsWith("@�������") && adminWork.systemLock) {
                    try {
                        send(msg, "ϵͳ����,�����!", 5);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * �������Ϣ.
     * 
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-5-26	SGJ	�½�
     * </pre>
     */
    public void send(QQMsg msg, String msgText, int id) throws Exception {
        // ��װQQ��Ϣ���ͻ�ȥ
        QQMsg sendMsg = new QQMsg();
        // Ⱥ��Ϣ
        if (QQMsg.Type.GROUP_MSG.equals(msg.getType())) {
            sendMsg.setGroup(msg.getGroup()); // QQ����UIN
            sendMsg.setType(QQMsg.Type.GROUP_MSG); // ��������Ϊ����
            // QQ����
            sendMsg.addContentItem(new TextItem(msgText)); // ����ı�����
            if (id >=0 ) {
                sendMsg.addContentItem(new FaceItem(id)); // QQ idΪ0�ı���
            }
            sendMsg.addContentItem(new FontItem()); // ʹ��Ĭ������
        }
        // ������Ϣ
        else if (QQMsg.Type.BUDDY_MSG.equals(msg.getType())) {
            sendMsg.setTo(msg.getFrom()); // QQ����UIN
            sendMsg.setType(QQMsg.Type.BUDDY_MSG); // ��������Ϊ����
            // QQ����
            sendMsg.addContentItem(new TextItem(msgText)); // ����ı�����
            if (id >= 0) {
                sendMsg.addContentItem(new FaceItem(id)); // QQ idΪ0�ı���
            }
            sendMsg.addContentItem(new FontItem()); // ʹ��Ĭ������
        }
        client.sendMsg(sendMsg, null); // ���ýӿڷ�����Ϣ
    }

    /**
     * 
     * �����Զ�����Ϣ.
     * 
     * @param sendMsg
     * @throws Exception
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-10	SGJ	�½�
     * </pre>
     */
    public void sendMsg(QQMsg sendMsg) throws Exception {
        client.sendMsg(sendMsg, null);
    }
    
    /**
     * ��Ҫ��֤��֪ͨ
     * 
     * @param event
     * @throws IOException
     */
    @QQNotifyHandler(QQNotifyEvent.Type.CAPACHA_VERIFY)
    protected void processVerify(QQNotifyEvent event) throws Exception {
        QQNotifyEventArgs.ImageVerify verify = (QQNotifyEventArgs.ImageVerify) event.getTarget();
        Random random = new Random();
        int name = random.nextInt();
        String fileName = String.valueOf(name).substring(1, String.valueOf(name).length()) + ".png";
        fileName = SystemInfo.getTempPath() + "/" + fileName;
        ImageIO.write(verify.image, "png", new File(fileName));
        logger.warn(verify.reason);
        logger.warn("����������Ŀ��Ŀ¼��verify.pngͼƬ�������֤��:");
        logger.warn(fileName);
        CaptchaPanel capachaVerify = CaptchaPanel.getInstance();
        capachaVerify.setIcon(fileName);
        capachaVerify.show(client, event);
    }

    /**
     * 
     * ��ѯ������Ϣ�û���Ϣ.
     * 
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-6-6	SGJ	�½�
     * </pre>
     */
    public void getQqUser(QQMsg msg, QQActionListener qQActionListener) throws Exception {
        client.getUserQQ(msg.getFrom(), qQActionListener);
    }

    /**
     * 
     * ��½
     * 
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-5-26	SGJ	�½�
     * </pre>
     */
    public void login() throws Exception {
        String ua = "Mozilla/5.0 (@os.name; @os.version; @os.arch) AppleWebKit/537.36 (KHTML, like Gecko) @appName Safari/537.36";
        ua = ua.replaceAll("@appName", QQConstants.USER_AGENT);
        ua = ua.replaceAll("@os.name", System.getProperty("os.name"));
        ua = ua.replaceAll("@os.version", System.getProperty("os.version"));
        ua = ua.replaceAll("@os.arch", System.getProperty("os.arch"));
        client.setHttpUserAgent(ua);
        client.login(QQStatus.ONLINE, new LoginActionListener(client));
    }
    public static void main(String[] args) throws Exception {
        QqMessag qqMessag = new QqMessag("2760866691", "shine1234");
        qqMessag.login();
    }
}
