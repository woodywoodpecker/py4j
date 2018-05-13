package com.chapter3;

import com.chapter3.po.Person;
import com.chapter3.service.PersonService;
import org.apache.log4j.Logger;
import org.smart4j.framework.ServletHandler;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;

/**
 * @Author Warren
 * @CreateTime 03.May.2018
 * @Version V1.0
 */
@Controller
public class RestController {

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    @Inject
    private PersonService personService;

    /**
     * 这地方的方法必须有参数，否则会报错
     * java.lang.IllegalArgumentException: wrong number of arguments
     * 因为反射无法确定该方法，（所以后面我会把这个地方修改一下）
     * @return
     */
    @Action("get:/test1")
    public Data test1 () {
        LOGGER.info("[RestController] [test1] => to test");
        Person p = personService.createPerson();
        Data data = new Data(p);
        return data;
    }

    @Action("get:/test2")
    public Data test2 (Param param) {
        LOGGER.info("[RestController] [test2] => to test");
        LOGGER.info("[RestController] [test2] => param is " + param);
        Person p = new Person("warren",10);
        Data data = new Data(p);
        return data;
    }

    @Action("get:/test3")
    public View test3(Param param) {
        LOGGER.info("[RestController] [test3] => to test");
        LOGGER.info("[RestController] [test3] => param is " + param);
        View view = new View("success.jsp");
        view.addModel("key1","value1");
        return view;
    }

    @Action("get:/test4")
    public Data test4(Param param) {
        LOGGER.info("[RestController] [test4] => to test");
        LOGGER.info("[RestController] [test4] => param is " + param);
        Data data = new Data(personService.getNames());
        return data;
    }

    @Action("get:/test5")
    public View test5() {
        LOGGER.info("[RestController] [test5] request => " + ServletHandler.getRequest());
        LOGGER.info("[RestController] [test5] response => " + ServletHandler.getResponse());
        View view = new View("success.jsp");
        view.addModel("key1","value1");
        return view;
    }
}