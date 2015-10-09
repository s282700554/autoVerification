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
    
    TrayIcon trayIcon;// 托盘图标，但不是Image类型的 哦
    SystemTray tray;// 系统托盘
    Image img = (new ImageIcon("data/icon.png")).getImage();// 托盘图标，建议使用较小的图片

    public ConsoleTextArea(InputStream[] inStreams) throws Exception {
        for (int i = 0; i < inStreams.length; ++i) {
            startConsoleReaderThread(inStreams[i]);
        }
    }
    
    /**
     * 
     * 实例化.
     * 
     * @return
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-9	SGJ	新建
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
            // 重定向System.out和System.err
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
                    JOptionPane.showMessageDialog(null, "从BufferedReader读取错误：" + e);
                    System.exit(1);
                }
            }
        }).start();
    }

    /**
     * 运行状态显示界面.
     * 
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-26	SGJ	新建
     * </pre>
     */
    public void showLog() throws Exception {
        consoleTextArea.setFont(java.awt.Font.decode("monospaced"));
        // 创建窗口
        final JFrame f = new JFrame("SHINE自动验包系统");
        JPanel jp = new JPanel();
        f.add(jp);
        f.getContentPane().add(new JScrollPane(consoleTextArea), java.awt.BorderLayout.CENTER);
        f.setBounds(50, 50, 500, 500);
        f.setVisible(true);

        tray = SystemTray.getSystemTray();// 获得系统托盘实例
        // 创建系统托盘的右键弹出菜单
        PopupMenu pm = new PopupMenu();
        MenuItem mi0 = new MenuItem("Open");
        MenuItem mi1 = new MenuItem("Close");
        pm.add(mi0);
        pm.add(mi1);

        trayIcon = new TrayIcon(img, "", pm);// 创建托盘图标实例
        trayIcon.setImageAutoSize(true);// 图标自动适应托盘，也就是说它自动改变大小
        trayIcon.setToolTip("SHINE自动验包系统请勿关闭");// 设置提示语
        // 添加系统托盘图标
        f.addWindowListener(new WindowAdapter() {// 当“关闭”窗口时，最小化到系统托盘
            public void windowClosing(WindowEvent e) {
                try {
                    tray.add(trayIcon);
                } catch (AWTException exc) {
                    logger.error(exc.getMessage());
                }
            }
        });
        // 打开菜单事件
        mi0.addActionListener(new ActionListener() { // 右键弹出菜单的事件监听
            public void actionPerformed(ActionEvent e) {
                PassWordPanel passwordWin = new PassWordPanel();
                try {
                    passwordWin.show(f, tray, trayIcon);
                } catch (Exception e1) {
                    logger.error(e1.getMessage());
                }
            }
        });
        // 关闭菜单事件
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
        // 系统托盘图标事件
        trayIcon.addMouseListener(new MouseAdapter() {// 单击鼠标左键，也是显示窗口
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (SwingUtilities.isLeftMouseButton(e)) {// 如果点击的是鼠标左键
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