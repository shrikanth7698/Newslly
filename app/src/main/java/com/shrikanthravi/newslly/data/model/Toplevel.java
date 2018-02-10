package com.shrikanthravi.newslly.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shrikanthravi on 20/12/17.
 */

public class Toplevel
{
    @SerializedName("CompleteSuggestion")
    private List<CompleteSuggestion> CompleteSuggestion;

    public List<CompleteSuggestion> getCompleteSuggestion ()
    {
        return CompleteSuggestion;
    }

    public void setCompleteSuggestion (List<CompleteSuggestion> CompleteSuggestion)
    {
        this.CompleteSuggestion = CompleteSuggestion;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [CompleteSuggestion = "+CompleteSuggestion+"]";
    }
}