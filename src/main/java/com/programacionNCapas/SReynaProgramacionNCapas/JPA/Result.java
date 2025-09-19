package com.programacionNCapas.SReynaProgramacionNCapas.JPA;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class Result {

    public boolean correct;

    public Exception ex;

    public String errorMessage;

    public List<Object> objects;

    public Object object;

    @JsonIgnore
    public int status;

}
