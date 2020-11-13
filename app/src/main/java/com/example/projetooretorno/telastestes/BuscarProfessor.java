package com.example.projetooretorno.telastestes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.projetooretorno.R;
import com.example.projetooretorno.adapter.ProfessorAdapter;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Professor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuscarProfessor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Professor> professores = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_professor);

        recyclerView = findViewById(R.id.recyclerViewBuscarProfessor);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listarProfessores();
        ProfessorAdapter adapter = new ProfessorAdapter(professores);
        recyclerView.setAdapter(adapter);
    }

    public void listarProfessores(){
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference pesquisarProfessores = databaseReference.child("Professor");
        pesquisarProfessores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String nomeTeste = dataSnapshot.child("nome").getValue().toString();
                    String emailTeste = dataSnapshot.child("email").getValue().toString();
                    String enderecoTeste = dataSnapshot.child("endereco").getValue().toString();
                    String caminhoFotoTeste = dataSnapshot.child("caminhoFoto").getValue().toString();
                    Log.d("funciona", nomeTeste + " " + emailTeste + " " + enderecoTeste + " " + caminhoFotoTeste);
                    professores.add(new Professor(nomeTeste, emailTeste, enderecoTeste, caminhoFotoTeste));
                    Log.d("funciona2", professores.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}