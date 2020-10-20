package com.example.projetooretorno.helper;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;


public class Conexao {
    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser firebaseUser;

    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;
    private static FirebaseStorage firebaseStorage;

    private Conexao(){
    }

    public static FirebaseAuth getFirebaseAuth(){
        if(firebaseAuth==null){
            inicializarFirebase();
        }
        return firebaseAuth;
    }

    public static FirebaseDatabase getFirebaseDatabase(){
        if(firebaseDatabase==null){
            inicializarFirebase();
        }
        return firebaseDatabase;
    }

    public static FirebaseStorage getFirebaseStorage(){
        if(firebaseStorage==null){
            inicializarFirebase();
        }
        return firebaseStorage;
    }

    private static void inicializarFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    firebaseUser = user;
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public static FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }

    public static void logOut(){
        firebaseAuth.signOut();
    }
}
