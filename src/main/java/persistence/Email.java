package persistence;

import annotations.*;

@Table
public class Email {
    @Column
    @PrimaryKey
    @NotNull
    public Integer id;

    @Column
    @NotNull
    public String email;

    @OneToOne(value = "id", referenced = "id") // student id, email id
    public Student student;
}
