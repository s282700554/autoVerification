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
     * 解密文件,并得到明文数据.
     * 
     * @param encrypFilePath 加密文件路径.
     * @param keyData 解密使用的key，为空则使用默认key解密.
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-9	SGJ	新建
     * </pre>
     */
    public static void decryp(String decrypFilePath, String keyFilePath) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 获得密匙数据
        byte rawKeyData[];
        if (StringUtils.isNotBlank(keyFilePath)) {
            FileInputStream fi = new FileInputStream(new File(keyFilePath));
            rawKeyData = new byte[fi.available()];
            fi.read(rawKeyData);
            fi.close();
        } else {
            rawKeyData = UtilsConstant.KEY_DATA;
        }
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个 SecretKey对象
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        // 现在，获取数据并解密
        FileInputStream fi2 = new FileInputStream(new File(decrypFilePath));
        byte encryptedData[] = new byte[fi2.available()];
        fi2.read(encryptedData);
        fi2.close();
        // 正式执行解密操作
        byte decryptedData[] = cipher.doFinal(encryptedData);
        // 这时把数据还原成原有的类文件
        FileOutputStream fo = new FileOutputStream(new File(decrypFilePath));
        fo.write(decryptedData);
        fo.close();
    }

    /**
     * 
     * 加密文件.
     * 
     * @param encrypFilePath 要加密的文件路径.
     * @param keyFilePath 解密使用的key，为空则使用默认key解密.
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-9	SGJ	新建
     * </pre>
     */
    public static void encryp(String encrypFilePath, String keyFilePath) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 获得密匙数据
        byte rawKeyData[];
        if (StringUtils.isNotBlank(keyFilePath)) {
            FileInputStream fi = new FileInputStream(new File(keyFilePath));
            rawKeyData = new byte[fi.available()];
            fi.read(rawKeyData);
            fi.close();
        } else {
            rawKeyData = UtilsConstant.KEY_DATA;
        }
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        // 现在，获取要加密的文件数据
        FileInputStream fi2 = new FileInputStream(new File(encrypFilePath));
        byte data[] = new byte[fi2.available()];
        fi2.read(data);
        fi2.close();
        // 正式执行加密操作
        byte encryptedData[] = cipher.doFinal(data);
        // 用加密后的数据覆盖原文件
        FileOutputStream fo = new FileOutputStream(new File(encrypFilePath));
        fo.write(encryptedData);
        fo.close();
    }

    /**
     * 
     * 生成加密KEY文件.
     * 
     * @param keyName
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-9	SGJ	新建
     * </pre>
     */
    public void createKey(String keyFilePath) throws Exception {
        // 创建一个可信任的随机数源，DES算法需要
        SecureRandom sr = new SecureRandom();
        // 用DES算法创建一个KeyGenerator对象
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        // 初始化此密钥生成器,使其具有确定的密钥长度
        kg.init(sr);
        // 生成密匙
        SecretKey key = kg.generateKey();
        // 获取密钥数据
        byte rawKeyData[] = key.getEncoded();
        // 将获取到密钥数据保存到文件中，待解密时使用
        FileOutputStream fo = new FileOutputStream(new File(keyFilePath));
        fo.write(rawKeyData);
    }
    
    
    /**
     * 
     * 解密文件,并得到明文数据.
     * 
     * @param encrypFilePath 加密文件路径.
     * @param keyData 解密使用的key，为空则使用默认key解密.
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期     修改人 修改原因
     * 2014-11-9    SGJ 新建
     * </pre>
     */
    public static byte[] decrypDataToByte(String decrypFilePath) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(UtilsConstant.KEY_DATA);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个 SecretKey对象
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        // 现在，获取数据并解密
        FileInputStream fi2 = new FileInputStream(new File(decrypFilePath));
        byte encryptedData[] = new byte[fi2.available()];
        fi2.read(encryptedData);
        fi2.close();
        // 正式执行解密操作
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return decryptedData;
    }
    
    /**
     * 
     * 根据原始文件数据生成加密文件.
     * 
     * @param date 文件原始数据
     * @param encrypFilePath 加密文件路径
     * @throws Exception
     *
     * <pre>
     * 修改日期     修改人 修改原因
     * 2015-3-13    SGJ 新建
     * </pre>
     */
    public static void encrypDateToFile(String date, String encrypFilePath) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(UtilsConstant.KEY_DATA);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        // 正式执行加密操作
        byte encryptedData[] = cipher.doFinal(date.getBytes("GBK"));
        // 用加密后的数据覆盖原文件
        FileOutputStream fo = new FileOutputStream(new File(encrypFilePath));
        fo.write(encryptedData);
        fo.close();
    }
}