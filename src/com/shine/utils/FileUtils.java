package com.shine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * ɾ����Ŀ¼
     * 
     * @param dir ��Ҫɾ����Ŀ¼·��
     * @param dir
     * 
     *            <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-6	SGJ	�½�
     * </pre>
     */
    public static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            logger.error("Successfully deleted empty directory: " + dir);
        } else {
            logger.error("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * * �ݹ�ɾ��Ŀ¼�µ������ļ�����Ŀ¼�������ļ�
     * 
     * @param dir ��Ҫɾ�����ļ�Ŀ¼
     * @return boolean Returns "true" if all deletions were successful. If a deletion fails, the method stops attempting
     *         to delete and returns "false".
     * 
     *         <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-6	SGJ	�½�
     * </pre>
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // �ݹ�ɾ��Ŀ¼�е���Ŀ¼��
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // Ŀ¼��ʱΪ�գ�����ɾ��
        return dir.delete();
    }

    /**
     * ���ļ��е�ָ���ַ����滻��ָ�����ַ���.
     * 
     * @param filePath ԭ�ļ�·��
     * @param fileType ��Ҫ�����滻�ļ��Ĺؼ��ʣ���.jsp
     * @param findStr �ļ�����Ҫ�滻���ַ���
     * @param replaceStr �滻����ַ���
     * @throws IOException
     * @throws InterruptedException
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-8-8      SGJ       �½�
     * </pre>
     */
    public static void replaceFileKeyWord(String filePath, String fileType, String findStr, String replaceStr,
            List<File> fileList) throws IOException, InterruptedException {
        String tempName = null;
        File baseDir = new File(filePath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            logger.warn("δ�ҵ�Ҫ�滻���ļ�");
        } else {
            String[] filelist = baseDir.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filePath + "\\" + filelist[i]);
                if (!readfile.isDirectory()) {
                    tempName = readfile.getName();
                    if (FileUtils.wildcardMatch(fileType, tempName)) {
                        File src = new File(readfile.getAbsoluteFile().toString());
                        String cont = FileUtils.read(src, findStr);
                        if (cont != null && cont != "") {
                            fileList.add(readfile.getAbsoluteFile());
                            Long fileDate = readfile.lastModified();
                            cont = cont.replaceAll(findStr, replaceStr);
                            FileUtils.write(cont, src);
                            readfile.setLastModified(fileDate);
                        }
                    }
                } else if (readfile.isDirectory()) {
                    replaceFileKeyWord(filePath + "\\" + filelist[i], fileType, findStr, replaceStr, fileList);
                }
            }
        }
    }

    /**
     * 
     * ��ѯ��Ӧ�����ļ�
     * 
     * @param fileType �ļ�����
     * @param fileName �ļ���������׺
     * @return
     * 
     *         <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-8-8 SGJ �½�
     * </pre>
     */
    public static boolean wildcardMatch(String fileType, String fileName) {
        int patternLength = fileType.length();
        int strLength = fileName.length();
        int strIndex = 0;
        char ch;
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
            ch = fileType.charAt(patternIndex);
            if (ch == '*') {
                while (strIndex < strLength) {
                    if (wildcardMatch(fileType.substring(patternIndex + 1), fileName.substring(strIndex))) {
                        return true;
                    }
                    strIndex++;
                }
            } else if (ch == '?') {
                strIndex++;
                if (strIndex > strLength) {
                    return false;
                }
            } else {
                if ((strIndex >= strLength) || (ch != fileName.charAt(strIndex))) {
                    return false;
                }
                strIndex++;
            }
        }
        return (strIndex == strLength);
    }

    /**
     * ��ȡ�ļ��е�ָ������.
     * 
     * @param src
     * @return
     * 
     *         <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-8-8 SGJ �½�
     * </pre>
     */
    public static String read(File src, String findStr) {
        StringBuffer res = new StringBuffer();
        String line = null;
        boolean flag = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(src));
            while ((line = reader.readLine()) != null) {
                if (findStr != null && wildcardMatch(findStr, line)) {
                    flag = true;
                }
                res.append(line + "\r\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        return flag || findStr == null ? res.toString() : null;
    }

    /**
     * �ļ��滻�����.
     * 
     * @param fileData
     * @param filePath
     * @return
     * 
     *         <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-8-8 SGJ �½�
     * </pre>
     */
    public static boolean write(String fileData, File filePath) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath));
            writer.write(fileData);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 
     * ���ݸ�ʽд�ļ�.
     * 
     * @param path
     * @param path2
     * @param format
     * @throws IOException
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-8-8 SGJ �½�
     * </pre>
     */
    public static void readFile(String path, String path2, String format) throws IOException {
        String str = "";
        FileInputStream fs = null;
        FileWriter fw = null;
        PrintWriter out = null;
        BufferedReader in = null;
        File f = new File(path);
        if (f.exists()) {
            try {
                fs = new FileInputStream(f);
                fw = new FileWriter(path2);
                out = new PrintWriter(fw);
                in = new BufferedReader(new InputStreamReader(fs, format));
                while (true) {
                    str = in.readLine();
                    if (str == null) {
                        break;
                    }
                    out.write(str);
                    out.println();
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                in.close();
                fs.close();
                fw.close();
                out.close();
            }
        }
    }

    /**
     * ������־,������־�д����Ƿ�ؼ���.
     * 
     * @param fileName
     * @param Keyword
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-5-24    SGJ �½�
     * </pre>
     */
    public static boolean searchFileKeyWord(String fileName, String Keyword) throws Exception {
        if (fileName == null || "".equalsIgnoreCase(fileName) || Keyword == null || "".equalsIgnoreCase(Keyword)) {
            logger.error("�ļ�����ؼ���Ϊ��");
            throw new Exception("�ļ�����ؼ���Ϊ��");
        }
        Charset charset = Charset.forName("GB18030");
        CharsetDecoder decoder = charset.newDecoder();
        // ����������޹أ��˶��估���������Ʉh��
        FileInputStream fis = null;
        FileChannel fc = null;
        MappedByteBuffer bb = null;
        try {
            fis = new FileInputStream(fileName);
            fc = fis.getChannel();
            int sz = (int) fc.size();
            bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
            CharBuffer cb = decoder.decode(bb);
            String s = String.valueOf(cb);
            int n = s.indexOf(Keyword);
            if (n > -1) {
                logger.warn(Keyword + " --- " + n);
                return true;
            } else {
                logger.warn(Keyword + " --- not found! ");
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            fc.close();
            fis.close();
            clean(bb);
        }
        return true;
    }

    /**
     * 
     * ���
     * 
     * @param buffer
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-8-12    SGJ �½�
     * </pre>
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void clean(final Object buffer) throws Exception {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    /**
     * 
     * ���ļ��������ؼ��ֵĸ���.
     * 
     * @param fileName
     * @param Keyword
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-8-12    SGJ �½�
     * </pre>
     */
    public static int searchFileKeyWordNum(String fileName, String Keyword) throws Exception {
        if (fileName == null || "".equalsIgnoreCase(fileName) || Keyword == null || "".equalsIgnoreCase(Keyword)) {
            logger.error("�ļ�����ؼ���Ϊ��");
            throw new Exception("�ļ�����ؼ���Ϊ��");
        }
        Charset charset = Charset.forName("GB18030");
        CharsetDecoder decoder = charset.newDecoder();
        // ����������޹أ��˶��估���������Ʉh��
        FileInputStream fis = null;
        FileChannel fc = null;
        MappedByteBuffer bb = null;
        try {
            fis = new FileInputStream(fileName);
            fc = fis.getChannel();
            int sz = (int) fc.size();
            bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
            CharBuffer cb = decoder.decode(bb);
            String s = String.valueOf(cb);
            int size = findKeyWordNum(s, Keyword, 0, 0);
            return size;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            fc.close();
            fis.close();
            clean(bb);
        }
        return 0;
    }

    /**
     * ���ַ�����ָ��λ��ͳ�ƹؼ��ָ���.
     * 
     * @param string
     * @param Keyword
     * @param index
     * @param size
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-8-12    SGJ �½�
     * </pre>
     */
    public static int findKeyWordNum(String string, String Keyword, int index, int size) throws Exception {
        int n = string.indexOf(Keyword, index);
        if (n > -1) {
            size = findKeyWordNum(string, Keyword, n + 1, size);
            size++;
        } else {
            return size;
        }
        return size;
    }

    /**
     * ����bat�ļ�.
     * 
     * @param srcFile
     * @param GenerateFile
     * @param replaceStr
     * @param valueStr
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-5-21	SGJ	�½�
     * </pre>
     */
    public static void replaceKeyAndCreateFile(String srcFile, String createFile, List<String> replaceStr,
            List<String> valueStr) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(createFile)), true);
        String line = null;
        while ((line = br.readLine()) != null) {
            for (int i = 0; i < replaceStr.size(); i++) {
                line = line.replaceAll(replaceStr.get(i), valueStr.get(i));
            }
            pw.println(line);
        }
        pw.flush();
        br.close();
        pw.close();
    }

    /**
     * ����map���key��value���ɶ�Ӧ��bat.
     * 
     * @param srcFile
     * @param GenerateFile
     * @param map
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-8-7	SGJ	�½�
     * </pre>
     */
    public static void replaceKeyAndCreateFile(String srcFile, String GenerateFile, Map<String, String> map)
            throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(GenerateFile)), true);
        String line = null;
        while ((line = br.readLine()) != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                line = line.replaceAll(entry.getKey(), entry.getValue());
            }
            pw.println(line);
        }
        pw.flush();
        br.close();
        pw.close();
    }

}
