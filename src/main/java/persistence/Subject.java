package persistence;

import annotations.NotNull;
import annotations.PrimaryKey;
import annotations.Table;

@Table
public class Subject {
    @PrimaryKey
    public Integer id;

    @NotNull
    public String name;
}