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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static java.util.UUID.randomUUID;

public class MeusProfessoresAdapter extends RecyclerView.Adapter<MeusProfessoresAdapter.MyViewHolder> {

    private List<Matricula> matriculas;
    private Context context;
    private Matricula matricula;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference pesquisa;

    String nome = "", email = "", caminhoFoto = "", valorPagamento = "", dataPagamento = "";
    Notificacao notificacao;

    public MeusProfessoresAdapter(List<Matricula> matriculas, Context context) {
        this.matriculas = matriculas;
        this.context = context;
    }

    @NonNull
    @Override
    public MeusProfessoresAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem_meusprofessores, parent, false);
        return new MeusProfessoresAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MeusProfessoresAdapter.MyViewHolder holder, int position) {
        matricula = matriculas.get(position);
        firebaseDatabase = Conexao.getFirebaseDatabase();
        databaseReference = firebaseDatabase.getReference();
        pesquisa = databaseReference.child("Professor").child(matricula.getIdProfessor());
        pesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nome = snapshot.child("nome").getValue().toString();
                email = snapshot.child("email").getValue().toString();
                if(snapshot.child("caminhoFoto").getValue()!=null){
                    caminhoFoto = snapshot.child("caminhoFoto").getValue().toString();
                }

                pesquisa = databaseReference.child("Professor").child(matricula.getIdProfessor()).child("Matricula").child(matricula.getIdMatricula());
                pesquisa.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("valor").getValue()!=null) {
                            valorPagamento = snapshot.child("valor").getValue().toString();
                        }
                        if(snapshot.child("dataPagamento").getValue()!=null){
                            dataPagamento = snapshot.child("dataPagamento").getValue().toString();
                        }
                        holder.nValorPagamento.setText("Valor do pagamento: " + valorPagamento);
                        holder.nDataPagamento.setText("Data do último pagamento: " + dataPagamento);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.nNome.setText("Nome: " + nome);
                holder.nEmail.setText("Email: " + email);
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
        return matriculas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView nFoto;
        private TextView nNome, nEmail, nValorPagamento, nDataPagamento;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nFoto = itemView.findViewById(R.id.fotoMeusProfessores);
            nNome = itemView.findViewById(R.id.nomeMeusProfessores);
            nEmail = itemView.findViewById(R.id.emailMeusProfessores);
            nValorPagamento = itemView.findViewById(R.id.valorPagamentoMeusProfessores);
            nDataPagamento = itemView.findViewById(R.id.dataPagamentoMeusProfessores);
        }
    }
}
