package com.example.projetooretorno.modelo;

import java.io.Serializable;

public class Contato implements Serializable {

    private String instagram;
    private String whatsapp;
    private String email;

    public Contato() {
    }

    public Contato(String instagram, String whatsapp, String email) {
        this.instagram = instagram;
        this.whatsapp = whatsapp;
        this.email = email;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
