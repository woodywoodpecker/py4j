package com.chapter4;

import com.chapter4.po.Animal;
import org.py4j.framework.annotation.Py4jAutowired;
import org.py4j.framework.annotation.Py4jController;
import org.py4j.framework.annotation.Py4jRequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Warren
 * @CreateTime 10.May.2018
 * @Version V1.0
 */
@Py4jController
public class Chapter4Controller {

    @Py4jAutowired
    private Animal mAnimal;

    @Py4jRequestMapping(value = "/test1" , method = Py4jRequestMapping.HttpMethod.GET)
    public Object test1() {
        Map<Object,Object> map = new HashMap<>();
        map.put("name","warren");
        map.put("sex","male");
        map.put("from","China");
        return map;
    }

}
