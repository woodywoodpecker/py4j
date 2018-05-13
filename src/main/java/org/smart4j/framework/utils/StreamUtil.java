package org.smart4j.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author Warren
 * @CreateTime 03.May.2018
 * @Version V1.0
 */
public final class StreamUtil {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(StreamUtil.class);

    public static String getString (InputStream is) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException pE) {
            LOGGER.error("get String failure",pE);
            throw new RuntimeException(pE);
        }
        return sb.toString();
    }

}
