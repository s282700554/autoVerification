package com.shine.ui.login;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class QRcodeLoginClient extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 4738541277817860274L;

    // ��ά��
    private JLabel lb = new JLabel();

    /**
     * ��ʼ������
     */
    public QRcodeLoginClient() {
        setLayout(null);
        setTitle("��֤��");
        setSize(180, 180);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // ������ʾ��֤��
        lb.setBounds(20, 20, 150, 53);
        add(lb);
    }

    /**
     * ������֤��
     * 
     * @param icon
     * @throws Exception
     */
    public void setIcon(String icon) throws Exception {
        lb.setIcon(new ImageIcon(icon));
    }

    /**
     * ��ʾ����
     */
    public void show () {
        this.setVisible(true);
    }
    
    /**
     * 
     * �رմ���
     * 
     * @param e
     * 
     *            <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-10-8	SGJ	�½�
     * </pre>
     */
    public void close() {
        this.setVisible(false);
    }
}
