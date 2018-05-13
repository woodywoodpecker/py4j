package org.smart4j.framework.bean;

import org.smart4j.framework.utils.CastUtil;

import java.util.Map;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public class Param {

    private Map<String,Object> paramMap;

    public Param(Map<String, Object> pParamMap) {
        paramMap = pParamMap;
    }

    public boolean isEmpty () {
        return paramMap == null || paramMap.isEmpty();
    }

    public long getLong(String name) {
        return CastUtil.castLong(paramMap.get(name));
    }

    public Map<String,Object> getMap(){
        return paramMap;
    }

    @Override
    public String toString() {
        return "Param{" +
                "paramMap=" + paramMap +
                '}';
    }
}
