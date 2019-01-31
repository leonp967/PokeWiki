package com.example.pokewiki.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PokemonHeader {

    @SerializedName("name")
    @Expose
    private String nome;
    @SerializedName("url")
    @Expose
    private String url;

    public String getNome() {
        return nome;
    }

    public void setName(String name) {
        this.nome = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
