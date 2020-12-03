package com.example.projetooretorno.modelo;

import android.provider.ContactsContract;

import com.example.projetooretorno.helper.Conexao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class Aluno {

    private String id;
    private String nome;
    private String f_etaria;
    private String email;
    private String caminhoFoto;


    @Override
    public String toString() {
        return "Aluno{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }

    public Aluno() {
    }

    public void atualizar(){
        FirebaseDatabase firebaseDatabase = Conexao.getFirebaseDatabase();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference alunosRef = databaseReference.child("Aluno").child(getId());

        Map<String, Object> valoresAluno = converterParaMap();
        alunosRef.updateChildren(valoresAluno);
    }

    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> alunoMap = new HashMap<>();
        alunoMap.put("email", getEmail());
        alunoMap.put("nome", getNome());
        alunoMap.put("id", getId());
        alunoMap.put("caminhoFoto", getCaminhoFoto());

        return alunoMap;
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

    public String getF_etaria() {
        return f_etaria;
    }

    public void setF_etaria(String f_etaria) {
        this.f_etaria = f_etaria;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }
}
