package com.example.projetooretorno.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.projetooretorno.modelo.Aluno;
import com.example.projetooretorno.modelo.Professor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfessorFirebase {

    public static FirebaseUser getProfessorAtual(){
        FirebaseAuth professor = Conexao.getFirebaseAuth();
        return professor.getCurrentUser();
    }

    public static String getIdentificadorProfessor(){
        return getProfessorAtual().getUid();
    }

    public static void atualizarNomeProfessor(String nome){
        try{
            FirebaseUser user = getProfessorAtual();
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.i("Perfil", "Erro ao atualizar o perfil.");
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void atualizarFotoProfessor(Uri url){
        try{
            FirebaseUser user = getProfessorAtual();
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();
            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.i("Perfil", "Erro ao atualizar a foto.");
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static Professor getDadosProfessorLogado(){
        FirebaseUser user = getProfessorAtual();
        Professor professor = new Professor();
        professor.setId(user.getUid());
        professor.setNome(user.getDisplayName());
        professor.setEmail(user.getEmail());

        if(user.getPhotoUrl() == null){
            professor.setCaminhoFoto("");
        }else{
            professor.setCaminhoFoto(user.getPhotoUrl().toString());
        }
        return professor;
    }
}
