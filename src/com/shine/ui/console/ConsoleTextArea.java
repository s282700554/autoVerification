package com.shine.ui.console;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleTextArea extends JTextArea {
    /**
	 * 
	 */
    private static final long serialVersionUID = -1766674925258825865L;

    private static Logger logger = LoggerFactory.getLogger(ConsoleTextArea.class);
    
    private volatile static ConsoleTextArea consoleTextArea = null;
    
    TrayIcon trayIcon;// ����ͼ�꣬������Image���͵� Ŷ
    SystemTray tray;// ϵͳ����
    Image img = (new ImageIcon("data/icon.png")).getImage();// ����ͼ�꣬����ʹ�ý�С��ͼƬ

    public ConsoleTextArea(InputStream[] inStreams) throws Exception {
        for (int i = 0; i < inStreams.length; ++i) {
            startConsoleReaderThread(inStreams[i]);
        }
    }
    
    /**
     * 
     * ʵ����.
     * 
     * @return
     *
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-10-9	SGJ	�½�
     * </pre>
     */
    public static ConsoleTextArea getInstance() {
        synchronized (ConsoleTextArea.class) {
            if (consoleTextArea == null) {
                consoleTextArea = new ConsoleTextArea();
            }
        }
        return consoleTextArea;
    }
    
    public ConsoleTextArea() {
        try {
            final LoopedStreams ls = new LoopedStreams();
            // �ض���System.out��System.err
            PrintStream ps = new PrintStream(ls.getOutputStream());
            System.setOut(ps);
            System.setErr(ps);
            startConsoleReaderThread(ls.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startConsoleReaderThread(InputStream inStream) throws Exception {
        final BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
        new Thread(new Runnable() {
            public void run() {
                StringBuffer sb = new StringBuffer();
                try {
                    String s;
                    Document doc = getDocument();
                    while ((s = br.readLine()) != null) {
                        boolean caretAtEnd = false;
                        caretAtEnd = getCaretPosition() == doc.getLength() ? true : false;
                        sb.setLength(0);
                        append(sb.append(s).append('\n').toString());
                        if (caretAtEnd) {
                            setCaretPosition(doc.getLength());
                        }
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "��BufferedReader��ȡ����" + e);
                    System.exit(1);
                }
            }
        }).start();
    }

    /**
     * ����״̬��ʾ����.
     * 
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-5-26	SGJ	�½�
     * </pre>
     */
    public void showLog() throws Exception {
        consoleTextArea.setFont(java.awt.Font.decode("monospaced"));
        // ��������
        final JFrame f = new JFrame("SHINE�Զ����ϵͳ");
        JPanel jp = new JPanel();
        f.add(jp);
        f.getContentPane().add(new JScrollPane(consoleTextArea), java.awt.BorderLayout.CENTER);
        f.setBounds(50, 50, 500, 500);
        f.setVisible(true);

        tray = SystemTray.getSystemTray();// ���ϵͳ����ʵ��
        // ����ϵͳ���̵��Ҽ������˵�
        PopupMenu pm = new PopupMenu();
        MenuItem mi0 = new MenuItem("Open");
        MenuItem mi1 = new MenuItem("Close");
        pm.add(mi0);
        pm.add(mi1);

        trayIcon = new TrayIcon(img, "", pm);// ��������ͼ��ʵ��
        trayIcon.setImageAutoSize(true);// ͼ���Զ���Ӧ���̣�Ҳ����˵���Զ��ı��С
        trayIcon.setToolTip("SHINE�Զ����ϵͳ����ر�");// ������ʾ��
        // ���ϵͳ����ͼ��
        f.addWindowListener(new WindowAdapter() {// �����رա�����ʱ����С����ϵͳ����
            public void windowClosing(WindowEvent e) {
                try {
                    tray.add(trayIcon);
                } catch (AWTException exc) {
                    logger.error(exc.getMessage());
                }
            }
        });
        // �򿪲˵��¼�
        mi0.addActionListener(new ActionListener() { // �Ҽ������˵����¼�����
            public void actionPerformed(ActionEvent e) {
                PassWordPanel passwordWin = new PassWordPanel();
                try {
                    passwordWin.show(f, tray, trayIcon);
                } catch (Exception e1) {
                    logger.error(e1.getMessage());
                }
            }
        });
        // �رղ˵��¼�
        mi1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PassWordPanel passwordWin = new PassWordPanel();
                try {
                    passwordWin.show(null, tray, trayIcon);
                } catch (Exception e1) {
                    logger.error(e1.getMessage());
                }
            }
        });
        // ϵͳ����ͼ���¼�
        trayIcon.addMouseListener(new MouseAdapter() {// ������������Ҳ����ʾ����
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (SwingUtilities.isLeftMouseButton(e)) {// ����������������
                        PassWordPanel passwordWin = PassWordPanel.getInstance();
                        try {
                            passwordWin.show(f, tray, trayIcon);
                        } catch (Exception e1) {
                            logger.error(e1.getMessage());
                        }
                    }
                }
            }
        });
    }
}