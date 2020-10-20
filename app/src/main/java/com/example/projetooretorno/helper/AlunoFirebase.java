package com.example.projetooretorno.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.projetooretorno.modelo.Aluno;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AlunoFirebase {

    public static FirebaseUser getAlunoAtual(){
        FirebaseAuth aluno = Conexao.getFirebaseAuth();
        return aluno.getCurrentUser();
    }

    public static String getIdentificadorAluno(){
        return getAlunoAtual().getUid();
    }

    public static void atualizarNomeAluno(String nome){
        try{
            FirebaseUser user = getAlunoAtual();
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

    public static void atualizarFotoAluno(Uri url){
        try{
            FirebaseUser user = getAlunoAtual();
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


    public static Aluno getDadosAlunoLogado(){
        FirebaseUser user = getAlunoAtual();
        Aluno aluno = new Aluno();
        aluno.setId(user.getUid());
        aluno.setNome(user.getDisplayName());
        aluno.setEmail(user.getEmail());

        if(user.getPhotoUrl() == null){
            aluno.setCaminhoFoto("");
        }else{
            aluno.setCaminhoFoto(user.getPhotoUrl().toString());
        }
        return aluno;
    }
}
