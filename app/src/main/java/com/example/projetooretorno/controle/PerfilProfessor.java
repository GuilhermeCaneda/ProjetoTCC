package com.example.projetooretorno.controle;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projetooretorno.R;
import com.example.projetooretorno.helper.AlunoFirebase;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Notificacao;
import com.example.projetooretorno.modelo.Professor;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.util.UUID.randomUUID;

public class PerfilProfessor extends AppCompatActivity {

    Professor professor;
    Notificacao notificacao;

    ImageView nFoto;
    TextView nNome, nEndereco, nMensalidade, nDisponibilidade;
    Button nMatricular, nAvaliacoes;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_professor);
        nFoto = findViewById(R.id.fotoPerfilProfessor);
        nNome = findViewById(R.id.nomePerfilProfessor);
        nEndereco = findViewById(R.id.enderecoPerfilProfessor);
        nMensalidade = findViewById(R.id.mensalidadePerfilProfessor);
        nDisponibilidade = findViewById(R.id.disponibilidadePerfilProfessor);
        nAvaliacoes = findViewById(R.id.avaliacoesPerfilProfessor);
        setarAtributos();


        nMatricular = findViewById(R.id.matricularPerfilProfessor);
        nMatricular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idNotificacao = randomUUID().toString();
                String idProfessor = professor.getId();
                String idAluno = firebaseUser.getUid();
                criarNotificacao(idNotificacao, idProfessor, idAluno);
            }
        });

    }

    public void criarNotificacao(String idNotificacao, String idProfessor, String idAluno){
        notificacao = new Notificacao(idNotificacao, idProfessor, idAluno);
        if(notificacao != null){
            Toast.makeText(PerfilProfessor.this, "Matrícula realizada com sucesso!", Toast.LENGTH_SHORT).show();
            databaseReference.child("Notificacao").child(notificacao.getIdNotificacao()).setValue(notificacao);
        }
    }

    public void setarAtributos(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            professor = (Professor) bundle.getSerializable("professorSelecionado");
        }
        nNome.setText(professor.getNome());
        nEndereco.setText(professor.getEndereco());
        if (professor.getCaminhoFoto() != null) {
            Uri uri = Uri.parse(professor.getCaminhoFoto());
            Glide.with(PerfilProfessor.this).load(uri).into(nFoto);
        }else{
            nFoto.setImageResource(R.drawable.perfil);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = AlunoFirebase.getAlunoAtual();
    }
}