package com.example.projetooretorno.controle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projetooretorno.R;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.helper.ProfessorFirebase;
import com.example.projetooretorno.modelo.Professor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class CadastroProfessor extends AppCompatActivity {

    Professor professor;
    Button nCadastrar;
    EditText nNome, nEmail, nSenha;
    ProgressBar nProgressBar;
    ImageView nFoto;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;

    private static final int SELECAO_GALERIA = 200;
    byte[] dadosImagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_professor);

        nFoto = findViewById(R.id.fotoCadastroProfessor);
        nFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelecionarFoto();
            }
        });

        nNome = findViewById(R.id.nomeCadastroProfessor);
        nNome.requestFocus();
        nEmail = findViewById(R.id.emailCadastroProfessor);
        nSenha = findViewById(R.id.senhaCadastroProfessor);
        nProgressBar = findViewById(R.id.progressBarCadastroProfessor);
        nProgressBar.setVisibility(View.GONE);

        nCadastrar = findViewById(R.id.buttonCadastroProfessor);
        nCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nNome.getText().toString().trim();
                String email = nEmail.getText().toString().trim();
                String senha = nSenha.getText().toString().trim();
                if(!nome.isEmpty()){
                    if(!email.isEmpty()){
                        if(!senha.isEmpty()){
                            nProgressBar.setVisibility(View.VISIBLE);
                            CadastrarProfessor(email, senha);
                        }else{
                            Toast.makeText(getBaseContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getBaseContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void CadastrarProfessor(String email, String senha){
        professor = new Professor();
        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadastroProfessor.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            professor.setId(firebaseAuth.getUid());
                            professor.setNome(nNome.getText().toString());
                            professor.setEmail(nEmail.getText().toString());
                            ProfessorFirebase.atualizarNomeProfessor(professor.getNome());

                            databaseReference.child("Professor").child(professor.getId()).setValue(professor);
                            Toast.makeText(CadastroProfessor.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();


                            SalvarImagem();
                            nProgressBar.setVisibility(View.GONE);
                            //startActivity(new Intent(getApplicationContext(), Menu.class));
                        }
                        else{
                            nProgressBar.setVisibility(View.GONE);
                            String erroExcecao = "";
                            try{ throw task.getException(); }
                            catch(FirebaseAuthWeakPasswordException e){ erroExcecao = "Digite uma senha mais forte!"; }
                            catch(FirebaseAuthInvalidCredentialsException e){ erroExcecao = "Digite um e-mail válido."; }
                            catch(FirebaseAuthUserCollisionException e){ erroExcecao = "Esta conta já foi cadastrada."; }
                            catch(Exception e){ erroExcecao = "Erro ao cadastrar usuário." + e.getMessage(); e.printStackTrace(); }
                            Toast.makeText(CadastroProfessor.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();
                        }}});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try{
                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                if(imagem != null){
                    nFoto.setImageBitmap(imagem);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.PNG, 70, baos);
                    dadosImagem = baos.toByteArray();
                }
            }catch (Exception e){ e.printStackTrace(); }
        }
    }

    private void SalvarImagem() {
        final StorageReference imagemRef = storageReference.child("FPerfilProfessor/" + firebaseAuth.getUid() + ".png");
        if(dadosImagem!=null){
            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CadastroProfessor.this, "Erro ao fazer upload da imagem.", Toast.LENGTH_SHORT).show();
                    Log.i("storage", "erro ao fazer upload");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri url = task.getResult();
                            atualizarFotoProfessor(url);
                            Toast.makeText(CadastroProfessor.this, "Sucesso ao atualizar a foto!", Toast.LENGTH_SHORT).show();
                            Log.i("storage", "sucesso ao fazer upload");
                        }
                    });
                }
            });}
    }

    private void SelecionarFoto() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivityForResult(i,  SELECAO_GALERIA);
        }
    }

    private void atualizarFotoProfessor(Uri url){
        ProfessorFirebase.atualizarFotoProfessor(url);
        professor.setCaminhoFoto(url.toString());
        professor.atualizar();
    }

    @Override
    protected void onStart(){
        super.onStart();

        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = Conexao.getFirebaseAuth();
        firebaseStorage = Conexao.getFirebaseStorage();
        storageReference = firebaseStorage.getReference();
    }
}