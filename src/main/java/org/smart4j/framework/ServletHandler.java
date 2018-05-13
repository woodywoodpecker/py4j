package org.smart4j.framework;

import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author Warren
 * @CreateTime 09.May.2018
 * @Version V1.0
 */
public final class ServletHandler {

    private static final Logger LOGGER = Logger.getLogger(ServletHandler.class);

    private static final ThreadLocal<ServletHandler> SERVLET_HELPER_HOLDER = new ThreadLocal<ServletHandler>();

    private HttpServletRequest request;

    private HttpServletResponse response;

    public ServletHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public static void init (HttpServletRequest request, HttpServletResponse response) {
        SERVLET_HELPER_HOLDER.set(new ServletHandler(request,response));
    }

    public static void destroy () {
        SERVLET_HELPER_HOLDER.remove();
    }

    public static HttpServletRequest getRequest () {
        return SERVLET_HELPER_HOLDER.get().request;
    }

    public static HttpServletResponse getResponse () {
        return SERVLET_HELPER_HOLDER.get().response;
    }

    public static HttpSession getSession () {
        return getRequest().getSession();
    }

    public static ServletContext getContext () {
        return getRequest().getServletContext();
    }

    public static <T> T getRequestAttribute (String key) {
        return (T) getRequest().getAttribute(key);
    }

    public static void setRequestAttribute (String key,Object value) {
        getRequest().setAttribute(key,value);
    }

    public static void removeRequestAttribute (String key) {
        getRequest().removeAttribute(key);
    }

    public static void sendRedirect (String location) {
        try {
            getResponse().sendRedirect(location);
        } catch (IOException e) {
            LOGGER.error("redirect failure",e);
        }
    }

    public static void setSessionAttribute (String key,Object value) {
        getSession().setAttribute(key,value);
    }

    public static  <T> T getSessionAttribute (String key) {
        return (T) getSession().getAttribute(key);
    }

    public static void removeSessionAttribute (String key) {
        getSession().removeAttribute(key);
    }

    public static void invalidateSession () {
        getSession().invalidate();
    }
}
