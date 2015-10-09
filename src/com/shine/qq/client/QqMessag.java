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

    // 客户端
    QQClient client;

    // 前一个命令时间
    private static long date = 0L;
    QRcodeShowWind qRcodeShowWind;
    static ConsoleTextArea consoleTextArea = ConsoleTextArea.getInstance();
    FileLock lock = null;

    /**
     * 初始化
     * 
     * @param user
     * @param pwd
     */
    public QqMessag() throws Exception {
        client = new WebQQClient(new QQNotifyHandlerProxy(this), new ThreadActorDispatcher());
    }

    /**
     * 初始化
     * 
     * @param user
     * @param pwd
     */
    public QqMessag(String user, String pwd) throws Exception {
        client = new WebQQClient(user, pwd, new QQNotifyHandlerProxy(this), new ThreadActorDispatcher());
    }

    /**
     * 
     * 网络错误信息.
     * 
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-26	SGJ	新建
     * </pre>
     */
    @QQNotifyHandler(QQNotifyEvent.Type.NET_ERROR)
    public void reLoggin() throws Exception {
        logger.warn("网络出错,重新登陆!");
        client.relogin(QQStatus.ONLINE, new LoginActionListener(client));
    }

    /**
     * 
     * 聊天消息通知，使用这个注解可以收到QQ消息
     * 
     * 接收到消息然后组装消息发送回去
     * 
     * @param event
     * @throws QQException
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-26	SGJ	新建
     * </pre>
     */
    @QQNotifyHandler(QQNotifyEvent.Type.CHAT_MSG)
    public void processBuddyMsg(QQNotifyEvent event) throws QQException {
        QQMsg msg = (QQMsg) event.getTarget();
        logger.warn("[消息] " + msg.getFrom().getNickname() + "说:" + msg.getText());
        logger.warn("\n");
        String message = msg.getText();
        if (checkCommand(msg, message)) {
            try {
                ExecutionTask.verifyAuthority(this, msg);
            } catch (Exception e) {
                try {
                    send(msg, "执行命令失败:" + e.getMessage(), 5);
                } catch (Exception e1) {
                    logger.warn(e1.getMessage());
                }
                logger.warn(e.getMessage());
            }
        }
    }

    /**
     * 
     * 检测系统是否可操作.
     * 
     * @param msg
     * @param message
     * @return
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2015-6-17	SGJ	新建
     * </pre>
     */
    private boolean checkCommand(QQMsg msg, String message) {
        if (message.startsWith("@")) {
            long newDate = new Date().getTime();
            if ((newDate - date) > 5000) {
                date = new Date().getTime();
                if (!message.startsWith("@管理加密") && adminWork.systemLock) {
                    try {
                        send(msg, "系统已锁,请解锁!", 5);
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
     * 命令返回信息.
     * 
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-26	SGJ	新建
     * </pre>
     */
    public void send(QQMsg msg, String msgText, int id) throws Exception {
        // 组装QQ消息发送回去
        QQMsg sendMsg = new QQMsg();
        switch (msg.getType()) {
            case GROUP_MSG: // 群消息
                sendMsg.setGroup(msg.getGroup()); // QQ好友UIN
                break;
            case BUDDY_MSG: // 好友消息
                sendMsg.setTo(msg.getFrom()); // QQ好友UIN
                break;
            case DISCUZ_MSG:
                sendMsg.setDiscuz(msg.getDiscuz()); // QQ好友UIN
                break;
            case SESSION_MSG:
                sendMsg.setTo(msg.getFrom()); // QQ好友UIN
                break;
            default:
                break;
        }
        // 发送类型
        sendMsg.setType(msg.getType());
        // QQ内容
        sendMsg.addContentItem(new TextItem(msgText)); // 添加文本内容
        if (id >= 0) {
            sendMsg.addContentItem(new FaceItem(id)); // QQ id为0的表情
        }
        sendMsg.addContentItem(new FontItem()); // 使用默认字体
        client.sendMsg(sendMsg, null); // 调用接口发送消息
    }

    /**
     * 需要验证码通知
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
        logger.warn("请输入在项目根目录下verify.png图片里面的验证码:");
        logger.warn(fileName);
        CaptchaPanel capachaVerify = CaptchaPanel.getInstance();
        capachaVerify.setIcon(fileName);
        capachaVerify.show(client, event);
    }

    /**
     * 
     * 查询发送消息用户信息.
     * 
     * @param msg
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-6	SGJ	新建
     * </pre>
     */
    public void getQqUser(QQMsg msg, QQActionListener qQActionListener) throws Exception {
        client.getUserQQ(msg.getFrom(), qQActionListener);
    }

    /**
     * 
     * 登陆
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-26	SGJ	新建
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
     * 获取二维码.
     * 
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-8	SGJ	新建
     * </pre>
     */
    public void getQRcode() throws Exception {
        // 获取二维码
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
                // 生成二维码
                try {
                    BufferedImage verify = (BufferedImage) event.getTarget();
                    // 生成二维码图片
                    Random random = new Random();
                    int name = random.nextInt();
                    String fileName = String.valueOf(name).substring(1, String.valueOf(name).length()) + ".png";
                    fileName = SystemInfo.getTempPath() + "/" + fileName;
                    ImageIO.write(verify, "png", new File(fileName));
                    logger.warn(verify.toString());
                    logger.warn("请输入在项目根目录下verify.png图片里面的验证码:");
                    logger.warn(fileName);
                    if (qRcodeShowWind != null) {
                        qRcodeShowWind.close();
                    }
                    qRcodeShowWind = new QRcodeShowWind();
                    qRcodeShowWind.setIcon(fileName);
                    qRcodeShowWind.showWind();
                    // 检查二维码状态
                    qqMessag.checkQRCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 
     * 检查二维码状态.
     * 
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-8	SGJ	新建
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
                    // 登陆成功,开始获取qq信息,并启动消息提醒.
                    new LoginActionListener(client).onActionEvent(event);
                    break;
                case EVT_ERROR:
                    QQException ex = (QQException) (event.getTarget());
                    QQException.QQErrorCode code = ex.getError();
                    switch (code) {
                        case QRCODE_INVALID:
                            logger.warn("二维码失效");
                            // 二维码失效,关闭二维码显示窗口，并重新获取
                            qRcodeShowWind.close();
                            try {
                                this.qqMessag.getQRcode();
                            } catch (Exception e1) {
                                logger.error(e1.getMessage());
                                e1.printStackTrace();
                            }
                            break;
                        case QRCODE_OK:
                            // 二维码有效,等待用户扫描
                            logger.warn("二维码未失效");
                        case QRCODE_AUTH:
                            // 二维码已经扫描,等用户允许登录
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // 继续检查二维码状态
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

    // 判断该应用是否已启动
    public boolean isRunning() {
        try {
            // 获得实例标志文件
            File flagFile = new File("instance");
            // 如果不存在就新建一个
            if (!flagFile.exists())
                flagFile.createNewFile();
            // 获得文件锁
            lock = new FileOutputStream("instance").getChannel().tryLock();
            // 返回空表示文件已被运行的实例锁定
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
            JOptionPane.showMessageDialog(null, "自动验包系统已运行,请勿多开!", "提示", JOptionPane.OK_OPTION);
        } else {
            // 启动信息监控窗口.
            try {
                consoleTextArea.showLog();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            // 获取二维码并显示
            qqMessag.getQRcode();
        }

    }
}
