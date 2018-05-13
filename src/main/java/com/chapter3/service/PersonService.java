package com.chapter3.service;

import com.chapter3.po.Person;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.annotation.Transaction;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Warren
 * @CreateTime 05.May.2018
 * @Version V1.0
 */
@Service
public class PersonService {

    public List<String> getNames () {
        return Arrays.asList("Jack","Tom","Warren");
    }

    @Transaction
    public Person createPerson() {
        return new Person("pp",1);
    }

    @Transaction
    public boolean updatePerson(String name,int age) {
        return true;
    }

    @Transaction
    public boolean deletePerson(String name) {
        return true;
    }
}
