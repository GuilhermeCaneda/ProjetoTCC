package com.example.projetooretorno.controle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.projetooretorno.R;

public class OpCadastros extends AppCompatActivity {

    ImageView aluno, professor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op_cadastros);

        aluno = findViewById(R.id.opCadastroAluno);
        aluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastroAluno.class));
            }
        });
        professor = findViewById(R.id.opCadastroProfessor);
        professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastroProfessor.class));
            }
        });

    }
}