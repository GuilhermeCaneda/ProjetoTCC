package com.example.projetooretorno.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projetooretorno.R;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Notificacao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class NotificacaoProfessorAdapter extends RecyclerView.Adapter<NotificacaoProfessorAdapter.MyViewHolder> {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference pesquisa;
    private List<Notificacao> notificacoes;
    private Context context;
    String nome = "";
    String caminhoFoto = "";

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notificacao notificacao = new Notificacao();
        holder.nTexto.setText("teste");
        holder.nFoto.setImageResource(R.drawable.perfil);
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
