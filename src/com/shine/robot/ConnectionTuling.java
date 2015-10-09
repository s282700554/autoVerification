package com.shine.robot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionTuling {

    // 日志
    private static Logger logger = LoggerFactory.getLogger(ConnectionTuling.class);

    /**
     * 
     * 连接网络图灵机器人.
     * 
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2015-8-21	SGJ	新建
     * </pre>
     */
    public static String sendGet(String user, String question) throws Exception {
        question = question.substring(1, question.length());
        String APIKEY = "da0cc41c5e9299eb3cbab3a83ecac993";
        String info = URLEncoder.encode(question, "utf-8");
        String userid = URLEncoder.encode(user, "utf-8");
        String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + info + "&userid=" + userid;
        URL getUrl = new URL(getURL);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuffer messge = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            messge.append(line);
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        logger.info("[" + question + "]返回信息[" + messge + "]");
        return messge.toString();
    }

    /**
     * 
     * 取得消息.
     * 
     * @param user
     * @param question
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2015-8-21	SGJ	新建
     * </pre>
     */
    public static String getMessage(String user, String question) throws Exception {
        String message= null;
        try {
            message = sendGet(user, question);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("连接网络聊天数据失败!");
        }
        JSONObject jsonObj = new JSONObject(message);
        SuperAnalyze superAnalyze = null;
        try {
            superAnalyze = AnalyzeFactory.create(String.valueOf(jsonObj.get("code")));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(jsonObj.toString());
            throw new Exception("未知的返回代码!");
        }
        return superAnalyze.processMessage(jsonObj);
    }
}
