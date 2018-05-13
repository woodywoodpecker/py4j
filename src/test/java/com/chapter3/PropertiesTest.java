package com.chapter3;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;
/**
 * @Author Warren
 * @CreateTime 05.May.2018
 * @Version V1.0
 */
public class PropertiesTest {

    @Test
    public void protest1() {
        Properties p = new Properties();
        p.put("smart.framework.app.base_package","com.chapter3");
        boolean flag = p.contains("smart.framework.app.base_package");
        Assert.assertTrue(flag);
    }

}
