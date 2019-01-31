package com.example.pokewiki.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Estatistica {

    @SerializedName("base_stat")
    @Expose
    private Integer valorBase;

    @SerializedName("stat")
    @Expose
    private String nome;

    public Integer getValorBase() {
        return valorBase;
    }

    public void setValorBase(Integer valorBase) {
        this.valorBase = valorBase;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
