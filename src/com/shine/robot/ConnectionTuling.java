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

    // ��־
    private static Logger logger = LoggerFactory.getLogger(ConnectionTuling.class);

    /**
     * 
     * ��������ͼ�������.
     * 
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-8-21	SGJ	�½�
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
        // ȡ������������ʹ��Reader��ȡ
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuffer messge = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            messge.append(line);
        }
        reader.close();
        // �Ͽ�����
        connection.disconnect();
        logger.info("[" + question + "]������Ϣ[" + messge + "]");
        return messge.toString();
    }

    /**
     * 
     * ȡ����Ϣ.
     * 
     * @param user
     * @param question
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2015-8-21	SGJ	�½�
     * </pre>
     */
    public static String getMessage(String user, String question) throws Exception {
        String message= null;
        try {
            message = sendGet(user, question);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("����������������ʧ��!");
        }
        JSONObject jsonObj = new JSONObject(message);
        SuperAnalyze superAnalyze = null;
        try {
            superAnalyze = AnalyzeFactory.create(String.valueOf(jsonObj.get("code")));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(jsonObj.toString());
            throw new Exception("δ֪�ķ��ش���!");
        }
        return superAnalyze.processMessage(jsonObj);
    }
}
