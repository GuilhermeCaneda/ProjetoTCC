package com.example.projetooretorno.controle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.projetooretorno.Menu;
import com.example.projetooretorno.R;
import com.example.projetooretorno.modelo.Aluno;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.io.FileNotFoundException;

public class CadastroAluno extends AppCompatActivity {

    Aluno aluno;
    Button nCadastrar;
    EditText nNome, nEmail, nSenha;
    ProgressBar nProgressBar;

    Spinner nFEtaria;
    String[] menu = new String[] {"Faixa etária", "10 a 14 anos", "15 a 18 anos", "19 a 25 anos", "26 anos ou mais"};
    ArrayAdapter<String> ab;

    ImageView nFoto;
    public Uri uri;
    private byte [] imagem = null;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;

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
        nEmail = findViewById(R.id.emailCadastroAluno);
        nSenha = findViewById(R.id.senhaCadastroAluno);
        nFEtaria = findViewById(R.id.fetariaCadastroAluno);
        nProgressBar = findViewById(R.id.progressBarCadastroAluno);
        nProgressBar.setVisibility(View.GONE);
        ab = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu);
        nFEtaria.setAdapter(ab);
        nNome.requestFocus();

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
                            startActivity(new Intent(getApplicationContext(), Menu.class));
                        }
                        else{
                            nProgressBar.setVisibility(View.GONE);
                            String erroExcecao = "";
                            try{ throw task.getException(); }
                            catch (FirebaseAuthWeakPasswordException e){ erroExcecao = "Digite uma senha mais forte!"; }
                            catch(FirebaseAuthInvalidCredentialsException e){ erroExcecao = "Digite um e-mail válido."; }
                            catch (FirebaseAuthUserCollisionException e){ erroExcecao = "Esta conta já foi cadastrada."; }
                            catch(Exception e){ erroExcecao = "Erro ao cadastrar usuário." + e.getMessage(); e.printStackTrace(); }
                            Toast.makeText(CadastroAluno.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();
                        }}});
    }

    private void SelecionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    private void SalvarImagem() {
        String path = "FPerfilAluno/" + aluno.getId() + ".png";
        StorageReference firebaseStorageReference = firebaseStorage.getReference(path);
        UploadTask uploadTask = firebaseStorageReference.putBytes(imagem);
        uploadTask.addOnCompleteListener(CadastroAluno.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Log.i("MA", "Deu bom" + uri);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth = Conexao.getFirebaseAuth();
        firebaseStorage = Conexao.getFirebaseStorage();
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        /*
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser!=null){
            LogarUsuario();
        }
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                nFoto.setImageURI(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] img = getBitmapAsByteArray(bitmap);
            imagem = img;
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 , outputStream);
        return outputStream.toByteArray();
    }
}