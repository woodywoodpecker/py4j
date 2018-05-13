package org.smart4j.framework;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.utils.CodecUtil;
import org.smart4j.framework.utils.ReflectionUtil;
import org.smart4j.framework.utils.StreamUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        /**
         * 初始化HelpLoader
         */
        HelperLoader.init();

        ServletContext servletContext = servletConfig.getServletContext();
        // 注册处理jsp的servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        // 注册处理静态资源的默认servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletHandler.init(request,response);
        try {
            String requestMethod = request.getMethod().toLowerCase();
            String requestPath = request.getServletPath();

            Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);
            if (handler == null) {
                throw new RuntimeException(
                        "no available handle found," +
                        "requestMethod is " + requestMethod +
                        ",requestPath " + requestPath);
            }

            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            Map<String,Object> paramMap = new HashMap<String,Object>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }

            String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
            if (StringUtils.isNotEmpty(body)) {
                String[] params = StringUtils.split(body,"&");
                if (params != null && params.length > 0) {
                    for (String param : params) {
                        String[] array = StringUtils.split(param,"=");
                        if (array != null && array.length == 2) {
                            paramMap.put(array[0],array[1]);
                        }
                    }
                }
            }

            Param param = new Param(paramMap);
            // 调用action方法
            Method actionMethod = handler.getActionMethod();
            Object result;
            // 这里增加param的空判断，在通过反射调用方法的时候，可以准确匹配目标方法
            if (param.isEmpty()) {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
            } else {
                result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
            }

            if (result instanceof View) {
                View view = (View) result;
                String path = view.getPath();
                if (StringUtils.isNotEmpty(path)) {
                    if (path.startsWith("/")) {
                        response.sendRedirect(request.getContextPath()+path);
                    } else {
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String, Object> entry : model.entrySet()) {
                            request.setAttribute(entry.getKey(),entry.getValue());
                        }
                        request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request,response);
                    }
                }
            }
            else if (result instanceof Data) {
                Data data = (Data) result;
                Object model = data.getModel();
                if (model != null) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    String json = new Gson().toJson(model);
                    out.write(json);
                    out.flush();
                    out.close();
                }
            }
        } finally {
            ServletHandler.destroy();
        }
    }

}
