package persistence;

import annotations.*;

@Table
public class Grade {
    @Column
    @PrimaryKey
    @NotNull
    public Integer id;

    @Column
    @NotNull
    public String name;

    @Column
    @NotNull
    public Integer grade;

    public Grade(Integer id, String name, Integer grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    @ManyToOne(name = "student_id", referencesField = "id") // name, student id
    public Student student;
}
