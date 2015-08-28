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

import com.shine.ui.console.PassWordPanel;

public class CaptchaPanel extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4738541277817860274L;
		// �ı������
		private JTextField tf = new JTextField();
		// ����
		private JLabel lb = new JLabel();
		// ��ť
		private JButton bt = new JButton();
		
		private QQClient client;
		
		private QQNotifyEvent event;
		
		private volatile static CaptchaPanel captchaPanel = null;
		
		/**
	     * 
	     * ʵ����.
	     * 
	     * @return
	     * 
	     *         <pre>
	     * �޸�����     �޸��� �޸�ԭ��
	     * 2014-8-15    SGJ �½�
	     * </pre>
	     */
	    public static CaptchaPanel getInstance() {
	        synchronized (PassWordPanel.class) {
	            if (captchaPanel == null) {
	                captchaPanel = new CaptchaPanel();
	            } else {
	                captchaPanel.setVisible(false);
	            }
	        }
	        return captchaPanel;
	    }
	    
		/**
		 * ��ʼ������
		 */
		public CaptchaPanel() {
			setLayout(null);
			setTitle("��֤��");
			setSize(180, 180);
			setResizable(false);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// ������ʾ��֤��
			lb.setBounds(20, 20, 150, 53);
			add(lb);
			// ���������
			tf.setBounds(20, 80, 130, 24);
			add(tf);
			bt = new JButton("ȷ����");
			bt.setBounds(47, 110, 80, 24);
			add(bt);
			bt.addActionListener(this);
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
		 * ���������¼�
		 */
		@Override
		public void actionPerformed(ActionEvent e){
			this.client.submitVerify(tf.getText(), event);
			this.setVisible(false);
		}
}
