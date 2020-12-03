package com.example.projetooretorno.modelo;

public class Notificacao {

    private String idNotificacao;
    private String idProfessor;
    private String idAluno;

    public Notificacao() {
    }

    public Notificacao(String idNotificacao, String idProfessor, String idAluno) {
        this.idNotificacao = idNotificacao;
        this.idProfessor = idProfessor;
        this.idAluno = idAluno;
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "idNotificacao='" + idNotificacao + '\'' +
                ", idProfessor='" + idProfessor + '\'' +
                ", idAluno='" + idAluno + '\'' +
                '}';
    }

    public String getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(String idNotificacao) {
        this.idNotificacao = idNotificacao;
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
