package com.example.projetooretorno.controle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.projetooretorno.R;
import com.example.projetooretorno.helper.RecyclerItemClickListener;
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

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Professor p = professores.get(position);
                Intent i = new Intent(getBaseContext(), PerfilProfessor.class);
                i.putExtra("professorSelecionado", p);
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Professor p = professores.get(position);
                startActivity(new Intent(getBaseContext(), PerfilProfessor.class));
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));
    }

    public void listarProfessores(){
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference pesquisarProfessores = databaseReference.child("Professor");
        pesquisarProfessores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String idProfessor = dataSnapshot.child("id").getValue().toString();
                    String nomeProfessor = dataSnapshot.child("nome").getValue().toString();
                    String emailProfessor = dataSnapshot.child("email").getValue().toString();
                    //String enderecoProfessor = dataSnapshot.child("endereco").getValue().toString();
                    String caminhoFotoProfessor = "";
                    if(dataSnapshot.child("caminhoFoto").getValue()!=null){
                        caminhoFotoProfessor = dataSnapshot.child("caminhoFoto").getValue().toString();
                    }

                    Log.d("funciona", nomeProfessor + " " + emailProfessor + " "  + " " + caminhoFotoProfessor);
                    professores.add(new Professor(idProfessor, nomeProfessor, emailProfessor, caminhoFotoProfessor));
                    Log.d("funciona2", professores.toString());
                }
                ProfessorAdapter adapter = new ProfessorAdapter(professores, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}