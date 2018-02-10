package com.shrikanthravi.newslly.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shrikanthravi on 20/12/17.
 */

public class SearchSuggestion
{
    @SerializedName("toplevel")
    private Toplevel toplevel;

    public Toplevel getToplevel ()
    {
        return toplevel;
    }

    public void setToplevel (Toplevel toplevel)
    {
        this.toplevel = toplevel;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [toplevel = "+toplevel+"]";
    }
}