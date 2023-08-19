package com.example.common;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 封装 ServletRequest 以支持可重复读请求体
 * @author WuQinglong
 */
public class ServletRequestWrapper extends HttpServletRequestWrapper {
    private static Logger log = LoggerFactory.getLogger(ServletRequestWrapper.class);

    /**
     * 存储上传的文件
     */
    private Collection<Part> parts;

    /**
     * 请求体数据
     */
    private final ByteArrayOutputStream bodyBytes;

    public ServletRequestWrapper(HttpServletRequest request) {
        super(request);

        // 获取上传的文件
        try {
            String contentType = request.getContentType();
            if (contentType != null && contentType.startsWith("multipart/form-data")) {
                parts = request.getParts();
            }
        } catch (IOException | ServletException e) {
            parts = Collections.emptyList();
            log.error("发生异常", e);
        }

        // 请求体数据
        bodyBytes = new ByteArrayOutputStream();
        try (InputStream inputStream = request.getInputStream()) {
            int hasRead;
            byte[] buffer = new byte[4096];
            while ((hasRead = inputStream.read(buffer)) != -1) {
                bodyBytes.write(buffer, 0, hasRead);
            }
        } catch (IOException e) {
            log.error("发生异常", e);
        }
    }

    @Override
    public Collection<Part> getParts() {
        return parts;
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private final byte[] body = bodyBytes.toByteArray();
            private int index = 0;

            @Override
            public boolean isFinished() {
                return index >= body.length;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            public int read() {
                if (!isFinished()) {
                    return body[index++];
                }
                return -1;
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * 获取请求体
     */
    public byte[] getBody() {
        return bodyBytes.toByteArray();
    }
}