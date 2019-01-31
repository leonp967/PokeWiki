package com.example.pokewiki.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TiposPokemonHeader {

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("results")
    @Expose
    private List<TipoPokemon> tiposPokemons;

    public Integer getCount() {
        return count;
    }

    public List<TipoPokemon> getTiposPokemons() {
        return tiposPokemons;
    }

}