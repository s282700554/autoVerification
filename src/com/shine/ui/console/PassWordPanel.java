package com.shine.ui.console;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.shine.operation.xml.BasicsConfigXmlOper;

public class PassWordPanel extends JFrame implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = -2186397362206292081L;

    // 文本输入框
    private JPasswordField pf = new JPasswordField();

    // 按钮
    private JButton bt = new JButton();

    private JFrame f;

    private SystemTray tray;

    private TrayIcon trayIcon;

    private String password;

    private volatile static PassWordPanel passwordWin = null;

    /**
     * 初始化窗口
     */
    public PassWordPanel() {
        setLayout(null);
        setTitle("密码");
        setSize(180, 100);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // 放置输入框
        pf.setBounds(20, 15, 130, 24);
        add(pf);
        bt = new JButton("确　定");
        bt.setBounds(47, 42, 80, 24);
        add(bt);
        bt.addActionListener(this);
        pf.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    try {
                        checkPassword();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        bt.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    try {
                        checkPassword();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 
     * 实例化.
     * 
     * @return
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2014-8-15	SGJ	新建
     * </pre>
     */
    public static PassWordPanel getInstance() {
        synchronized (PassWordPanel.class) {
            if (passwordWin == null) {
                passwordWin = new PassWordPanel();
            }
        }
        return passwordWin;
    }

    /**
     * 显示窗口
     * 
     * @param client
     * @param event
     * @throws Exception
     */
    public void show(JFrame j, SystemTray tray, TrayIcon trayIcon) throws Exception {
        this.f = j;
        this.tray = tray;
        this.trayIcon = trayIcon;
        Map<String, String> winControl = BasicsConfigXmlOper.getBasicConfigInfo("winControl");
        if ("true".equals(winControl.get("WIN_HASPASW"))) {
            this.password = winControl.get("WIN_PASSWORD");
            this.setVisible(true);
        } else {
            this.operationWin();
        }
    }

    /**
     * 
     * 密码验证.
     * 
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-8-15	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("deprecation")
    public void checkPassword() throws Exception {
        if (pf.getText() != null && pf.getText().equals(this.password)) {
            this.operationWin();
        } else {
            JOptionPane.showMessageDialog(null, "密码错误!", "提示", JOptionPane.OK_OPTION);
        }
    }

    /**
     * 
     * 操作窗口.
     * 
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2015-6-18	SGJ	新建
     * </pre>
     */
    public void operationWin() {
        try {
            if (f != null) {
                f.setExtendedState(JFrame.NORMAL);
                f.setVisible(true);
                tray.remove(trayIcon);
                pf.setText("");
                this.setVisible(false);
            } else {
                System.exit(0);
                tray.remove(trayIcon);// 退出程序，移出系统托盘处的图标
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动作监听事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            checkPassword();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
