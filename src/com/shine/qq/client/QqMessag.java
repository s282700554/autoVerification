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
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent;
import iqq.im.event.QQNotifyEventArgs;
import iqq.im.event.QQNotifyHandler;
import iqq.im.event.QQNotifyHandlerProxy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.authority.ExecutionTask;
import com.shine.qq.listener.LoginActionListener;
import com.shine.ui.console.ConsoleTextArea;
import com.shine.ui.login.CaptchaPanel;
import com.shine.ui.login.QRcodeShowWind;
import com.shine.utils.SystemInfo;
import com.shine.work.adminWork;

public class QqMessag {

    private static Logger logger = LoggerFactory.getLogger(QqMessag.class);

    // �ͻ���
    QQClient client;

    // ǰһ������ʱ��
    private static long date = 0L;
    QRcodeShowWind qRcodeShowWind;
    static ConsoleTextArea consoleTextArea = ConsoleTextArea.getInstance();
    FileLock lock = null;

    /**
     * ��ʼ��
     * 
     * @param user
     * @param pwd
     */
    public QqMessag() throws Exception {
        client = new WebQQClient(new QQNotifyHandlerProxy(this), new ThreadActorDispatcher());
    }

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
     *         <pre>
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
        switch (msg.getType()) {
            case GROUP_MSG: // Ⱥ��Ϣ
                sendMsg.setGroup(msg.getGroup()); // QQ����UIN
                break;
            case BUDDY_MSG: // ������Ϣ
                sendMsg.setTo(msg.getFrom()); // QQ����UIN
                break;
            case DISCUZ_MSG:
                sendMsg.setDiscuz(msg.getDiscuz()); // QQ����UIN
                break;
            case SESSION_MSG:
                sendMsg.setTo(msg.getFrom()); // QQ����UIN
                break;
            default:
                break;
        }
        // ��������
        sendMsg.setType(msg.getType());
        // QQ����
        sendMsg.addContentItem(new TextItem(msgText)); // ����ı�����
        if (id >= 0) {
            sendMsg.addContentItem(new FaceItem(id)); // QQ idΪ0�ı���
        }
        sendMsg.addContentItem(new FontItem()); // ʹ��Ĭ������
        client.sendMsg(sendMsg, null); // ���ýӿڷ�����Ϣ
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

    /**
     * ��ȡ��ά��.
     * 
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-10-8	SGJ	�½�
     * </pre>
     */
    public void getQRcode() throws Exception {
        // ��ȡ��ά��
        client.getQRcode(new GetQRcodeListener(this));
    }

    public class GetQRcodeListener implements QQActionListener {
        QqMessag qqMessag;

        public GetQRcodeListener(QqMessag qqMessag) {
            this.qqMessag = qqMessag;
        }

        @Override
        public void onActionEvent(QQActionEvent event) {
            if (event.getType() == QQActionEvent.Type.EVT_OK) {
                // ���ɶ�ά��
                try {
                    BufferedImage verify = (BufferedImage) event.getTarget();
                    // ���ɶ�ά��ͼƬ
                    Random random = new Random();
                    int name = random.nextInt();
                    String fileName = String.valueOf(name).substring(1, String.valueOf(name).length()) + ".png";
                    fileName = SystemInfo.getTempPath() + "/" + fileName;
                    ImageIO.write(verify, "png", new File(fileName));
                    logger.warn(verify.toString());
                    logger.warn("����������Ŀ��Ŀ¼��verify.pngͼƬ�������֤��:");
                    logger.warn(fileName);
                    if (qRcodeShowWind != null) {
                        qRcodeShowWind.close();
                    }
                    qRcodeShowWind = new QRcodeShowWind();
                    qRcodeShowWind.setIcon(fileName);
                    qRcodeShowWind.showWind();
                    // ����ά��״̬
                    qqMessag.checkQRCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 
     * ����ά��״̬.
     * 
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-10-8	SGJ	�½�
     * </pre>
     */
    public void checkQRCode() throws Exception {
        client.checkQRCode(new CheckQRcodeListener(this));
    }

    public class CheckQRcodeListener implements QQActionListener {
        QqMessag qqMessag;

        public CheckQRcodeListener(QqMessag qqMessag) {
            this.qqMessag = qqMessag;
        }

        @Override
        public void onActionEvent(QQActionEvent event) {
            logger.warn("checkQRCode " + event);
            switch (event.getType()) {
                case EVT_OK:
                    qRcodeShowWind.close();
                    // ��½�ɹ�,��ʼ��ȡqq��Ϣ,��������Ϣ����.
                    new LoginActionListener(client).onActionEvent(event);
                    break;
                case EVT_ERROR:
                    QQException ex = (QQException) (event.getTarget());
                    QQException.QQErrorCode code = ex.getError();
                    switch (code) {
                        case QRCODE_INVALID:
                            logger.warn("��ά��ʧЧ");
                            // ��ά��ʧЧ,�رն�ά����ʾ���ڣ������»�ȡ
                            qRcodeShowWind.close();
                            try {
                                this.qqMessag.getQRcode();
                            } catch (Exception e1) {
                                logger.error(e1.getMessage());
                                e1.printStackTrace();
                            }
                            break;
                        case QRCODE_OK:
                            // ��ά����Ч,�ȴ��û�ɨ��
                            logger.warn("��ά��δʧЧ");
                        case QRCODE_AUTH:
                            // ��ά���Ѿ�ɨ��,���û������¼
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // ��������ά��״̬
                            try {
                                this.qqMessag.checkQRCode();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    break;
            }
        }
    }

    // �жϸ�Ӧ���Ƿ�������
    public boolean isRunning() {
        try {
            // ���ʵ����־�ļ�
            File flagFile = new File("instance");
            // ��������ھ��½�һ��
            if (!flagFile.exists())
                flagFile.createNewFile();
            // ����ļ���
            lock = new FileOutputStream("instance").getChannel().tryLock();
            // ���ؿձ�ʾ�ļ��ѱ����е�ʵ������
            if (lock == null)
                return false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        QqMessag qqMessag = new QqMessag();
        if (!qqMessag.isRunning()) {
            JOptionPane.showMessageDialog(null, "�Զ����ϵͳ������,����࿪!", "��ʾ", JOptionPane.OK_OPTION);
        } else {
            // ������Ϣ��ش���.
            try {
                consoleTextArea.showLog();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            // ��ȡ��ά�벢��ʾ
            qqMessag.getQRcode();
        }

    }
}
