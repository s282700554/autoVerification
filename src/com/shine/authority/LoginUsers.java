package com.shine.authority;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginUsers {

    private static Map<Long, AuthorityBean> map = new ConcurrentHashMap<Long, AuthorityBean>();

    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

    /**
     * 
     * ��½.
     * 
     * @param uin
     * @param authorityBean
     * 
     *            <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-10-12	SGJ	�½�
     * </pre>
     */
    public static void login(Long uin, AuthorityBean authorityBean) {
        synchronized (LoginUsers.class) {
            LoginUsers.map.put(uin, authorityBean);
        }
    }

    /**
     * 
     * �ǳ�.
     * 
     * @param uin
     * 
     *            <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-10-12	SGJ	�½�
     * </pre>
     */
    public static void loginOut(Long uin) {
        synchronized (LoginUsers.class) {
            map.remove(uin);
        }
    }

    /**
     * 
     * �Ƿ��ѵ�½.
     * 
     * @param uin
     * @return
     * 
     *         <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-10-12	SGJ	�½�
     * </pre>
     */
    public static boolean hasLogin(Long uin) {
        return map.containsKey(uin);
    }

    /**
     * 
     * ��ȡ��½��Ϣ.
     * 
     * @param uin
     * @return
     * 
     *         <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-10-12	SGJ	�½�
     * </pre>
     */
    public static AuthorityBean getLoginInfo(Long uin) {
        return map.get(uin);
    }

    /**
     * 
     * �����½����.
     * 
     * 
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-10-12	SGJ	�½�
     * </pre>
     */
    public static void clear() {
        map.clear();
    }

    /**
     * 
     * ������ڵ�½.
     * 
     * 
     * <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-10-12	SGJ	�½�
     * </pre>
     */
    public static void clearTimeOut() {
        synchronized (LoginUsers.class) {
            Date date = new Date();
            String time = df.format(date);
            for (Long key : map.keySet()) {
                if (Long.valueOf(time) - Long.valueOf(df.format(map.get(key).getDate())) > 30) {
                    map.remove(key);
                }
            }
        }
    }
}
