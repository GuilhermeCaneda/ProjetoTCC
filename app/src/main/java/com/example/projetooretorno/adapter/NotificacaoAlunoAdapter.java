package com.example.projetooretorno.adapter;

import android.content.Context;
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
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Matricula;
import com.example.projetooretorno.modelo.Notificacao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static java.util.UUID.randomUUID;

public class NotificacaoAlunoAdapter extends RecyclerView.Adapter<NotificacaoAlunoAdapter.MyViewHolder> {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference pesquisa;
    private List<Notificacao> notificacoes;
    private Context context;
    String nome = "";
    String caminhoFoto = "";
    Notificacao notificacao, notificacao_2;

    public NotificacaoAlunoAdapter(List<Notificacao> notificacoes, Context context) {
        this.notificacoes = notificacoes;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificacaoAlunoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem_notificacaoaluno, parent, false);
        return new NotificacaoAlunoAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificacaoAlunoAdapter.MyViewHolder holder, final int position) {
        notificacao = notificacoes.get(position);
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        if(notificacao.getDestinatario().equals("aluno")){


            pesquisa = databaseReference.child("Professor").child(notificacao.getIdProfessor());
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
                case "aprovacao_matricula" : holder.nTexto.setText(nome + " aprovou a sua solicitação de matrícula.");
                    break;
                case "negacao_matricula": holder.nTexto.setText(nome + " negou a sua solicitação de matrícula.");
                    break;
                case "edicao_matricula": holder.nTexto.setText(nome + " editou a sua matrícula.");
                    break;
                case "remocao_matricula": holder.nTexto.setText(nome + " cancelou a sua matrícula.");
                    break;
                default:
                    break;
            }



            holder.nRecusar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificacao = notificacoes.get(position);
                    databaseReference.child("Notificacao").child(notificacao.getIdNotificacao()).removeValue();
                    Toast.makeText(context, "Notificação removida!", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return notificacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView nFoto;
        private TextView nTexto;
        private Button nRecusar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nFoto = itemView.findViewById(R.id.fotoNotificacaoAluno);
            nTexto = itemView.findViewById(R.id.textoNotificacaoAluno);
            nRecusar = itemView.findViewById(R.id.recusarNotificacaoAluno);

        }
    }
}
