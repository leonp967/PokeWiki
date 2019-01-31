package com.example.pokewiki.models;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {

    private List<String> habilidades = new ArrayList<>();
    private Integer experienciaBase;
    private List<String> formas = new ArrayList<>();
    private Integer altura;
    private List<String> movimentos = new ArrayList<>();
    private String nome;
    private String especie;
    private String imagemUrl;
    private List<Estatistica> estatisticas = new ArrayList<>();
    private List<String> tipos = new ArrayList<>();
    private Integer peso;

    public List<String> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<String> habilidades) {
        this.habilidades = habilidades;
    }

    public Integer getExperienciaBase() {
        return experienciaBase;
    }

    public void setExperienciaBase(Integer experienciaBase) {
        this.experienciaBase = experienciaBase;
    }

    public List<String> getFormas() {
        return formas;
    }

    public void setFormas(List<String> formas) {
        this.formas = formas;
    }

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
    }

    public List<String> getMovimentos() {
        return movimentos;
    }

    public void setMovimentos(List<String> movimentos) {
        this.movimentos = movimentos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public List<Estatistica> getEstatisticas() {
        return estatisticas;
    }

    public void setEstatisticas(List<Estatistica> estatisticas) {
        this.estatisticas = estatisticas;
    }

    public List<String> getTipos() {
        return tipos;
    }

    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }
}