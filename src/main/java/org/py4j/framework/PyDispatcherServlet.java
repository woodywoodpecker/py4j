package org.py4j.framework;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.py4j.framework.annotation.*;
import org.py4j.framework.aop.EnhanceHandler;
import org.py4j.framework.bean.Handler;
import org.py4j.framework.utils.ClassTemplateUtil;
import org.smart4j.framework.utils.ReflectionUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @Author Warren
 * @CreateTime 10.May.2018
 * @Version V1.0
 */
public class PyDispatcherServlet extends HttpServlet {

    private final Logger mLogger = Logger.getLogger(this.getClass());

    private final List<Class<?>> classList = new ArrayList<Class<?>>();

    private final Map<Object,Object> iocContainer = new HashMap<Object,Object>(16);

    private final Map<String,Handler> handlerMappings = new HashMap<String, Handler>(16);

    @Override
    public void init(ServletConfig config) throws ServletException {
        String scanBase = config.getInitParameter(Py4jConstants.COMMON_CONFIG_BASE_SCAN);

        // 1 =>
        initialClassTemplateList(scanBase);

        // 2->
        initialIocContainer();

        // 2.5->在这个地方把我们的aspect加进去，这样在依赖注入的时候，注入就是代理类
        initialAopApplication();

        // 3->
        doDependencyInject();

        // 4->
        resolveHandlerMappings();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = req.getMethod().toUpperCase();
        String requestPath = req.getServletPath();

        Handler handler = handlerMappings.get(requestMethod + ":" + requestPath);
        if (handler != null) {
            Object result = ReflectionUtil.invokeMethod(handler.getTargetInstance(), handler.getTargetMethod());
            // 这个地方为了简便，所以我直接选择将result作为json格式的数据输出
            // TODO 因为这地方其实还有一个view resolver的工作的，就是spring mvc里面ModelAndView的操作
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            String json = new Gson().toJson(result);
            out.write(json);
            out.flush();
            out.close();
        }
    }

