package org.smart4j.framework.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public class Request {

    private String requestMethod;

    private String requestPath;

    public Request() { }

    public Request(String pRequestMethod, String pRequestPath) {
        requestMethod = pRequestMethod;
        requestPath = pRequestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String pRequestMethod) {
        requestMethod = pRequestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String pRequestPath) {
        requestPath = pRequestPath;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this,obj);
    }
}
