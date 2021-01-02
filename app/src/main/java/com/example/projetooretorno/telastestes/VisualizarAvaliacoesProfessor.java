package com.example.projetooretorno.telastestes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.projetooretorno.R;
import com.example.projetooretorno.adapter.MeusProfessoresAdapter;
import com.example.projetooretorno.adapter.VisualizarAvaliacoesProfessorAdapter;
import com.example.projetooretorno.controle.CadastroAluno;
import com.example.projetooretorno.helper.AlunoFirebase;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Avaliacao;
import com.example.projetooretorno.modelo.Matricula;
import com.example.projetooretorno.modelo.Professor;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

public class VisualizarAvaliacoesProfessor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    FirebaseUser user;
    Professor professor;
    CardView cardView;

    ImageView nFoto;
    EditText nTexto;
    Button nEnviarAvaliacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_avaliacoes_professor);

        user = AlunoFirebase.getAlunoAtual();
        receberProfessor();

        cardView = findViewById(R.id.cardViewVisualizarAvaliacoesProfessor);
        verificarCardView();

        recyclerView = findViewById(R.id.recyclerViewVisualizarAvaliacoesProfessor);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nFoto = findViewById(R.id.fotoVisualizarAvaliacoesProfessor);
        nTexto = findViewById(R.id.textoVisualizarAvaliacoesProfessor);

        nEnviarAvaliacao = findViewById(R.id.enviarAvaliacaoVisualizarAvaliacoesProfessor);
        nEnviarAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idAvaliacaoo = randomUUID().toString();
                String idProfesorr = professor.getId();
                String idAlunoo = user.getUid();
                String textoo = nTexto.getText().toString();
                Avaliacao a = new Avaliacao(idAvaliacaoo, idProfesorr, idAlunoo, textoo);

                firebaseDatabase = Conexao.getFirebaseDatabase();
                databaseReference = firebaseDatabase.getReference();
                databaseReference.child("Professor").child(idProfesorr).child("Avaliacao").child(idAvaliacaoo).setValue(a);
                cardView.setVisibility(View.GONE);
            }
        });
        
        listarAvaliacoes();
    }


    public void listarAvaliacoes(){
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference pesquisarAvaliacoes = databaseReference.child("Professor").child(professor.getId()).child("Avaliacao");
        pesquisarAvaliacoes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String idAvaliacao = dataSnapshot.child("idAvaliacao").getValue().toString();
                    String idProfessor = dataSnapshot.child("idProfessor").getValue().toString();
                    String idAluno = dataSnapshot.child("idAluno").getValue().toString();
                    String texto = dataSnapshot.child("texto").getValue().toString();
                    avaliacoes.add(new Avaliacao(idAvaliacao, idProfessor, idAluno, texto));
                }
                VisualizarAvaliacoesProfessorAdapter adapter = new VisualizarAvaliacoesProfessorAdapter(avaliacoes, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void receberProfessor(){
        Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                professor = (Professor) bundle.getSerializable("professorAvaliacoes");
            }
    }


    public void verificarCardView(){
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference verificarAvaliacoes = databaseReference.child("Professor").child(professor.getId()).child("Avaliacao");
        verificarAvaliacoes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String idAlunooo = dataSnapshot.child("idAluno").getValue().toString();
                    if(idAlunooo.equals(user.getUid())){
                        cardView.setVisibility(View.GONE);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference verificarMatricula = databaseReference.child("Professor").child(professor.getId()).child("Matricula");
        verificarMatricula.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String idAlunoooo = dataSnapshot.child("idAluno").getValue().toString();
                    if(idAlunoooo.equals(user.getUid())){
                        cardView.setVisibility(View.VISIBLE);
                    }else{
                        cardView.setVisibility(View.GONE);
                    }
                    break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}