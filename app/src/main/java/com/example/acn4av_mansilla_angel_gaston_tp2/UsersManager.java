package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsersManager extends AppCompatActivity {

    private RecyclerView RecyclerViewUsers;
    private final String presentClass = this.getClass().getSimpleName().toString();
    Button buttonReturn, buttonCreate;

    ExecutorService Ejecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_manager);

        Ejecutor = Executors.newSingleThreadExecutor();

        buttonReturn = findViewById(R.id.usersManagerButtonReturn);
        buttonCreate = findViewById(R.id.usersManagerButtonCreate);

        RecyclerViewUsers = findViewById(R.id.recyclerViewUsersManager);

        Bundle idUserReception = getIntent().getExtras();
        String idUser = idUserReception.getString("idUser");

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoStart = new Intent(UsersManager.this, Inicio.class);
                gotoStart.putExtras(idUserReception);
                startActivity(gotoStart);
            }
        });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToCreateUser = new Intent(UsersManager.this, CrearUsuario.class);
                goToCreateUser.putExtras(idUserReception);
                startActivity(goToCreateUser);
            }
        });

        HelperDatabase Helper = new HelperDatabase(this,"dbAppVotes", null, 1);
        SQLiteDatabase Database = Helper.getReadableDatabase();

        Cursor resultOfSelectUsuario = Database.rawQuery("SELECT * FROM usuario;",null);

        UsuarioObjeto [] arrayUsers = new UsuarioObjeto[resultOfSelectUsuario.getCount()];
        Log.d(presentClass,"Usuarios recuperados con SELECT : " + resultOfSelectUsuario.getCount());

        while(resultOfSelectUsuario.moveToNext()) {

            int index = resultOfSelectUsuario.getPosition();
            Log.d(presentClass,"Indice de ITEM USUARIO : " + index);

            boolean isAdmin = false;
            if(resultOfSelectUsuario.getInt(8) == 1) isAdmin = true;

            UsuarioObjeto User = new UsuarioObjeto(
                    resultOfSelectUsuario.getString(0),
                    resultOfSelectUsuario.getString(1),
                    resultOfSelectUsuario.getString(2),
                    resultOfSelectUsuario.getString(3),
                    resultOfSelectUsuario.getString(4),
                    resultOfSelectUsuario.getString(5),
                    resultOfSelectUsuario.getString(6),
                    resultOfSelectUsuario.getString(7),
                    isAdmin
            );

            Log.d(presentClass, "Usuario recuperado de BBDD : " + User.getNombre() + " " + User.getApellido());
            arrayUsers[index] = User;
        }

        Ejecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(presentClass, "Ingreso a hilo secundario para generar RecyclerView");
                    adaptadorDeVistas adapter = new adaptadorDeVistas(arrayUsers, idUser );
                    RecyclerViewUsers.setAdapter(adapter);

                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        });
    }
}