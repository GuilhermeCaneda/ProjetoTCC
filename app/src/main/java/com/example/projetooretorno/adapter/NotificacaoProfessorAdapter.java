package com.example.projetooretorno.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projetooretorno.R;
import com.example.projetooretorno.controle.CadastroAluno;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Matricula;
import com.example.projetooretorno.modelo.Notificacao;
import com.example.projetooretorno.telastestes.NotificacaoProfessor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static java.util.UUID.randomUUID;

public class NotificacaoProfessorAdapter extends RecyclerView.Adapter<NotificacaoProfessorAdapter.MyViewHolder> {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference pesquisa;
    private List<Notificacao> notificacoes;
    private Context context;
    String nome = "";
    String caminhoFoto = "";
    Notificacao notificacao, notificacao_2;

    public NotificacaoProfessorAdapter(List<Notificacao> notificacoes, Context context) {
        this.notificacoes = notificacoes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem_notificacaoprofessor, parent, false);
        return new NotificacaoProfessorAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        notificacao = notificacoes.get(position);
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        if(notificacao.getDestinatario().equals("professor")){


            pesquisa = databaseReference.child("Aluno").child(notificacao.getIdAluno());
            pesquisa.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("nome").getValue()!=null){ nome = snapshot.child("nome").getValue().toString(); }
                    if(snapshot.child("caminhoFoto").getValue()!=null){ caminhoFoto = snapshot.child("caminhoFoto").getValue().toString(); }



                    if (caminhoFoto!="") {
                        Uri uri = Uri.parse(caminhoFoto);
                        Glide.with(context).load(uri).into(holder.nFoto);
                    }else{
                        holder.nFoto.setImageResource(R.drawable.perfil);
                    }


                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


            switch(notificacao.getAssunto()){
                case "solicitacao_matricula": holder.nTexto.setText(nome + " gostaria de ser seu aluno(a).");
                    break;
                case "remocao_matricula": holder.nTexto.setText(nome + " cancelou a matrícula.");
                    break;
                default:
                    break;
            }


            switch (notificacao.getAssunto()){
                case "solicitacao_matricula":
                holder.nAceitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Toast.makeText(context, "Matrícula aceita!", Toast.LENGTH_SHORT).show();
                        final Matricula matricula = new Matricula(randomUUID().toString(), notificacao.getIdProfessor(), notificacao.getIdAluno());
                        databaseReference.child("Professor").child(notificacao.getIdProfessor()).child("Matricula").child(matricula.getIdMatricula()).setValue(matricula).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            databaseReference.child("Aluno").child(notificacao.getIdAluno()).child("Matricula").child(matricula.getIdMatricula()).setValue(matricula).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    notificacao_2 = new Notificacao(randomUUID().toString(), notificacao.getIdProfessor(), notificacao.getIdAluno(), "professor", "aluno", "aprovacao_matricula");
                                    databaseReference.child("Notificacao").child(notificacao.getIdNotificacao()).removeValue();
                                    databaseReference.child("Notificacao").child(notificacao_2.getIdNotificacao()).setValue(notificacao_2);
                                }});
                        }
                    });
                }
                });
                break;
                default: holder.nAceitar.setVisibility(View.GONE);
                break;
            }


            holder.nRecusar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificacao = notificacoes.get(position);
                    switch (notificacao.getAssunto()){
                        case "solicitacao_matricula":
                            notificacao_2 = new Notificacao(randomUUID().toString(), notificacao.getIdProfessor(), notificacao.getIdAluno(), "professor", "aluno", "negacao_matricula");
                            databaseReference.child("Notificacao").child(notificacao_2.getIdNotificacao()).setValue(notificacao_2);
                            break;
                        default:
                            break;
                    }
                    Toast.makeText(context, "Notificação removida!", Toast.LENGTH_SHORT).show();
                    databaseReference.child("Notificacao").child(notificacao.getIdNotificacao()).removeValue();
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return notificacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView nFoto;
        private TextView nTexto;
        private Button nAceitar, nRecusar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nFoto = itemView.findViewById(R.id.fotoNotificacaoProfessor);
            nTexto = itemView.findViewById(R.id.textoNotificacaoProfessor);
            nAceitar = itemView.findViewById(R.id.aceitarNotificacaoProfessor);
            nRecusar = itemView.findViewById(R.id.recusarNotificacaoProfessor);
        }
    }

}
