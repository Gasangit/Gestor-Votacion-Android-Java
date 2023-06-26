package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Inicio extends AppCompatActivity {

    private Button buttonVote, buttonCreateVote, buttonMyUser, buttonUserAdmin, buttonExit;
    private String presentClass = this.getClass().getSimpleName().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        buttonExit = findViewById(R.id.startButtonExit);
        buttonUserAdmin = findViewById(R.id.startButtonUsersAdmin);
        buttonMyUser = findViewById(R.id.startButtonMyUser);
        buttonVote = findViewById(R.id.startVote);
        buttonCreateVote = findViewById(R.id.startCreateVote);

        Log.d(presentClass, "Traer contexto : " + buttonUserAdmin.getContext().getClass().getSimpleName().toString());

        Bundle idUserReception = getIntent().getExtras();
        String idUser = idUserReception.getString("idUser");

        Log.d(presentClass, "Se instancia HELPER");
        SQLiteOpenHelper Helper = new HelperDatabase(this,"dbAppVotes",null, 1);

        Log.d(presentClass,"Se INSTANCIA BASE DE DATOS");
        SQLiteDatabase databaseRead = Helper.getReadableDatabase();

        Log.d(presentClass, "Se hace SELECT a base de datos por USUARIO");
        Cursor selectResult = databaseRead.rawQuery(
                "SELECT * " +
                        "FROM usuario " +
                        "WHERE id_usuario = '" + idUser + "'", null);

        if(selectResult.moveToFirst()) {

            Log.d(presentClass, "Datos de usuario obtenidos");
            Log.d(presentClass, selectResult.getColumnCount() + " columnas obtenidas");
            Log.d(presentClass, "Se crea : UserObject");

            boolean isAdmin = false;
            if(selectResult.getInt(8) == 1) {
                isAdmin = true;
                buttonUserAdmin.setVisibility(View.VISIBLE);
            }

            Log.d(presentClass, "Administrador : " + isAdmin);

            UsuarioObjeto UserObject = new UsuarioObjeto(
                    selectResult.getString(0), selectResult.getString(1),
                    selectResult.getString(2), selectResult.getString(3),
                    selectResult.getString(4), selectResult.getString(5),
                    selectResult.getString(6), selectResult.getString(7),
                    isAdmin
            );
        }

        //Bundle bundleUser = new Bundle();
        //bundleUser.putString("idUser", idUser);

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToExit = new Intent(Inicio.this, MainActivity.class);
                startActivity(goToExit);
            }
        });

        buttonUserAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoUsersManager = new Intent(Inicio.this, UsersManager.class);
                gotoUsersManager.putExtras(idUserReception);
                startActivity(gotoUsersManager);
            }
        });

        buttonMyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToMyUser = new Intent(Inicio.this, MiUsuario.class);
                goToMyUser.putExtras(idUserReception);
                startActivity(goToMyUser);
            }
        });

        buttonVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToVote = new Intent(Inicio.this, MisVotaciones.class);
                goToVote.putExtras(idUserReception);
                startActivity(goToVote);
            }
        });

        buttonCreateVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToCreateVote = new Intent(Inicio.this, CrearVotacion.class);
                goToCreateVote.putExtras(idUserReception);
                startActivity(goToCreateVote);
            }
        });
    }
}