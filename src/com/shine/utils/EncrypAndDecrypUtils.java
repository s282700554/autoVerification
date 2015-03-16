package com.shine.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class EncrypAndDecrypUtils {

    /**
     * 
     * �����ļ�,���õ���������.
     * 
     * @param encrypFilePath �����ļ�·��.
     * @param keyData ����ʹ�õ�key��Ϊ����ʹ��Ĭ��key����.
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-9	SGJ	�½�
     * </pre>
     */
    public static void decryp(String decrypFilePath, String keyFilePath) throws Exception {
        // DES�㷨Ҫ����һ�������ε������Դ
        SecureRandom sr = new SecureRandom();
        // ����ܳ�����
        byte rawKeyData[];
        if (StringUtils.isNotBlank(keyFilePath)) {
            FileInputStream fi = new FileInputStream(new File(keyFilePath));
            rawKeyData = new byte[fi.available()];
            fi.read(rawKeyData);
            fi.close();
        } else {
            rawKeyData = UtilsConstant.KEY_DATA;
        }
        // ��ԭʼ�ܳ����ݴ���һ��DESKeySpec����
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        // ����һ���ܳ׹�����Ȼ��������DESKeySpec����ת����һ�� SecretKey����
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
        // Cipher����ʵ����ɽ��ܲ���
        Cipher cipher = Cipher.getInstance("DES");
        // ���ܳ׳�ʼ��Cipher����
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        // ���ڣ���ȡ���ݲ�����
        FileInputStream fi2 = new FileInputStream(new File(decrypFilePath));
        byte encryptedData[] = new byte[fi2.available()];
        fi2.read(encryptedData);
        fi2.close();
        // ��ʽִ�н��ܲ���
        byte decryptedData[] = cipher.doFinal(encryptedData);
        // ��ʱ�����ݻ�ԭ��ԭ�е����ļ�
        FileOutputStream fo = new FileOutputStream(new File(decrypFilePath));
        fo.write(decryptedData);
        fo.close();
    }

    /**
     * 
     * �����ļ�.
     * 
     * @param encrypFilePath Ҫ���ܵ��ļ�·��.
     * @param keyFilePath ����ʹ�õ�key��Ϊ����ʹ��Ĭ��key����.
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-9	SGJ	�½�
     * </pre>
     */
    public static void encryp(String encrypFilePath, String keyFilePath) throws Exception {
        // DES�㷨Ҫ����һ�������ε������Դ
        SecureRandom sr = new SecureRandom();
        // ����ܳ�����
        byte rawKeyData[];
        if (StringUtils.isNotBlank(keyFilePath)) {
            FileInputStream fi = new FileInputStream(new File(keyFilePath));
            rawKeyData = new byte[fi.available()];
            fi.read(rawKeyData);
            fi.close();
        } else {
            rawKeyData = UtilsConstant.KEY_DATA;
        }
        // ��ԭʼ�ܳ����ݴ���DESKeySpec����
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        // ����һ���ܳ׹�����Ȼ��������DESKeySpecת����һ��SecretKey����
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
        // Cipher����ʵ����ɼ��ܲ���
        Cipher cipher = Cipher.getInstance("DES");
        // ���ܳ׳�ʼ��Cipher����
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        // ���ڣ���ȡҪ���ܵ��ļ�����
        FileInputStream fi2 = new FileInputStream(new File(encrypFilePath));
        byte data[] = new byte[fi2.available()];
        fi2.read(data);
        fi2.close();
        // ��ʽִ�м��ܲ���
        byte encryptedData[] = cipher.doFinal(data);
        // �ü��ܺ�����ݸ���ԭ�ļ�
        FileOutputStream fo = new FileOutputStream(new File(encrypFilePath));
        fo.write(encryptedData);
        fo.close();
    }

    /**
     * 
     * ���ɼ���KEY�ļ�.
     * 
     * @param keyName
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-9	SGJ	�½�
     * </pre>
     */
    public void createKey(String keyFilePath) throws Exception {
        // ����һ�������ε������Դ��DES�㷨��Ҫ
        SecureRandom sr = new SecureRandom();
        // ��DES�㷨����һ��KeyGenerator����
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        // ��ʼ������Կ������,ʹ�����ȷ������Կ����
        kg.init(sr);
        // �����ܳ�
        SecretKey key = kg.generateKey();
        // ��ȡ��Կ����
        byte rawKeyData[] = key.getEncoded();
        // ����ȡ����Կ���ݱ��浽�ļ��У�������ʱʹ��
        FileOutputStream fo = new FileOutputStream(new File(keyFilePath));
        fo.write(rawKeyData);
    }
    
    
    /**
     * 
     * �����ļ�,���õ���������.
     * 
     * @param encrypFilePath �����ļ�·��.
     * @param keyData ����ʹ�õ�key��Ϊ����ʹ��Ĭ��key����.
     * @return
     * @throws Exception
     * 
     *             <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2014-11-9    SGJ �½�
     * </pre>
     */
    public static byte[] decrypDataToByte(String decrypFilePath) throws Exception {
        // DES�㷨Ҫ����һ�������ε������Դ
        SecureRandom sr = new SecureRandom();
        // ��ԭʼ�ܳ����ݴ���һ��DESKeySpec����
        DESKeySpec dks = new DESKeySpec(UtilsConstant.KEY_DATA);
        // ����һ���ܳ׹�����Ȼ��������DESKeySpec����ת����һ�� SecretKey����
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
        // Cipher����ʵ����ɽ��ܲ���
        Cipher cipher = Cipher.getInstance("DES");
        // ���ܳ׳�ʼ��Cipher����
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        // ���ڣ���ȡ���ݲ�����
        FileInputStream fi2 = new FileInputStream(new File(decrypFilePath));
        byte encryptedData[] = new byte[fi2.available()];
        fi2.read(encryptedData);
        fi2.close();
        // ��ʽִ�н��ܲ���
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return decryptedData;
    }
    
    /**
     * 
     * ����ԭʼ�ļ��������ɼ����ļ�.
     * 
     * @param date �ļ�ԭʼ����
     * @param encrypFilePath �����ļ�·��
     * @throws Exception
     *
     * <pre>
     * �޸�����     �޸��� �޸�ԭ��
     * 2015-3-13    SGJ �½�
     * </pre>
     */
    public static void encrypDateToFile(String date, String encrypFilePath) throws Exception {
        // DES�㷨Ҫ����һ�������ε������Դ
        SecureRandom sr = new SecureRandom();
        // ��ԭʼ�ܳ����ݴ���DESKeySpec����
        DESKeySpec dks = new DESKeySpec(UtilsConstant.KEY_DATA);
        // ����һ���ܳ׹�����Ȼ��������DESKeySpecת����һ��SecretKey����
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
        // Cipher����ʵ����ɼ��ܲ���
        Cipher cipher = Cipher.getInstance("DES");
        // ���ܳ׳�ʼ��Cipher����
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        // ��ʽִ�м��ܲ���
        byte encryptedData[] = cipher.doFinal(date.getBytes("GBK"));
        // �ü��ܺ�����ݸ���ԭ�ļ�
        FileOutputStream fo = new FileOutputStream(new File(encrypFilePath));
        fo.write(encryptedData);
        fo.close();
    }
}