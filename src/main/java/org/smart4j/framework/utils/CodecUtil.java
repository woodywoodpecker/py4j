package org.smart4j.framework.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Author Warren
 * @CreateTime 03.May.2018
 * @Version V1.0
 */
public final class CodecUtil {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(CodecUtil.class);

    public static String encodeURL (String source) {
        try {
            return URLEncoder.encode(source,"UTF-8");
        } catch (UnsupportedEncodingException pE) {
            LOGGER.error("encode url failure",pE);
            throw new RuntimeException(pE);
        }
    }

    public static String decodeURL (String source) {
        try {
            return URLDecoder.decode(source,"UTF-8");
        } catch (UnsupportedEncodingException pE) {
            LOGGER.error("decode url failure",pE);
            throw new RuntimeException(pE);
        }
    }
}
