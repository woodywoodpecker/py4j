package com.chapter3.po;

/**
 * @Author Warren
 * @CreateTime 03.May.2018
 * @Version V1.0
 */
public class Person {

    private String name;

    private int age;

    public Person(String pName, int pAge) {
        name = pName;
        age = pAge;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int pAge) {
        age = pAge;
    }
}
