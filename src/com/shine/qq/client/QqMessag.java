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

    // 客户端
    QQClient client;

    // 前一个命令时间
    private static long date = 0L;

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
     * <pre>
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
        // 群消息
        if (QQMsg.Type.GROUP_MSG.equals(msg.getType())) {
            sendMsg.setGroup(msg.getGroup()); // QQ好友UIN
            sendMsg.setType(QQMsg.Type.GROUP_MSG); // 发送类型为好友
            // QQ内容
            sendMsg.addContentItem(new TextItem(msgText)); // 添加文本内容
            if (id >=0 ) {
                sendMsg.addContentItem(new FaceItem(id)); // QQ id为0的表情
            }
            sendMsg.addContentItem(new FontItem()); // 使用默认字体
        }
        // 好友消息
        else if (QQMsg.Type.BUDDY_MSG.equals(msg.getType())) {
            sendMsg.setTo(msg.getFrom()); // QQ好友UIN
            sendMsg.setType(QQMsg.Type.BUDDY_MSG); // 发送类型为好友
            // QQ内容
            sendMsg.addContentItem(new TextItem(msgText)); // 添加文本内容
            if (id >= 0) {
                sendMsg.addContentItem(new FaceItem(id)); // QQ id为0的表情
            }
            sendMsg.addContentItem(new FontItem()); // 使用默认字体
        }
        client.sendMsg(sendMsg, null); // 调用接口发送消息
    }

    /**
     * 
     * 发送自定义消息.
     * 
     * @param sendMsg
     * @throws Exception
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-10	SGJ	新建
     * </pre>
     */
    public void sendMsg(QQMsg sendMsg) throws Exception {
        client.sendMsg(sendMsg, null);
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
    public static void main(String[] args) throws Exception {
        QqMessag qqMessag = new QqMessag("2760866691", "shine1234");
        qqMessag.login();
    }
}
