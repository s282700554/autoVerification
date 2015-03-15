package com.shine.ui.login;

import iqq.im.QQClient;
import iqq.im.event.QQNotifyEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CaptchaPanel extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4738541277817860274L;
		// 文本输入框
		private JTextField tf = new JTextField();
		// 表单名
		private JLabel lb = new JLabel();
		// 按钮
		private JButton bt = new JButton();
		
		private QQClient client;
		
		private QQNotifyEvent event;
		
		/**
		 * 初始化窗口
		 */
		public CaptchaPanel() {
			setLayout(null);
			setTitle("验证码");
			setSize(180, 180);
			setResizable(false);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// 放置显示验证码
			lb.setBounds(20, 20, 150, 53);
			add(lb);
			// 放置输入框
			tf.setBounds(20, 80, 130, 24);
			add(tf);
			bt = new JButton("确　定");
			bt.setBounds(47, 110, 80, 24);
			add(bt);
			bt.addActionListener(this);
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
		 * 
		 * @param client
		 * @param event
		 * @throws Exception
		 */
		public void show(QQClient client, QQNotifyEvent event)throws Exception {
			this.client = client;
			this.event = event;
			this.setVisible(true);
		}
		/**
		 * 动作监听事件
		 */
		@Override
		public void actionPerformed(ActionEvent e){
			this.client.submitVerify(tf.getText(), event);
			this.setVisible(false);
		}
}
