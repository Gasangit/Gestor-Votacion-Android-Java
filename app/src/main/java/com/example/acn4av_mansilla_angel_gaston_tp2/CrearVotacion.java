package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrearVotacion extends AppCompatActivity {

    private final String presentClass = this.getClass().getSimpleName();
    private RecyclerView RecyclerViewCreateVotes;
    private TextView DefaultTextView;
    private HelperDatabase SqliteHelper;
    private Button buttonToGenerate, buttonReturn;
    ExecutorService Ejecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_votacion);

        Ejecutor = Executors.newSingleThreadExecutor();

        RecyclerViewCreateVotes = findViewById(R.id.recyclerViewCreateVotes);
        DefaultTextView = findViewById(R.id.defaultTextView);

        buttonReturn = findViewById(R.id.createVoteReturn);
        buttonToGenerate = findViewById(R.id.createVoteCreate);

        Bundle bundleUser = getIntent().getExtras();
        String idUser = bundleUser.getString("idUser");

        SqliteHelper = new HelperDatabase(this, "dbAppVotes",null, 1);

        SQLiteDatabase database = SqliteHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM votacion WHERE votacion_id_usuario_organizador = '" + idUser + "'", null);

        VotacionObjeto [] arrayVoting = new VotacionObjeto[cursor.getCount()];

        while(cursor.moveToNext()) {

            int index = cursor.getPosition();

            VotacionObjeto voting = new VotacionObjeto(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2));

            Log.d("CLASE : " + this.getClass().getSimpleName(),">>>Indice CURSOR Select votacion : " + index);
            Log.d("CLASE : " + this.getClass().getSimpleName(),">>>Texto CURSOR Select votacion : " + cursor.getString(2));

            arrayVoting[index] = voting;
        }

        if(arrayVoting.length < 1) {

            Toast.makeText(this, "Todavía no ha creado votaciones", Toast.LENGTH_SHORT).show();
        }

        Ejecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    adaptadorDeVistas adapter = new adaptadorDeVistas(arrayVoting, idUser);
                    RecyclerViewCreateVotes.setAdapter(adapter);

                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        });

        database.close();

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToInicio = new Intent(CrearVotacion.this, Inicio.class);
                goToInicio.putExtras(bundleUser);
                startActivity(goToInicio);
            }
        });

        buttonToGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleUser = new Bundle();
                bundleUser.putString("idUser", idUser);
                Intent goToVoting = new Intent(CrearVotacion.this, Votacion.class);
                goToVoting.putExtras(bundleUser);
                startActivity(goToVoting);
            }
        });
    }
}