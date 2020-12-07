package com.example.projetooretorno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projetooretorno.controle.TelaProvisoria;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.telastestes.MeusAlunos;
import com.example.projetooretorno.telastestes.NotificacaoProfessor;

public class MenuProfessor extends AppCompatActivity {

    Button nSair, nNotificacao, nEditarPerfil, nMeusAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_professor);

        nMeusAlunos = findViewById(R.id.meusAlunosMenuProfessor);
        nMeusAlunos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MeusAlunos.class));
            }
        });

        nEditarPerfil = findViewById(R.id.editarPerfilMenuProfessor);
        nEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TelaProvisoria.class));
            }
        });

        nNotificacao = findViewById(R.id.notificacoesMenuProfessor);
        nNotificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotificacaoProfessor.class));
            }
        });

        nSair = findViewById(R.id.sairMenuProfessor);
        nSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexao.logOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}