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
import com.example.projetooretorno.helper.AlunoFirebase;
import com.example.projetooretorno.helper.RecyclerItemClickListener;
import com.example.projetooretorno.adapter.ProfessorAdapter;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Filtro;
import com.example.projetooretorno.modelo.Matricula;
import com.example.projetooretorno.modelo.Professor;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class BuscarProfessor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Professor> professores = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    FirebaseUser user;

    Filtro filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_professor);

        receberFiltro();
        recyclerView = findViewById(R.id.recyclerViewBuscarProfessor);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        user = AlunoFirebase.getAlunoAtual();

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
                Intent i = new Intent(getBaseContext(), PerfilProfessor.class);
                i.putExtra("professorSelecionado", p);
                startActivity(i);
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

        //Query pesquisarProfessores = databaseReference.child("Professor").orderByChild("instrumentos").equalTo(%filtro.getInstrumento()%);

        pesquisarProfessores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                professores.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                     String idProfessor = dataSnapshot.child("id").getValue().toString();
                     String nomeProfessor = dataSnapshot.child("nome").getValue().toString();
                     String emailProfessor = dataSnapshot.child("email").getValue().toString();
                     String enderecoProfessor = dataSnapshot.child("endereco").getValue().toString();
                     //String instrumentosProfessor = dataSnapshot.child("instrumentos").getValue().toString();
                     String caminhoFotoProfessor = "";
                     if(dataSnapshot.child("caminhoFoto").getValue()!=null) {
                         caminhoFotoProfessor = dataSnapshot.child("caminhoFoto").getValue().toString();
                     }

                    /*
                     instrumentosProfessor = deAccent(instrumentosProfessor).toUpperCase();
                     if(instrumentosProfessor.equals(filtro.getInstrumento())){
                        professores.add(new Professor(idProfessor, nomeProfessor, emailProfessor, caminhoFotoProfessor, enderecoProfessor));
                     }*/

                    professores.add(new Professor(idProfessor, nomeProfessor, emailProfessor, caminhoFotoProfessor, enderecoProfessor));
                }
                ProfessorAdapter adapter = new ProfessorAdapter(professores, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void receberFiltro(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            filtro = (Filtro) bundle.getSerializable("filtro");
        }
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}