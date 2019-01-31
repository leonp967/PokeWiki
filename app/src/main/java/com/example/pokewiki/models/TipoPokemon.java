package com.example.pokewiki.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TipoPokemon {

    @SerializedName("name")
    @Expose
    private String nome;

    @SerializedName("url")
    @Expose
    private String url;

    public String getNome() {
        return nome;
    }

    public String getUrl() {
        return url;
    }

}