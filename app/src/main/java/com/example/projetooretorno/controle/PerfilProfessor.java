package com.example.projetooretorno.controle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.util.UUID.randomUUID;

public class PerfilProfessor extends AppCompatActivity {

    Professor professor;
    Notificacao notificacao;

    ImageView nFoto;
    TextView nNome, nEndereco, nMensalidade, nDisponibilidade, nBiografia, nInstrumentos;
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
        nBiografia = findViewById(R.id.biografiaPerfilProfessor);
        nInstrumentos = findViewById(R.id.instrumentosPerfilProfessor);
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
        notificacao = new Notificacao(idNotificacao, idProfessor, idAluno, "aluno", "professor", "solicitacao_matricula");


        if(notificacao != null){
            Toast.makeText(PerfilProfessor.this, "A notificação foi enviada com sucesso!", Toast.LENGTH_SHORT).show();
            databaseReference.child("Notificacao").child(notificacao.getIdNotificacao()).setValue(notificacao).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    nMatricular.setFocusable(false);
                    nMatricular.setText("Notificação Pendente");
                }
            });
        }
    }

    public void setarAtributos(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            professor = (Professor) bundle.getSerializable("professorSelecionado");
        }
        nNome.setText(professor.getNome());

        /*
        nEndereco.setText(professor.getEndereco());
        nInstrumentos.setText(professor.getInstrumentos());
        nBiografia.setText(professor.getBiografia());
        nMensalidade.setText(professor.getValor());
        nBiografia.setText(professor.getBiografia());
        nDisponibilidade.setText(professor.getDisponibilidade());
        */

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