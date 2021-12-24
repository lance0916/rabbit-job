package com.snail.job.common.tools;

import com.snail.job.common.exception.RabbitJobException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author WuQinglong
 * @date 2021/9/11 3:43 下午
 */
public class SignTool {

    /**
     * 计算签名
     */
    public static String sign(String body, String secretKey, String timestamp) {
        String source = body + secretKey + timestamp;
        try {
            return md5(source);
        } catch (NoSuchAlgorithmException e) {
            throw new RabbitJobException(e);
        }
    }

    /**
     * MD5
     */
    private static String md5(String source) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(source.getBytes(UTF_8));
        byte[] result = messageDigest.digest();

        // 通常转为 16 进制进行显示。
        return new BigInteger(1, result).toString(16);
    }

}
