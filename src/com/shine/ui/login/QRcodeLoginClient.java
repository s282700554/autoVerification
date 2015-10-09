package com.shine.ui.login;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class QRcodeLoginClient extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 4738541277817860274L;

    // 二维码
    private JLabel lb = new JLabel();

    /**
     * 初始化窗口
     */
    public QRcodeLoginClient() {
        setLayout(null);
        setTitle("验证码");
        setSize(180, 180);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 放置显示验证码
        lb.setBounds(20, 20, 150, 53);
        add(lb);
    }

    /**
     * 设置验证码
     * 
     * @param icon
     * @throws Exception
     */
    public void setIcon(String icon) throws Exception {
        lb.setIcon(new ImageIcon(icon));
    }

    /**
     * 显示窗口
     */
    public void show () {
        this.setVisible(true);
    }
    
    /**
     * 
     * 关闭窗口
     * 
     * @param e
     * 
     *            <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-8	SGJ	新建
     * </pre>
     */
    public void close() {
        this.setVisible(false);
    }
}
