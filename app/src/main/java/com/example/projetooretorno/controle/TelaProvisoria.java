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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projetooretorno.R;
import com.example.projetooretorno.helper.AlunoFirebase;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.helper.ProfessorFirebase;
import com.example.projetooretorno.modelo.Professor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class TelaProvisoria extends AppCompatActivity {

    Professor professor;

    ImageView nFoto;
    TextView nAlterarFoto;
    EditText nNome, nInstrumentos, nEndereco, nBiografia, nEmail, nValor;
    Switch nDisponibilidade;
    ProgressBar nProgressBar;
    Button nSalvar, nEditarContatos;

    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    String nome, instrumentos, endereco, biografia, disponibilidade, valor, email;
    private static final int SELECAO_GALERIA = 200;

    String idProfessorAtual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_provisoria);

        mapearComponentes();
        professor = new Professor();
        user = AlunoFirebase.getAlunoAtual();
        professor = ProfessorFirebase.getDadosProfessorLogado();
        setarValores();

        nEditarContatos = findViewById(R.id.buttonEditarContatosTelaProvisoria);
        nEditarContatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditarContatosProfessor.class));
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

        nAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i,  SELECAO_GALERIA);
                }
            }
        });

        nSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeAtualiazado = nNome.getText().toString();
                ProfessorFirebase.atualizarNomeProfessor(nomeAtualiazado);
                professor.setNome(nomeAtualiazado);
                professor.setInstrumentos(nInstrumentos.getText().toString());
                professor.setEndereco(nEndereco.getText().toString());
                professor.setBiografia(nBiografia.getText().toString());
                if(nDisponibilidade.isChecked()){
                    professor.setDisponibilidade("Disponível");
                }else{
                    professor.setDisponibilidade("Indisponível");
                }
                professor.setValor(nValor.getText().toString());
                professor.atualizar();
                Toast.makeText(TelaProvisoria.this, "Dados atualizados!", Toast.LENGTH_SHORT).show();
            }
        });

        idProfessorAtual = ProfessorFirebase.getIdentificadorProfessor();

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
                    final StorageReference imagemRef = storageReference.child("FPerfilProfessor/" + idProfessorAtual + ".png");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TelaProvisoria.this, "Erro ao fazer upload da imagem.", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(TelaProvisoria.this, "Sucesso ao atualizar a foto!", Toast.LENGTH_SHORT).show();
                                    Log.i("storage", "sucesso ao fazer upload");
                                }
                            });
                        }
                    });
                }
            }catch (Exception e){ e.printStackTrace(); }
        }
    }

    private void atualizarFotoProfessor(Uri url){
        ProfessorFirebase.atualizarFotoProfessor(url);
        professor.setCaminhoFoto(url.toString());
        professor.atualizar();
    }

    private void mapearComponentes() {
        nFoto = findViewById(R.id.fotoTelaProvisoria);
        nNome = findViewById(R.id.nomeTelaProvisoria);
        nNome.requestFocus();
        nAlterarFoto = findViewById(R.id.alterarFotoTelaProvisoria);
        nValor = findViewById(R.id.valorTelaProvisoria);
        nInstrumentos = findViewById(R.id.instrumentosTelaProvisoria);
        nEndereco = findViewById(R.id.enderecoTelaProvisoria);
        nBiografia = findViewById(R.id.biografiaTelaProvisoria);
        nDisponibilidade = findViewById(R.id.disponibilidadeTelaProvisoria);
        nEmail = findViewById(R.id.emailTelaProvisoria);
        nEmail.setFocusable(false);
        nProgressBar = findViewById(R.id.progressBarTelaProvisoria);
        nProgressBar.setVisibility(View.GONE);
        nSalvar = findViewById(R.id.buttonConfirmarTelaProvisoria);
    }

    private void setarValores(){
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Professor").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nome = snapshot.child("nome").getValue().toString();
                instrumentos = snapshot.child("instrumentos").getValue().toString();
                endereco = snapshot.child("endereco").getValue().toString();
                biografia = snapshot.child("biografia").getValue().toString();
                disponibilidade = snapshot.child("disponibilidade").getValue().toString();
                valor = snapshot.child("valor").getValue().toString();
                email = snapshot.child("email").getValue().toString();

                nInstrumentos.setText(instrumentos);
                nEndereco.setText(endereco);
                nBiografia.setText(biografia);
                nDisponibilidade.setText(disponibilidade);
                if(disponibilidade.equals("Disponível")){
                    nDisponibilidade.setChecked(true);
                }else if(disponibilidade.equals("Indisponível")){
                    nDisponibilidade.setChecked(false);
                }
                nValor.setText(valor);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        nNome.setText(user.getDisplayName());
        nEmail.setText(user.getEmail());
        Uri url = user.getPhotoUrl();
        if(url != null){
            Glide.with(TelaProvisoria.this).load(url).into(nFoto);
        }else{
            nFoto.setImageResource(R.drawable.perfil);
        }


    }

    @Override
    protected void onStart(){
        super.onStart();

        firebaseStorage = Conexao.getFirebaseStorage();
        storageReference = firebaseStorage.getReference();
    }
}