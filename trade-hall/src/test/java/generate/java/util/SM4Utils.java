package generate.java.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

import org.noear.snack.ONode;

import com.sdnc.common.kits.KV;

public class SM4Utils {

    /**
     * SM4加密
     *
     * @param str 参数
     * @param key 随机码
     * @return
     */
    public static String EncryptStr(String str, String key) {
        return DatatypeConverter.printHexBinary(new SM4_Context().EncryptByte(str.getBytes(), key.getBytes()));
    }

    /**
     * 解密
     *
     * @param cipherStrings 密文
     * @param keyStr        随机码
     * @return
     */
    public static String DecryptStr(String cipherStrings, String keyStr) {
        String result = "";
        SM4_Context aa = new SM4_Context();
        byte[] dd = "00".getBytes();

        byte[] cipherText = DatatypeConverter.parseHexBinary(cipherStrings);
        byte[] keyBytes = keyStr.getBytes();

        try {

            dd = aa.DecryptStrByte(cipherText, keyBytes);
            result = new String(dd);
        } catch (Exception e) {
        }
        return result;

    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // 参数
        KV kv = KV.by("id", new BigInteger("1245967"))
                .set("time", System.currentTimeMillis());
        String str = ONode.stringify(kv);
        System.out.println("参数:" + str);
        // key必须是16位
        System.out.println("----SM4----");
        String key = "LSxH5p1vs3ePYJfM";
        String ncECBData = EncryptStr(str, key);
        System.out.println("加密:" + ncECBData);
        String plainTextEncripted = DecryptStr(ncECBData, key);
        System.out.println("解密:" + plainTextEncripted);
        System.out.println("----Base64----");
        System.out.println(Base64.getEncoder().encodeToString(str.getBytes("utf-8")));
    }
}
