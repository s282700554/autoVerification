package com.shine.ui.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileLock;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shine.qq.client.QqMessag;
import com.shine.ui.console.ConsoleTextArea;

public class LoginClient extends JFrame implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1773340910651864525L;

    private static Logger logger = LoggerFactory.getLogger(LoginClient.class);

    // 文本输入框
    private JTextField tf = new JTextField();
    // 表单名
    private String[] labelStr = { "用户名:", "密　码:" };
    // 表单名
    private JLabel[] lb = new JLabel[3];
    // 密码框
    private JPasswordField pf;
    // 按钮
    private JButton bt = new JButton();

    FileLock lock = null;

    public LoginClient() {
        setLayout(null);
        setTitle("SHINE自动验包系统");
        setSize(300, 150);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 放置表单名
        for (int i = 0; i < 2; i++) {
            lb[i] = new JLabel(labelStr[i]);
            lb[i].setBounds(32, 20 + 35 * i, 80, 24);
            add(lb[i]);
        }
        // 放置文本输入框
        tf.setBounds(100, 20, 150, 24);
        add(tf);
        // 密码输入框
        pf = new JPasswordField();
        pf.setBounds(100, 55, 150, 24);
        add(pf);
        bt = new JButton("登　录");
        bt.setBounds(66, 90, 180, 24);
        bt.addActionListener(this);
        add(bt);
        tf.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    try {
                        login();
                    } catch (Exception e2) {
                        logger.error(e2.getMessage());
                    }
                }
            }
        });
        pf.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    try {
                        login();
                    } catch (Exception e2) {
                        logger.error(e2.getMessage());
                    }
                }
            }
        });
        bt.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    try {
                        login();
                    } catch (Exception e2) {
                        logger.error(e2.getMessage());
                    }
                }
            }
        });
    }

    /**
	 * 
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            login();
        } catch (Exception e2) {
            logger.error(e2.getMessage());
        }
    }

    /**
     * 
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-8-15	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("deprecation")
    public void login() throws Exception {
        try {
            QqMessag msg = new QqMessag(tf.getText(), pf.getText());
            msg.login();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        this.setVisible(false);
        try {
            ConsoleTextArea consoleTextArea = new ConsoleTextArea();
            consoleTextArea.showLog();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
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
        LoginClient autoLog = new LoginClient();
        if (!autoLog.isRunning()) {
            JOptionPane.showMessageDialog(null, "自动验包系统已运行,请勿多开!", "提示", JOptionPane.OK_OPTION);
        } else {
            autoLog.setVisible(true);
        }
    }
}
