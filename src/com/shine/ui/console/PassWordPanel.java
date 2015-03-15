package com.shine.ui.console;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class PassWordPanel extends JFrame implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = -2186397362206292081L;

    // �ı������
    private JPasswordField pf = new JPasswordField();

    // ��ť
    private JButton bt = new JButton();

    private JFrame f;

    private SystemTray tray;

    private TrayIcon trayIcon;
    
    private volatile static PassWordPanel passwordWin = null;
    
    /**
     * ��ʼ������
     */
    public PassWordPanel() {
        setLayout(null);
        setTitle("����");
        setSize(180, 100);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // ���������
        pf.setBounds(20, 15, 130, 24);
        add(pf);
        bt = new JButton("ȷ����");
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
     * ʵ����.
     * 
     * @return
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-8-15	SGJ	�½�
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
     * ��ʾ����
     * 
     * @param client
     * @param event
     * @throws Exception
     */
    public void show(JFrame j, SystemTray tray, TrayIcon trayIcon) throws Exception {
        this.f = j;
        this.tray = tray;
        this.trayIcon = trayIcon;
        this.setVisible(true);
    }
    
    /**
     * 
     * ������֤.
     * 
     * @throws Exception
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-8-15	SGJ	�½�
     * </pre>
     */
    @SuppressWarnings("deprecation")
    public void checkPassword() throws Exception {
        if (pf.getText() != null && pf.getText().equals("shineosp")) {
            if (f != null) {
                f.setExtendedState(JFrame.NORMAL);
                f.setVisible(true);
                tray.remove(trayIcon);
                pf.setText("");
                this.setVisible(false);
            } else {
                System.exit(0);
                tray.remove(trayIcon);// �˳������Ƴ�ϵͳ���̴���ͼ��
            }
        } else {
            JOptionPane.showMessageDialog(null, "�������!", "��ʾ", JOptionPane.OK_OPTION);
        }
    }
    
    /**
     * ���������¼�
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
