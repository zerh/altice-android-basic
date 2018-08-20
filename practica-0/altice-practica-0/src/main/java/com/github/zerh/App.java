package com.github.zerh;

import java.util.Arrays;
import java.util.List;

public class App {

    List<Student> students = Arrays.asList(
        new Student(1, "Jose", "Hernandez", 23),
        new Student(2, "Daniel", "Perez", 43), 
        new Student(3, "Rafael", "Brito", 12),
        new Student(4, "Miguel", "Ureña", 19), 
        new Student(5, "Victor", "Arias", 52),
        new Student(6, "Maria", "Perez", 52), 
        new Student(7, "Cesar", "Dominguez", 52),
        new Student(8, "Arnold", "Trujillo", 52), 
        new Student(9, "Luis", "Mancebo", 52),
        new Student(10, "Daniela", "Abreu", 52)
    );

    public App(){
        System.out.println("1. Total de estudiantes: " + students.size() );
        System.out.println("2. Edad minima: " + getMinAge() );
        System.out.println("3. Edad maxima: " + getMaxAge() );
        System.out.println("4. Edad promedio: " + getAvgAge() );
        System.out.println();
        System.out.println("5. Listado de estudiantes: \n");
        getAllStudents();
    }

    int getMinAge() {
        return students.stream().mapToInt(s -> s.getAge()).min().getAsInt();
    }

    int getMaxAge() {
        return students.stream().mapToInt(s -> s.getAge()).max().getAsInt();
    }

    double getAvgAge() {
        return students.stream().mapToInt(s -> s.getAge()).average().getAsDouble();
    }

    void getAllStudents(){
        printCell("ID");
        printCell("NOMBRE");
        printCell("APELLIDO");
        printCell("EDAD");
        System.out.print("|\n");
        students.forEach(s -> {
            printCell(String.valueOf(s.getId())); 
            printCell(s.getName()); 
            printCell(s.getLastName()); 
            printCell(String.valueOf(s.getAge())); 
            System.out.print("|\n");
        });
    }

    void printCell(String word){
        String tt = (word.length()>=7)?"\t":"\t\t";
        System.out.print("|"+word+tt);
    }

    public static void main(String[] args) {
        new App();
    }
}
