package com.shrikanthravi.newslly.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shrikanthravi on 20/12/17.
 */
public class Suggestion
{
    @SerializedName("data")
    private String data;

    public String getData ()
    {
        return data;
    }

    public Suggestion(String data) {
        this.data = data;
    }

    public void setData (String data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+"]";
    }
}

