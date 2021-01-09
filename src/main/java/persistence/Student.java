package persistence;

import annotations.NotNull;
import annotations.PrimaryKey;
import annotations.Table;

@Table
public class Student {
    @PrimaryKey
    @NotNull
    public Integer id;

    @NotNull
    public String name;

    @NotNull
    public Integer age;

    public Student(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}