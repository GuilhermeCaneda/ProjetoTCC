package com.example.projetooretorno.modelo;

public class Notificacao {

    private String idNotificacao;
    private String idProfessor;
    private String idAluno;
    private String remetente;
    private String destinatario;
    private String assunto;

    public Notificacao() {
    }


    public Notificacao(String idNotificacao, String idProfessor, String idAluno, String remetente, String destinatario, String assunto) {
        this.idNotificacao = idNotificacao;
        this.idProfessor = idProfessor;
        this.idAluno = idAluno;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.assunto = assunto;
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "idNotificacao='" + idNotificacao + '\'' +
                ", idProfessor='" + idProfessor + '\'' +
                ", idAluno='" + idAluno + '\'' +
                ", remetente='" + remetente + '\'' +
                ", destinatario='" + destinatario + '\'' +
                ", assunto='" + assunto + '\'' +
                '}';
    }



    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
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
