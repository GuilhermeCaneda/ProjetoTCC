package com.example.projetooretorno.modelo;

import java.io.Serializable;

public class Matricula implements Serializable {

    private String idMatricula;
    private String idProfessor;
    private String idAluno;
    private String dataPagamento;
    private String valor;

    @Override
    public String toString() {
        return "Matricula{" +
                "idMatricula='" + idMatricula + '\'' +
                ", idProfessor='" + idProfessor + '\'' +
                ", idAluno='" + idAluno + '\'' +
                ", dataPagamento='" + dataPagamento + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }

    public Matricula() {
    }

    public Matricula(String idMatricula, String idProfessor, String idAluno) {
        this.idMatricula = idMatricula;
        this.idProfessor = idProfessor;
        this.idAluno = idAluno;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(String dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(String idMatricula) {
        this.idMatricula = idMatricula;
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
}
