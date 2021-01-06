package com.example.projetooretorno.modelo;

import java.io.Serializable;

public class Filtro implements Serializable {

    private String instrumento;
    private String cidade;
    private String estado;

    public Filtro() {
    }

    public Filtro(String instrumento, String cidade, String estado) {
        this.instrumento = instrumento;
        this.cidade = cidade;
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Filtro{" +
                "instrumento='" + instrumento + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }

    public String getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