    private void initialClassTemplateList(String pScanBase) {
        try {
            String filePath = pScanBase.replace(".","/");
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(filePath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                File[] files = new File(url.getPath()).listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
                    }
                });
                for (File file : files) {
                    if (file.isFile()) {
                        String classPathName = pScanBase + "." +file.getName().substring(0,file.getName().lastIndexOf("."));
                        Class<?> clazz = Class.forName(classPathName);
                        classList.add(clazz);
                    }
                    else {
                        String parentPath = pScanBase;
                        String currentPath = file.getName();
                        initialClassTemplateList(parentPath+"."+currentPath);
                    }
                }
            }
        }
        catch (Exception pE) {
            mLogger.error("class template list init failure", pE);
        }
    }

    private void initialIocContainer() {
        if (classList.isEmpty()) mLogger.warn("class template list is empty,skip ioc container initial");

        try {
            for (Class<?> cls : classList) {
                if (cls.isAnnotationPresent(Py4jController.class)) {
                    Object instance = cls.newInstance();
                    iocContainer.put(toDefaultInstanceName(cls),instance);
                }
                if (cls.isAnnotationPresent(Py4jService.class)) {
                    Object instance = cls.newInstance();
                    Py4jService serviceAnnotation = cls.getAnnotation(Py4jService.class);
                    String serviceName = serviceAnnotation.name();
                    if (StringUtils.isBlank(serviceName)) {
                        iocContainer.put(toDefaultInstanceName(cls),instance);
                    }
                    else {
                        iocContainer.put(serviceName,instance);
                    }
                }
                if (cls.isAnnotationPresent(Py4jAspect.class)) {
                    Object instance = cls.newInstance();
                    iocContainer.put(toDefaultInstanceName(cls),instance);
                }
            }
        }
        catch (Exception pE) {
            mLogger.error("ioc container init failure", pE);
        }

    }

    private void initialAopApplication() {
        List<Class<?>> aspectClassList = ClassTemplateUtil.getAspectClassList(classList);
        for (Class<?> cls : aspectClassList) {
            Method[] declaredMethods = cls.getDeclaredMethods();
            final List<EnhanceHandler> enhanceHandlerList = new ArrayList<>();
            for (Method declaredMethod : declaredMethods) {
                if (!declaredMethod.isAnnotationPresent(Py4jPointCut.class)) continue;
                Py4jPointCut annotation = declaredMethod.getAnnotation(Py4jPointCut.class);
                Class<?> targetClass = annotation.targetClass();
                String targetMethod = annotation.targetMethod();
                Py4jPointCut.PointCutType pointCutType = annotation.pointCutType();
                Object aspectInstance = iocContainer.get(toDefaultInstanceName(cls));
                Method aspectMethod = declaredMethod;
                EnhanceHandler enhanceHandler = new EnhanceHandler(
                        aspectInstance,aspectMethod,pointCutType,targetClass,targetMethod);
                enhanceHandlerList.add(enhanceHandler);
            }
            // TODO
            // 这个地方出来的mapping可能会有点问题，因为一个controller可能被多个切面作用，所以应该等到上面的aspectClassList
            // 迭代完成以后，合并我们下面createEnhanceHandlerMapping(enhanceHandlerList);出来的mapping，最后再做createProxy
            // 的操作。
            // --> 这个可以后面来做，因为这点已经很容易实现了
            Map<Class<?>, List<EnhanceHandler>> mapping = createEnhanceHandlerMapping(enhanceHandlerList);
            for (Map.Entry<Class<?>, List<EnhanceHandler>> entry : mapping.entrySet()) {
                Object proxy = Py4jProxyManager.createProxy(entry.getKey(),entry.getValue());
                iocContainer.put(toDefaultInstanceName(entry.getKey()),proxy);
            }
        }
    }

    private Map<Class<?>, List<EnhanceHandler>> createEnhanceHandlerMapping(List<EnhanceHandler> pEnhanceHandlerList) {
        final Map<Class<?>, List<EnhanceHandler>> mapping = new HashMap<>();
        for (EnhanceHandler enhanceHandler : pEnhanceHandlerList) {
            List<EnhanceHandler> enhanceHandlerList = mapping.get(enhanceHandler.getTargetClass());
            if (enhanceHandlerList == null) {
                enhanceHandlerList = new ArrayList<>();
                mapping.put(enhanceHandler.getTargetClass(),enhanceHandlerList);
            }
            enhanceHandlerList.add(enhanceHandler);
        }
        return mapping;
    }


    private void doDependencyInject() {
        for (Map.Entry<Object, Object> env : iocContainer.entrySet()) {
            Object targetInstance = env.getValue();
            Class<?> clazz = targetInstance.getClass();
            Field[] targetFields = clazz.getDeclaredFields();
            for (Field targetField : targetFields) {
                if (targetField.isAnnotationPresent(Py4jAutowired.class)) {
                    Py4jAutowired annotation = targetField.getAnnotation(Py4jAutowired.class);
                    String beanName = annotation.name();
                    if (StringUtils.isBlank(beanName)) {
                        beanName = toDefaultInstanceName(targetField.getType());
                    }
                    Object dependentInstance = iocContainer.get(beanName);
                    if (dependentInstance == null) {
                        if (annotation.required()) {
                            throw new RuntimeException(
                                    "inject failed. class is "+clazz.getName()+" , field is "+targetField.getName());
                        }
                    }
                    else {
                        ReflectionUtil.setField(targetInstance,targetField,dependentInstance);
                    }
                }
            }
        }
    }

    private void resolveHandlerMappings() {
        List<Class<?>> controllerClassList = ClassTemplateUtil.getControllerClassList(classList);
        for (Class<?> cls : controllerClassList) {
            Method[] declaredMethods = cls.getDeclaredMethods();
            if (declaredMethods == null || declaredMethods.length == 0) continue;
            for (Method targetMethod : declaredMethods) {
                if (!targetMethod.isAnnotationPresent(Py4jRequestMapping.class)) continue;
                Py4jRequestMapping annotation = targetMethod.getAnnotation(Py4jRequestMapping.class);
                String requestPath = annotation.value();
                Py4jRequestMapping.HttpMethod requestMethod = annotation.method();
                Object targetInstance = iocContainer.get(toDefaultInstanceName(cls));
                Handler h = new Handler(requestPath,requestMethod,targetInstance,targetMethod);
                handlerMappings.put(requestMethod+":"+requestPath,h);
            }
        }
    }

    private String toDefaultInstanceName(Class<?> cls) {
        String absoluteName = cls.getName();
        String className = absoluteName.substring(absoluteName.lastIndexOf(".")+1);
        char[] chars = className.toCharArray();
        // 我们默认Class Name是大写开头的，约定大于语法，所以不用纠结如果不是大写而出现的乱码问题
        return (char) (chars[0] + 32) + className.substring(1);
    }


}
