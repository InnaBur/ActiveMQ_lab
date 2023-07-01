package com.thirdTask;

import jakarta.validation.constraints.*;

import java.io.Serializable;

public class MyMessage implements Serializable {

    @NotNull(message = "You need to input a name")
    @Size(min = 7, message = "Name length must be longer then 6 symbols")
    @Pattern(regexp = "^A?.*a.*", message = "Must be at least one letter 'а'")
    private String name;

    @NotNull(message = "You need to input an eddr")
    @Size(min = 13, max = 14, message = "EDDR must be 13 or 14 symbols long")
    @ControlDigit
    private String eddr;
    @Min(value = 10, message = "Must be more than 9")
    private int count;
    @NotNull(message = "Date can not be null")
    private String created_at;

    public MyMessage() {
    }

    public MyMessage(String name) {
        this.name = name;
    }

    public MyMessage(String name, String eddr, int count, String created_at) {
        this.name = name;
        this.eddr = eddr;
        this.count = count;
        this.created_at = created_at;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEddr() {
        return eddr;
    }

    public void setEddr(String eddr) {
        if (eddr.charAt(8)=='-') {
            this.eddr = eddr.replace("-", "");
        } else {
            this.eddr = eddr;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "MyMessage{" +
                "name='" + name + '\'' +
                ", eddr='" + eddr + '\'' +
                ", count=" + count +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
