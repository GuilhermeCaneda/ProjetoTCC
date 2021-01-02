package com.example.projetooretorno.modelo;

import java.io.Serializable;

public class Avaliacao implements Serializable {

    private String idAvaliacao;
    private String idProfessor;
    private String idAluno;
    private String texto;

    public Avaliacao(String idAvaliacao, String idProfessor, String idAluno, String texto) {
        this.idAvaliacao = idAvaliacao;
        this.idProfessor = idProfessor;
        this.idAluno = idAluno;
        this.texto = texto;
    }

    public Avaliacao() {
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "idAvaliacao='" + idAvaliacao + '\'' +
                ", idProfessor='" + idProfessor + '\'' +
                ", idAluno='" + idAluno + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }

    public String getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(String idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public String getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(String idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(String idAluno) {
        this.idAluno = idAluno;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
