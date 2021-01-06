package com.example.projetooretorno.controle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projetooretorno.R;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.helper.ProfessorFirebase;
import com.example.projetooretorno.modelo.Contato;
import com.example.projetooretorno.modelo.Professor;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditarContatosProfessor extends AppCompatActivity {

    EditText nInstagram, nWhatsapp, nEmail;
    Button nSalvar;

    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Contato contato;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contatos_professor);

        user = ProfessorFirebase.getProfessorAtual();

        mapearComponentes();

        nSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instagram = "", whatsapp = "";
                instagram =  nInstagram.getText().toString().trim();
                whatsapp = nWhatsapp.getText().toString().trim();
                salvarContatos(instagram, whatsapp);
            }
        });
    }

    public void salvarContatos(String instagram, String whatsapp){
        contato = new Contato(instagram, whatsapp, user.getEmail());
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Professor").child(user.getUid()).child("Contato").setValue(contato);
        Toast.makeText(this, "Contatos alterados com sucesso!", Toast.LENGTH_SHORT).show();
    }

    public void mapearComponentes(){
        nInstagram = findViewById(R.id.instagramEditarContatosProfessor);
        nWhatsapp = findViewById(R.id.whatsappEditarContatosProfessor);
        nEmail = findViewById(R.id.emailEditarContatosProfessor);
        nEmail.setText(user.getEmail());
        nEmail.setFocusable(false);
        nSalvar = findViewById(R.id.salvarEditarContatosProfessor);

        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Professor").child(user.getUid()).child("Contato").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String insta = "", wpp = "";
                if(snapshot.child("instagram").getValue()!=null){
                    insta = snapshot.child("instagram").getValue().toString();
                    nInstagram.setText(insta);
                }
                if(snapshot.child("whatsapp").getValue()!=null){
                    wpp = snapshot.child("whatsapp").getValue().toString();
                    nWhatsapp.setText(wpp);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}