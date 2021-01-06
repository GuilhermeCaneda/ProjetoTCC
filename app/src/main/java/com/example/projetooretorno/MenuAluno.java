package com.example.projetooretorno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projetooretorno.controle.EditarPerfilAluno;
import com.example.projetooretorno.controle.MeusProfessoresAluno;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.controle.NotificacaoAluno;
import com.example.projetooretorno.controle.FiltroBuscarProfessor;

public class MenuAluno extends AppCompatActivity {

    Button nSair, nEditarPerfil, nProcurarProfessor, nNotificacoes, nMeusProfessores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        nProcurarProfessor = findViewById(R.id.procurarProfessoresMenuAluno);
        nProcurarProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FiltroBuscarProfessor.class));
            }
        });

        nEditarPerfil = findViewById(R.id.editarPerfilMenuAluno);
        nEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditarPerfilAluno.class));
            }
        });

        nMeusProfessores = findViewById(R.id.meusProfessoresMenuAluno);
        nMeusProfessores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MeusProfessoresAluno.class));
            }
        });

        nNotificacoes = findViewById(R.id.notificacoesMenuAluno);
        nNotificacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotificacaoAluno.class));
            }
        });

        nSair = findViewById(R.id.sairMenuAluno);
        nSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexao.logOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}