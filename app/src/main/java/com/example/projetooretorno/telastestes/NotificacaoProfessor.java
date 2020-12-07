package com.example.projetooretorno.telastestes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.projetooretorno.R;
import com.example.projetooretorno.adapter.NotificacaoProfessorAdapter;
import com.example.projetooretorno.adapter.ProfessorAdapter;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.helper.ProfessorFirebase;
import com.example.projetooretorno.modelo.Notificacao;
import com.example.projetooretorno.modelo.Professor;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificacaoProfessor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Notificacao> notificacoes = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao_professor);

        recyclerView = findViewById(R.id.recyclerViewNotificacaoProfessor);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        user = ProfessorFirebase.getProfessorAtual();
        Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
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
                    Log.d("fff", idNotificacao + " " + idProfessor + " " + idAluno);

                    if(idProfessor.equals(user.getUid())) {
                        notificacoes.add(new Notificacao(idNotificacao, idProfessor, idAluno));
                        Log.d("fff2", notificacoes.toString());
                    }
                }
                NotificacaoProfessorAdapter adapter = new NotificacaoProfessorAdapter(notificacoes, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        //
    }

}