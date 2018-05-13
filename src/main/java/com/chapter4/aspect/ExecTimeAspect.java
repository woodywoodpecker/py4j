package com.chapter4.aspect;

import com.chapter4.Chapter4Controller;
import org.apache.log4j.Logger;
import org.py4j.framework.annotation.Py4jAspect;
import org.py4j.framework.annotation.Py4jPointCut;
import org.py4j.framework.bean.ProxyHandler;

/**
 * @Author Warren
 * @CreateTime 12.May.2018
 * @Version V1.0
 */
@Py4jAspect
public class ExecTimeAspect {

    private final Logger mLogger = Logger.getLogger(this.getClass());

    @Py4jPointCut(targetClass = Chapter4Controller.class,
                  targetMethod = "test1" ,
                  pointCutType = Py4jPointCut.PointCutType.BEFORE)
    public void testBefore (ProxyHandler handler) {
        mLogger.info("[ExecTimeAspect] [testBefore] => this is pointCut testBefore");
    }

    @Py4jPointCut(targetClass = Chapter4Controller.class,
                  targetMethod = "test1" ,
                  pointCutType = Py4jPointCut.PointCutType.AFTER)
    public void testAfter (ProxyHandler handler) {
        mLogger.info("[ExecTimeAspect] [testAfter] => this is pointCut testAfter");
    }

}
