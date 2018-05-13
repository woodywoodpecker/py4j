package org.smart4j.framework.utils;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public final class PropsUtils {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PropsUtils.class);

    public static Properties loadProps (String fileName) {
        Properties props = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is ==null) {
                throw new FileNotFoundException(fileName+" file is not found.");
            }
            props = new Properties();
            props.load(is);
        }  catch (IOException s) {
            LOGGER.error("load properties failure.",s);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException pE) {
                    // ignore this exception
                }
            }
        }
        return props;
    }

    public static String getString (Properties props,String key) {
        return getString(props,key,"");
    }

    public static String getString (Properties props,String key,String defaultValue) {
        String value = props.getProperty(key);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    public static int getInt(Properties props,String key) {
        return getInt(props,key,0);
    }

    private static int getInt(Properties props, String key, int defaultValue) {
        String value = props.getProperty(key);
        if (value != null) {
            return CastUtil.castInt(value);
        }
        return defaultValue;
    }

    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props,key,false);
    }

    public static boolean getBoolean(Properties props,String key,boolean defaultValue){
        String value = props.getProperty(key);
        if (value != null) {
            return Boolean.valueOf(value);
        }
        return defaultValue;
    }
}
