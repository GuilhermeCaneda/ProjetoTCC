package com.example.projetooretorno.controle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projetooretorno.R;
import com.example.projetooretorno.helper.AlunoFirebase;
import com.example.projetooretorno.helper.Conexao;
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

import static java.util.UUID.randomUUID;

public class PerfilProfessor extends AppCompatActivity {

    Professor professor;
    Notificacao notificacao;

    ImageView nFoto;
    TextView nNome, nEndereco, nValor, nDisponibilidade, nBiografia, nInstrumentos;
    Button nMatricular, nAvaliacoes, nCancelarMatricula, nContatos;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, pesquisa;
    FirebaseUser firebaseUser;

    String idNotificacaoo, idProfessorr, idAlunoo;
    List<Notificacao> notificacoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_professor);

        firebaseUser = AlunoFirebase.getAlunoAtual();

        nFoto = findViewById(R.id.fotoPerfilProfessor);
        nNome = findViewById(R.id.nomePerfilProfessor);
        nEndereco = findViewById(R.id.enderecoPerfilProfessor);
        nValor = findViewById(R.id.mensalidadePerfilProfessor);
        nDisponibilidade = findViewById(R.id.disponibilidadePerfilProfessor);
        nBiografia = findViewById(R.id.biografiaPerfilProfessor);
        nInstrumentos = findViewById(R.id.instrumentosPerfilProfessor);
        setarAtributos();

        nContatos = findViewById(R.id.contatosPerfilProfessor);
        nContatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Professor prof = new Professor();
                prof.setId(professor.getId());
                Intent intent = new Intent(getBaseContext(), VisualizarContatosProfessor.class);
                intent.putExtra("professorContatos", prof);
                startActivity(intent);
            }
        });

        nAvaliacoes = findViewById(R.id.avaliacoesPerfilProfessor);
        nAvaliacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Professor prof = new Professor();
                prof.setId(professor.getId());
                Intent intent = new Intent(getBaseContext(), VisualizarAvaliacoesProfessor.class);
                intent.putExtra("professorAvaliacoes", prof);
                startActivity(intent);
            }
        });


        nCancelarMatricula = findViewById(R.id.cancelarMatriculaPerfilProfessor);
        nCancelarMatricula.setVisibility(View.GONE);
        nMatricular = findViewById(R.id.matricularPerfilProfessor);

        nMatricular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idNotificacaoo = randomUUID().toString();
                idProfessorr = professor.getId();
                idAlunoo = firebaseUser.getUid();
                criarNotificacao(idNotificacaoo, idProfessorr, idAlunoo);
            }
        });

        //ERRO AQUI
        verificarNotificacoes();
        //verificarMatriculas();

    }


    public void verificarMatriculas(){
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        DatabaseReference verificarMatricula = databaseReference.child("Professor").child(professor.getId()).child("Matricula");
        verificarMatricula.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    final String idMatricula = dataSnapshot.child("idMatricula").getValue().toString();
                    String idAlunooo = dataSnapshot.child("idAluno").getValue().toString();
                    if(idAlunooo.equals(firebaseUser.getUid())){
                        nMatricular.setFocusable(false);
                        nMatricular.setText("Você já está matriculado.");

                        nCancelarMatricula.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removerMatricula(idMatricula);
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    public void verificarNotificacoes(){
        firebaseDatabase = Conexao.getFirebaseDatabase();
        DatabaseReference verificarNotificacao = firebaseDatabase.getReference();

        verificarNotificacao.child("Notificacao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificacoes.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    final String idNotificacao, idProfessor, idAluno, assunto;

                    idNotificacao = dataSnapshot.child("idNotificacao").getValue().toString();
                    idProfessor = dataSnapshot.child("idProfessor").getValue().toString();
                    idAluno = dataSnapshot.child("idAluno").getValue().toString();
                    assunto = dataSnapshot.child("assunto").getValue().toString();

                    if (idProfessor.equals(professor.getId())) {
                        if (idAluno.equals(firebaseUser.getUid())) {
                            if (assunto.equals("solicitacao_matricula")) {
                                nCancelarMatricula.setVisibility(View.GONE);
                                nMatricular.setVisibility(View.VISIBLE);
                                nMatricular.setText("Solicitação pendente");
                                nMatricular.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        databaseReference.child("Notificacao").child(idNotificacao).removeValue();
                                        Toast.makeText(PerfilProfessor.this, "Solicitação removida!", Toast.LENGTH_SHORT).show();
                                        nMatricular.setText("Solicitar Matrícula");
                                        nMatricular.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                idNotificacaoo = randomUUID().toString();
                                                idProfessorr = professor.getId();
                                                idAlunoo = firebaseUser.getUid();
                                                criarNotificacao(idNotificacaoo, idProfessorr, idAlunoo);
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }

                    if(idNotificacao==null){
                        notificacoes.add(new Notificacao(idNotificacao));
                    }
                }

                if(notificacoes==null){
                    nMatricular.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(PerfilProfessor.this, "A notificação foi enviada com sucesso!", Toast.LENGTH_SHORT).show();

                            idNotificacaoo = randomUUID().toString();
                            idProfessorr = professor.getId();
                            idAlunoo = firebaseUser.getUid();
                            criarNotificacao(idNotificacaoo, idProfessorr, idAlunoo);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }




    public void criarNotificacao(String idNotificacao, String idProfessor, String idAluno){
        notificacao = new Notificacao(idNotificacao, idProfessor, idAluno, "aluno", "professor", "solicitacao_matricula");
        if(notificacao != null){
            Toast.makeText(PerfilProfessor.this, "A notificação foi enviada com sucesso!", Toast.LENGTH_SHORT).show();
            databaseReference.child("Notificacao").child(notificacao.getIdNotificacao()).setValue(notificacao);
        }
    }

    public void removerMatricula(String idMatricula){
        databaseReference.child("Professor").child(professor.getId()).child("Matricula").child(idMatricula).removeValue();
        databaseReference.child("Aluno").child(firebaseUser.getUid()).child("Matricula").child(idMatricula).removeValue();
        notificacao = new Notificacao(randomUUID().toString(), professor.getId(), firebaseUser.getUid(), "aluno", "professor", "remocao_matricula");
        databaseReference.child("Notificacao").child(notificacao.getIdNotificacao()).setValue(notificacao);
        Toast.makeText(this, "Matricula removida!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), BuscarProfessor.class));
    }

    public void setarAtributos(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            professor = (Professor) bundle.getSerializable("professorSelecionado");
        }
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        pesquisa = databaseReference.child("Professor").child(professor.getId());
        pesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nome = snapshot.child("nome").getValue().toString();
                professor.setNome(nome);
                nNome.setText(nome);

                String email = snapshot.child("email").getValue().toString();
                professor.setEmail(email);

                String endereco = snapshot.child("endereco").getValue().toString();
                professor.setEndereco(endereco);
                nEndereco.setText(endereco);

                String instrumentos = snapshot.child("instrumentos").getValue().toString();
                professor.setInstrumentos(instrumentos);
                nInstrumentos.setText(instrumentos);


                String biografia = snapshot.child("biografia").getValue().toString();
                professor.setBiografia(biografia);
                nBiografia.setText(biografia);

                String valor = snapshot.child("valor").getValue().toString();
                professor.setValor(valor);
                nValor.setText(valor);


                String disponibilidade = snapshot.child("disponibilidade").getValue().toString();
                professor.setDisponibilidade(disponibilidade);
                nDisponibilidade.setText(disponibilidade);

                String caminhoFoto = "";
                caminhoFoto =  snapshot.child("caminhoFoto").getValue().toString();
                professor.setCaminhoFoto(caminhoFoto);
                if (professor.getCaminhoFoto() != null) {
                    Uri uri = Uri.parse(professor.getCaminhoFoto());
                    Glide.with(PerfilProfessor.this).load(uri).into(nFoto);
                }else{
                    nFoto.setImageResource(R.drawable.perfil);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}