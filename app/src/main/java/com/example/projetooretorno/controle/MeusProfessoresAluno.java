package com.example.projetooretorno.controle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.projetooretorno.R;
import com.example.projetooretorno.adapter.MeusAlunosAdapter;
import com.example.projetooretorno.adapter.MeusProfessoresAdapter;
import com.example.projetooretorno.helper.AlunoFirebase;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.helper.ProfessorFirebase;
import com.example.projetooretorno.helper.RecyclerItemClickListener;
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

public class MeusProfessoresAluno extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Matricula> matriculas = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_professores_aluno);

        recyclerView = findViewById(R.id.recyclerViewMeusProfessores);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        user = AlunoFirebase.getAlunoAtual();
        listarMatriculas();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Matricula m = matriculas.get(position);
                Professor p = new Professor(m.getIdProfessor());
                Intent i = new Intent(getBaseContext(), PerfilProfessor.class);
                i.putExtra("professorSelecionado", p);
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Matricula m = matriculas.get(position);
                Professor p = new Professor(m.getIdProfessor());
                Intent i = new Intent(getBaseContext(), PerfilProfessor.class);
                i.putExtra("professorSelecionado", p);
                startActivity(i);
            }
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));
    }

    public void listarMatriculas(){
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference pesquisarMatriculas = databaseReference.child("Aluno").child(user.getUid()).child("Matricula");
        pesquisarMatriculas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String idMatricula = dataSnapshot.child("idMatricula").getValue().toString();
                    String idProfessor = dataSnapshot.child("idProfessor").getValue().toString();
                    String idAluno = dataSnapshot.child("idAluno").getValue().toString();
                    matriculas.add(new Matricula(idMatricula, idProfessor, idAluno));
                }
                MeusProfessoresAdapter adapter = new MeusProfessoresAdapter(matriculas, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}