package com.example.projetooretorno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projetooretorno.controle.EditarPerfilAluno;
import com.example.projetooretorno.helper.Conexao;
import com.example.projetooretorno.telastestes.BuscarProfessor;
import com.example.projetooretorno.telastestes.PerfilProfessor;

public class Menu extends AppCompatActivity {

    Button nSair, nEditarPerfil, nTeste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        nTeste = findViewById(R.id.teste);
        nTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BuscarProfessor.class));
            }
        });

        nEditarPerfil = findViewById(R.id.buttonEditarPerfilAluno);
        nEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditarPerfilAluno.class));
            }
        });

        nSair = findViewById(R.id.sair);
        nSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexao.logOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}