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
     * 登陆.
     * 
     * @param uin
     * @param authorityBean
     * 
     *            <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-12	SGJ	新建
     * </pre>
     */
    public static void login(Long uin, AuthorityBean authorityBean) {
        synchronized (LoginUsers.class) {
            LoginUsers.map.put(uin, authorityBean);
        }
    }

    /**
     * 
     * 登出.
     * 
     * @param uin
     * 
     *            <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-12	SGJ	新建
     * </pre>
     */
    public static void loginOut(Long uin) {
        synchronized (LoginUsers.class) {
            map.remove(uin);
        }
    }

    /**
     * 
     * 是否已登陆.
     * 
     * @param uin
     * @return
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-12	SGJ	新建
     * </pre>
     */
    public static boolean hasLogin(Long uin) {
        return map.containsKey(uin);
    }

    /**
     * 
     * 获取登陆信息.
     * 
     * @param uin
     * @return
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-12	SGJ	新建
     * </pre>
     */
    public static AuthorityBean getLoginInfo(Long uin) {
        return map.get(uin);
    }

    /**
     * 
     * 清除登陆数据.
     * 
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-12	SGJ	新建
     * </pre>
     */
    public static void clear() {
        map.clear();
    }

    /**
     * 
     * 清理过期登陆.
     * 
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2015-10-12	SGJ	新建
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
