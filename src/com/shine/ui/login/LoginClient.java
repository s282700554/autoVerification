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

    // �ı������
    private JTextField tf = new JTextField();
    // ����
    private String[] labelStr = { "�û���:", "�ܡ���:" };
    // ����
    private JLabel[] lb = new JLabel[3];
    // �����
    private JPasswordField pf;
    // ��ť
    private JButton bt = new JButton();

    FileLock lock = null;

    public LoginClient() {
        setLayout(null);
        setTitle("SHINE�Զ����ϵͳ");
        setSize(300, 150);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // ���ñ���
        for (int i = 0; i < 2; i++) {
            lb[i] = new JLabel(labelStr[i]);
            lb[i].setBounds(32, 20 + 35 * i, 80, 24);
            add(lb[i]);
        }
        // �����ı������
        tf.setBounds(100, 20, 150, 24);
        add(tf);
        // ���������
        pf = new JPasswordField();
        pf.setBounds(100, 55, 150, 24);
        add(pf);
        bt = new JButton("�ǡ�¼");
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
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-8-15	SGJ	�½�
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
        LoginClient autoLog = new LoginClient();
        if (!autoLog.isRunning()) {
            JOptionPane.showMessageDialog(null, "�Զ����ϵͳ������,����࿪!", "��ʾ", JOptionPane.OK_OPTION);
        } else {
            autoLog.setVisible(true);
        }
    }
}
