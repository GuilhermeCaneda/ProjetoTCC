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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projetooretorno.R;
import com.example.projetooretorno.helper.AlunoFirebase;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Aluno;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditarPerfilAluno extends AppCompatActivity {

    EditText nEmail, nNome;
    ImageView nFoto;
    TextView nAlterarFoto;
    ProgressBar nProgressBar;
    Button nConfirmar;

    Aluno aluno;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseUser user;
    private static final int SELECAO_GALERIA = 200;
    private String idAlunoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_aluno);

        mapearComponentes();
        aluno = new Aluno();
        aluno = AlunoFirebase.getDadosAlunoLogado();
        user = AlunoFirebase.getAlunoAtual();
        nNome.setText(user.getDisplayName());
        nEmail.setText(user.getEmail());

        Uri url = user.getPhotoUrl();
        if(url != null){
            Glide.with(EditarPerfilAluno.this).load(url).into(nFoto);
        }else{
            nFoto.setImageResource(R.drawable.perfil);
        }

        nConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeAtualiazado = nNome.getText().toString();
                AlunoFirebase.atualizarNomeAluno(nomeAtualiazado);
                aluno.setNome(nomeAtualiazado);
                aluno.atualizar();
                Toast.makeText(EditarPerfilAluno.this, "Dados atualizados!", Toast.LENGTH_SHORT).show();

            }
        });

        nAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,  SELECAO_GALERIA);
                }
            }
        });

        nFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,  SELECAO_GALERIA);
                }
            }
        });

        idAlunoAtual = AlunoFirebase.getIdentificadorAluno();
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
                    byte[] dadosImagem = baos.toByteArray();
                    final StorageReference imagemRef = storageReference.child("FPerfilAluno/" + idAlunoAtual + ".png");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditarPerfilAluno.this, "Erro ao fazer upload da imagem.", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(EditarPerfilAluno.this, "Sucesso ao atualizar a foto!", Toast.LENGTH_SHORT).show();
                                    Log.i("storage", "sucesso ao fazer upload");
                                }
                            });
                        }
                    });
                }
            }catch (Exception e){ e.printStackTrace(); }
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
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = Conexao.getFirebaseStorage();
        storageReference = firebaseStorage.getReference();
    }

    private void mapearComponentes() {
        nNome = findViewById(R.id.nomeEditarPerfilAluno);
        nNome.requestFocus();
        nEmail = findViewById(R.id.emailEditarPerfilAluno);
        nEmail.setFocusable(false);
        nProgressBar = findViewById(R.id.progressBarEditarPerfilAluno);
        nProgressBar.setVisibility(View.GONE);
        nFoto = findViewById(R.id.fotoEditarPerfilAluno);
        nAlterarFoto = findViewById(R.id.alterarFotoEditarPerfilAluno);
        nConfirmar = findViewById(R.id.buttonEditarPerfilAluno);
    }
}