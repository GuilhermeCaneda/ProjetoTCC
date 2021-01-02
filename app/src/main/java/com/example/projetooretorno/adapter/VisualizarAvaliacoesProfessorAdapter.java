package com.example.projetooretorno.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projetooretorno.R;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.modelo.Avaliacao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class VisualizarAvaliacoesProfessorAdapter extends RecyclerView.Adapter<VisualizarAvaliacoesProfessorAdapter.MyViewHolder> {

    private List<Avaliacao> avaliacoes;
    private Context context;
    private Avaliacao avaliacao;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference pesquisa, pesquisa2;

    private String texto = "", caminhoFoto = "";

    public VisualizarAvaliacoesProfessorAdapter(List<Avaliacao> avaliacoes, Context context) {
        this.avaliacoes = avaliacoes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem_avaliacoes_professor, parent, false);
        return new VisualizarAvaliacoesProfessorAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        avaliacao = avaliacoes.get(position);
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();

        pesquisa = databaseReference.child("Professor").child(avaliacao.getIdProfessor()).child("Avaliacao").child(avaliacao.getIdAvaliacao());
        pesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("texto").getValue()!=null){
                    texto = snapshot.child("texto").getValue().toString();
                }
                holder.nTexto.setText(texto);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pesquisa2 = databaseReference.child("Aluno").child(avaliacao.getIdAluno());
        pesquisa2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("caminhoFoto").getValue()!=null){
                    caminhoFoto = snapshot.child("caminhoFoto").getValue().toString();
                }

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

    }

    @Override
    public int getItemCount() {
        return avaliacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView nFoto;
        private TextView nTexto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nFoto = itemView.findViewById(R.id.fotoVisualizarAvaliacoesProfessorAdapter);
            nTexto = itemView.findViewById(R.id.textoVisualizarAvaliacoesProfessorAdapter);
        }
    }
}
