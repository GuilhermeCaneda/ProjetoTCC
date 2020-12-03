package com.example.projetooretorno.modelo;

public class Matricula {

    private String idMatricula;
    private String idProfessor;
    private String idAluno;

    @Override
    public String toString() {
        return "Matricula{" +
                "idMatricula='" + idMatricula + '\'' +
                ", idProfessor='" + idProfessor + '\'' +
                ", idAluno='" + idAluno + '\'' +
                '}';
    }

    public Matricula() {
    }

    public Matricula(String idMatricula, String idProfessor, String idAluno) {
        this.idMatricula = idMatricula;
        this.idProfessor = idProfessor;
        this.idAluno = idAluno;
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
