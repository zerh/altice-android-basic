package com.github.zerh;

import lombok.*;

@Data
class Student {
    private int id;
    private String name;
    private String lastName;
    private int age;

    public Student(int id, String name, String lastName, int age){
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }
}