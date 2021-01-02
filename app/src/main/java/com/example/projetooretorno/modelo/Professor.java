package com.example.projetooretorno.modelo;

import android.net.Uri;

import com.example.projetooretorno.helper.Conexao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Professor implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String caminhoFoto;
    private String estado;
    private String cidade;

    private String endereco;
    private String biografia;
    private String instrumentos;
    private String disponibilidade;
    private String valor;

    public Professor(String id, String nome, String email, String caminhoFoto, String estado, String cidade, String endereco, String biografia, String instrumentos, String disponibilidade, String valor) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.caminhoFoto = caminhoFoto;
        this.estado = estado;
        this.cidade = cidade;
        this.endereco = endereco;
        this.biografia = biografia;
        this.instrumentos = instrumentos;
        this.disponibilidade = disponibilidade;
        this.valor = valor;
    }

    public Professor() {
    }

    public Professor(String id, String nome, String email, String caminhoFoto, String endereco) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.caminhoFoto = caminhoFoto;
    }

    public Professor(String id) {
        this.id = id;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getInstrumentos() {
        return instrumentos;
    }

    public void setInstrumentos(String instrumentos) {
        this.instrumentos = instrumentos;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void atualizar(){
        FirebaseDatabase firebaseDatabase = Conexao.getFirebaseDatabase();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference professoresRef = databaseReference.child("Professor").child(getId());

        Map<String, Object> valorProfessor = converterParaMap();
        professoresRef.updateChildren(valorProfessor);
    }

    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> professorMap = new HashMap<>();
        professorMap.put("email", getEmail());
        professorMap.put("nome", getNome());
        professorMap.put("id", getId());
        professorMap.put("caminhoFoto", getCaminhoFoto());

        professorMap.put("instrumentos", getInstrumentos());
        professorMap.put("valor", getValor());
        professorMap.put("biografia", getBiografia());
        professorMap.put("endereco", getEndereco());
        professorMap.put("disponibilidade", getDisponibilidade());


        return professorMap;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

}
