package chat;

import sun.misc.BASE64Encoder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Dongfang on 2015/12/15.
 */
public class CipherUtil {

    public static String encoderByMd5(String str)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        //ȷ�����㷽��
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //���ܺ���ַ���
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    public static boolean checkPassword(String newpasswd, String oldpasswd)
            throws NoSuchAlgorithmException, UnsupportedEncodingException{

        if(encoderByMd5(newpasswd).equals(oldpasswd))
            return true;
        else
            return false;
    }
}

