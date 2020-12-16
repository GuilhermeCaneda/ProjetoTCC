package com.example.projetooretorno.telastestes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.projetooretorno.R;
import com.example.projetooretorno.adapter.NotificacaoAlunoAdapter;
import com.example.projetooretorno.adapter.NotificacaoProfessorAdapter;
import com.example.projetooretorno.helper.AlunoFirebase;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.helper.ProfessorFirebase;
import com.example.projetooretorno.modelo.Notificacao;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificacaoAluno extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Notificacao> notificacoes = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao_aluno);

        recyclerView = findViewById(R.id.recyclerViewNotificacaoAluno);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        user = AlunoFirebase.getAlunoAtual();
        listarNotificacoes();
    }

    public void listarNotificacoes(){
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference pesquisarNotificacoes = databaseReference.child("Notificacao");
        pesquisarNotificacoes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String idNotificacao = dataSnapshot.child("idNotificacao").getValue().toString();
                    String idProfessor = dataSnapshot.child("idProfessor").getValue().toString();
                    String idAluno = dataSnapshot.child("idAluno").getValue().toString();

                    String remetente = dataSnapshot.child("remetente").getValue().toString();
                    String destinatario = dataSnapshot.child("destinatario").getValue().toString();
                    String assunto = dataSnapshot.child("assunto").getValue().toString();

                    if(idAluno.equals(user.getUid()) && destinatario.equals("aluno")) {
                        notificacoes.add(new Notificacao(idNotificacao, idProfessor, idAluno, remetente, destinatario, assunto));
                    }
                }
                NotificacaoAlunoAdapter adapter = new NotificacaoAlunoAdapter(notificacoes, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}