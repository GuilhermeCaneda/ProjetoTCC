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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projetooretorno.helper.AlunoFirebase;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.MenuAluno;
import com.example.projetooretorno.R;
import com.example.projetooretorno.modelo.Aluno;
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

public class CadastroAluno extends AppCompatActivity {

    Aluno aluno;
    Button nCadastrar;
    EditText nNome, nEmail, nSenha;
    ProgressBar nProgressBar;
    ImageView nFoto;

    Spinner nFEtaria;
    String[] menu = new String[] {"Faixa etária", "10 a 14 anos", "15 a 18 anos", "19 a 25 anos", "26 anos ou mais"};
    ArrayAdapter<String> ab;

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
        setContentView(R.layout.activity_cadastro_aluno);

        nFoto = findViewById(R.id.fotoCadastroAluno);
        nFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelecionarFoto();
            }
        });

        nNome = findViewById(R.id.nomeCadastroAluno);
        nNome.requestFocus();
        nEmail = findViewById(R.id.emailCadastroAluno);
        nSenha = findViewById(R.id.senhaCadastroAluno);
        nFEtaria = findViewById(R.id.fetariaCadastroAluno);
        nProgressBar = findViewById(R.id.progressBarCadastroAluno);
        nProgressBar.setVisibility(View.GONE);
        ab = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu);
        nFEtaria.setAdapter(ab);

        nCadastrar = findViewById(R.id.buttonCadastroAluno);
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
                            CadastrarAluno(email, senha);
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

    private void CadastrarAluno(String email, String senha){
        aluno = new Aluno();
        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadastroAluno.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            aluno.setId(firebaseAuth.getUid());
                            aluno.setNome(nNome.getText().toString());
                            aluno.setEmail(nEmail.getText().toString());
                            switch(nFEtaria.getSelectedItemPosition()){
                                case 0: aluno.setF_etaria("");
                                    break;
                                case 1: aluno.setF_etaria("10 a 14 anos");
                                    break;
                                case 2: aluno.setF_etaria("15 a 18 anos");
                                    break;
                                case 3: aluno.setF_etaria("19 a 25 anos");
                                    break;
                                case 4: aluno.setF_etaria("26 anos ou mais");
                                    break;
                                default: aluno.setF_etaria("");
                                    break;
                            }
                            AlunoFirebase.atualizarNomeAluno(aluno.getNome());
                            databaseReference.child("Aluno").child(aluno.getId()).setValue(aluno);
                            SalvarImagem();
                            Toast.makeText(CadastroAluno.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MenuAluno.class));
                        }
                        else{
                            nProgressBar.setVisibility(View.GONE);
                            String erroExcecao = "";
                            try{ throw task.getException(); }
                            catch(FirebaseAuthWeakPasswordException e){ erroExcecao = "Digite uma senha mais forte!"; }
                            catch(FirebaseAuthInvalidCredentialsException e){ erroExcecao = "Digite um e-mail válido."; }
                            catch(FirebaseAuthUserCollisionException e){ erroExcecao = "Esta conta já foi cadastrada."; }
                            catch(Exception e){ erroExcecao = "Erro ao cadastrar usuário." + e.getMessage(); e.printStackTrace(); }
                            Toast.makeText(CadastroAluno.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();
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
        final StorageReference imagemRef = storageReference.child("FPerfilAluno/" + firebaseAuth.getUid() + ".png");

        if(dadosImagem!=null){
        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CadastroAluno.this, "Erro ao fazer upload da imagem.", Toast.LENGTH_SHORT).show();
                Log.i("storage", "erro ao fazer upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        atualizarFotoAluno(url);
                        Toast.makeText(CadastroAluno.this, "Sucesso ao atualizar a foto!", Toast.LENGTH_SHORT).show();
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

    private void atualizarFotoAluno(Uri url){
        AlunoFirebase.atualizarFotoAluno(url);
        aluno.setCaminhoFoto(url.toString());
        aluno.atualizar();
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth = Conexao.getFirebaseAuth();
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = Conexao.getFirebaseStorage();
        storageReference = firebaseStorage.getReference();
    }
}