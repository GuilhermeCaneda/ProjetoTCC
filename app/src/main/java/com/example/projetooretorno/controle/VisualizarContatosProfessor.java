package com.example.projetooretorno.controle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import com.example.projetooretorno.R;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Professor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VisualizarContatosProfessor extends AppCompatActivity {

    TextView nInstagram, nWhatsapp, nEmail;

    Professor professor;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_contatos_professor);

        mapearComponentes();
        setarAtributos();
    }

    private void setarAtributos() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            professor = (Professor) bundle.getSerializable("professorContatos");
        }
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Professor").child(professor.getId()).child("Contato").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String insta = "", wpp = "", email = "";
                if(snapshot.child("instagram").getValue()!=null){
                    insta = snapshot.child("instagram").getValue().toString();
                    nInstagram.setText(insta);
                }
                if(snapshot.child("whatsapp").getValue()!=null){
                    wpp = snapshot.child("whatsapp").getValue().toString();
                    nWhatsapp.setText(wpp);
                }
                if(snapshot.child("email").getValue()!=null){
                    email = snapshot.child("email").getValue().toString();
                    nEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void mapearComponentes() {
        nInstagram = findViewById(R.id.textViewInstaVisualizarContatosProfessor);
        nWhatsapp = findViewById(R.id.textViewWppVisualizarContatosProfessor);
        nEmail = findViewById(R.id.textViewEmailVisualizarContatosProfessor);
    }
}