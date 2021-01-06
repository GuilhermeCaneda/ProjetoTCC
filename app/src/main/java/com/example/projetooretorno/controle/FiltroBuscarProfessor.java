package com.example.projetooretorno.controle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projetooretorno.R;
import com.example.projetooretorno.modelo.Filtro;

import java.text.Normalizer;
import java.util.regex.Pattern;


public class FiltroBuscarProfessor extends AppCompatActivity {

    EditText nInstrumento, nCidade, nEstado;
    Button nBuscarProfessores;

    Filtro filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_buscar_professor);

        mapearComponentes();

        nBuscarProfessores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instrumento = nInstrumento.getText().toString().trim();
                String cidade = nCidade.getText().toString().trim();
                String estado = nEstado.getText().toString().trim();
                if(!instrumento.isEmpty()){
                    if(!cidade.isEmpty()){
                        if(!estado.isEmpty()){
                            instrumento = deAccent(instrumento).toUpperCase();
                            cidade = deAccent(cidade).toUpperCase();
                            estado = deAccent(estado).toUpperCase();
                            Toast.makeText(FiltroBuscarProfessor.this, instrumento + " " + cidade + " " + estado + " ", Toast.LENGTH_SHORT).show();
                            filtro = new Filtro(instrumento, cidade, estado);
                            Intent i = new Intent(getBaseContext(), BuscarProfessor.class);
                            i.putExtra("filtro", filtro);
                            startActivity(i);
                        }else{
                            Toast.makeText(FiltroBuscarProfessor.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(FiltroBuscarProfessor.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FiltroBuscarProfessor.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }


    public void mapearComponentes(){
        nInstrumento = findViewById(R.id.instrumentoFiltroBuscarProfessor);
        nCidade = findViewById(R.id.cidadeFiltroBuscarProfessor);
        nEstado = findViewById(R.id.estadoFiltroBuscarProfessor);
        nBuscarProfessores = findViewById(R.id.buscarProfessoresFiltroBuscarProfessor);
    }
}