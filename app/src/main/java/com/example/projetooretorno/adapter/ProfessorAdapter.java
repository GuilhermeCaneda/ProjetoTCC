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
import com.example.projetooretorno.modelo.Professor;

import java.util.List;

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.MyViewHolder> {

    private List<Professor> professores;
    private Context context;

    public ProfessorAdapter(List<Professor> listaProfessores, Context context) {
        this.professores = listaProfessores;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem_professor, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Professor professor = professores.get(position);
        holder.nNome.setText(professor.getNome());
        holder.nEndereco.setText(professor.getEndereco());
        holder.nEmail.setText(professor.getEmail());
        if (professor.getCaminhoFoto() != "") {
            Uri uri = Uri.parse(professor.getCaminhoFoto());
            Glide.with(context).load(uri).into(holder.nFoto);
        }else{
            holder.nFoto.setImageResource(R.drawable.perfil);
        }
    }

    @Override
    public int getItemCount() {
        return professores.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nNome, nEmail, nEndereco;
        private ImageView nFoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nNome = itemView.findViewById(R.id.nomeListagemProfessor);
            nEmail = itemView.findViewById(R.id.emailListagemProfessor);
            nEndereco = itemView.findViewById(R.id.enderecoListagemProfessor);
            nFoto = itemView.findViewById(R.id.fotoListagemProfessor);
        }
    }


}
