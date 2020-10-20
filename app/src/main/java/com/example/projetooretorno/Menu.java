package com.example.projetooretorno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projetooretorno.controle.EditarPerfilAluno;
import com.example.projetooretorno.helper.Conexao;

public class Menu extends AppCompatActivity {

    Button nSair, nEditarPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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