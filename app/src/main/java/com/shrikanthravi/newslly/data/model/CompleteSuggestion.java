package com.shrikanthravi.newslly.data.model;

/**
 * Created by shrikanthravi on 20/12/17.
 */


public class CompleteSuggestion
{
    private Suggestion suggestion;

    public Suggestion getSuggestion ()
    {
        return suggestion;
    }

    public void setSuggestion (Suggestion suggestion)
    {
        this.suggestion = suggestion;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [suggestion = "+suggestion+"]";
    }
}
