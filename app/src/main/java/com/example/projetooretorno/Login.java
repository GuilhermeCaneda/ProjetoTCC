package com.example.projetooretorno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetooretorno.controle.OpCadastros;
import com.example.projetooretorno.helper.Conexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText nEmail, nSenha;
    Button nEntrar;
    TextView nCadastrar;
    ProgressBar nProgressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nEmail = findViewById(R.id.emailLogin);
        nSenha = findViewById(R.id.senhaLogin);
        nProgressBar = findViewById(R.id.progressBarLogin);
        nProgressBar.setVisibility(View.GONE);
        nEmail.requestFocus();

        nEntrar = findViewById(R.id.entrarLogin);
        nEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = nEmail.getText().toString().trim();
                String senha = nSenha.getText().toString().trim();
                if(!email.isEmpty()){
                    if(!senha.isEmpty()){
                        nProgressBar.setVisibility(View.VISIBLE);
                        login(email, senha);
                    }else{
                        Toast.makeText(getBaseContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nCadastrar = findViewById(R.id.cadastrarLogin);
        nCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OpCadastros.class));
            }
        });
    }

    private void login(String email, String senha){
        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), Menu.class));
                }
                else{
                    Toast.makeText(Login.this, "Usuário ou senha incorretos.", Toast.LENGTH_SHORT).show();
                }
                nProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth = Conexao.getFirebaseAuth();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Menu.class));
            finish();
        }
    }
}