package com.example.projetooretorno.telastestes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projetooretorno.R;
import com.example.projetooretorno.controle.CadastroAluno;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Matricula;
import com.example.projetooretorno.modelo.Notificacao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.util.UUID.randomUUID;

public class EditarMeusAlunos extends AppCompatActivity {

    ImageView nFoto;
    EditText nEmail, nNome, nPagamento, nDataPagamento;
    Button nConfirmar, nCancelar;
    ProgressBar nProgressBar;

    Matricula matricula;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, pesquisa;

    String nome = "", email = "", valor = "", dataPagamento = "", caminhoFoto = "";
    Notificacao notificacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_meus_alunos);

        mapearComponentes();
        setarAtributos();
        
        nConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matricula.setValor(nPagamento.getText().toString());
                matricula.setDataPagamento(nDataPagamento.getText().toString());
                databaseReference.child("Professor").child(matricula.getIdProfessor()).child("Matricula").child(matricula.getIdMatricula()).setValue(matricula);
                databaseReference.child("Aluno").child(matricula.getIdAluno()).child("Matricula").child(matricula.getIdMatricula()).setValue(matricula);
                Toast.makeText(EditarMeusAlunos.this, "Os dados foram alterados com sucesso!", Toast.LENGTH_SHORT).show();
                notificacao = new Notificacao(randomUUID().toString(), matricula.getIdProfessor(), matricula.getIdAluno(), "professor", "aluno", "edicao_matricula");
                databaseReference.child("Notificacao").child(notificacao.getIdNotificacao()).setValue(notificacao);
            }
        });

        nCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditarMeusAlunos.this, "Matrícula cancelada!", Toast.LENGTH_SHORT).show();
                notificacao = new Notificacao(randomUUID().toString(), matricula.getIdProfessor(), matricula.getIdAluno(), "professor", "aluno", "remocao_matricula");
                databaseReference.child("Notificacao").child(notificacao.getIdNotificacao()).setValue(notificacao);
                databaseReference.child("Professor").child(matricula.getIdProfessor()).child("Matricula").child(matricula.getIdMatricula()).removeValue();
                databaseReference.child("Aluno").child(matricula.getIdAluno()).child("Matricula").child(matricula.getIdMatricula()).removeValue();
            }
        });

    }


    public void setarAtributos(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            matricula = (Matricula) bundle.getSerializable("matriculaSelecionada");
        }

        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        pesquisa = databaseReference.child("Aluno").child(matricula.getIdAluno());
        pesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nome = snapshot.child("nome").getValue().toString();
                email = snapshot.child("email").getValue().toString();
                if(snapshot.child("caminhoFoto").getValue()!=null){
                    caminhoFoto = snapshot.child("caminhoFoto").getValue().toString();
                }
                nNome.setText(nome);
                nEmail.setText(email);
                if (caminhoFoto!="") {
                    Uri uri = Uri.parse(caminhoFoto);
                    Glide.with(EditarMeusAlunos.this).load(uri).into(nFoto);
                }else{
                    nFoto.setImageResource(R.drawable.perfil);
                }

                pesquisa = databaseReference.child("Aluno").child(matricula.getIdAluno()).child("Matricula").child(matricula.getIdMatricula());
                pesquisa.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("valor").getValue()!=null) {
                            valor = snapshot.child("valor").getValue().toString();
                        }
                        if(snapshot.child("dataPagamento").getValue()!=null){
                            dataPagamento = snapshot.child("dataPagamento").getValue().toString();
                        }
                        nPagamento.setText(valor);
                        nDataPagamento.setText(dataPagamento);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void mapearComponentes() {
        nNome = findViewById(R.id.nomeEditarMeusAlunos);
        nNome.setFocusable(false);
        nEmail = findViewById(R.id.emailEditarMeusAlunos);
        nEmail.setFocusable(false);
        nPagamento = findViewById(R.id.valorMensalidadeEditarMeusAlunos);
        nDataPagamento = findViewById(R.id.dataPagamentoEditarMeusAlunos);
        nProgressBar = findViewById(R.id.progressBarEditarMeusAlunos);
        nProgressBar.setVisibility(View.GONE);
        nFoto = findViewById(R.id.fotoEditarMeusAlunos);
        nConfirmar = findViewById(R.id.confirmarEditarMeusAlunos);
        nCancelar = findViewById(R.id.cancelarEditarMeusAlunos);
    }
}