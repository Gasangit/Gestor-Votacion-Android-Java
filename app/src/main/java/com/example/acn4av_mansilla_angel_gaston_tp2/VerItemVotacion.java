package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class VerItemVotacion extends AppCompatActivity {

    private TextView inputDescription, inputOption1, inputOption2, inputOption3, textOwner;
    private Button buttonModify, buttonDelete, buttonReturn;
    private String idString, descriptionString, ownerIdString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_item_votacion);

        inputDescription = findViewById(R.id.votingViewInputDescription);
        inputOption1 = findViewById(R.id.votingViewInputOption1);
        inputOption2 = findViewById(R.id.votingViewInputOption2);
        inputOption3 = findViewById(R.id.votingViewInputOption3);
        textOwner = findViewById(R.id.votingViewTextOwner);

        buttonReturn = findViewById(R.id.votingViewButtonReturn);
        buttonModify = findViewById(R.id.votingViewButtonModify);
        buttonDelete = findViewById(R.id.votingViewButtonDelete);

        Bundle votingData = getIntent().getExtras();
        idString = votingData.getString("idVotacion");
        descriptionString = votingData.getString("descripcion");
        ownerIdString = votingData.getString("idOrganizador");

        Log.d(this.getClass().getSimpleName().toString(), "idVotacion : " + idString + " descripción : " +
                descriptionString + " organizador : " + ownerIdString);

        Intent goToCreateVoting = new Intent(VerItemVotacion.this, CrearVotacion.class);
        Bundle dataEntry = getIntent().getExtras();
        goToCreateVoting.putExtras(dataEntry);

        HelperDatabase Helper = new HelperDatabase(this, "dbAppVotes", null, 1);
        SQLiteDatabase databaseRead = Helper.getReadableDatabase();
        SQLiteDatabase databaseWrite = Helper.getWritableDatabase();

        Cursor rowOfUser = databaseRead.rawQuery("SELECT id_usuario, usuario_nombre, usuario_apellido FROM usuario WHERE id_usuario = '"
                                                + ownerIdString +"';", null );

        OpcionesObjeto [] options = new OpcionesObjeto[3];

        if(rowOfUser.moveToFirst()) {

            Cursor rowOfOptions = databaseRead.rawQuery("SELECT * FROM opcion AS op JOIN opcion_votacion AS opvo " +
                        "ON op.id_opcion = opvo.opcion_votacion_id_opcion WHERE opvo.opcion_votacion_id_votacion = " +
                        idString, null);

            while(rowOfOptions.moveToNext())
            {
                for(int i = 0; i < rowOfOptions.getColumnCount(); i++)
                {
                    Log.d ("    JOIN : Columna " + i + " "+ rowOfOptions.getColumnName(i)  ,
                                                            rowOfOptions.getString(i).toString());
                }

                OpcionesObjeto option  = new OpcionesObjeto(rowOfOptions.getString(0), rowOfOptions.getString(1));

                options[rowOfOptions.getPosition()] = option;
                Log.d(this.getClass().getSimpleName().toString(),"Opcion obtenida : " + rowOfOptions.getString(1));
            }

            String ownerFullName = "Organizador : \n" + rowOfUser.getString(1) + " " + rowOfUser.getString(2);

            for(int i = 0; i < options.length; i++) {

                if(options[i] == null) {
                    options[i] = new OpcionesObjeto("","");
                }

                if(options[i] != null) {
                    Log.d(this.getClass().getSimpleName().toString(), " OPCION"+ i +" : " + options[i].getDescription());
                }

            }

            inputDescription.setText(descriptionString);
            if(options[0] != null) inputOption1.setText(options[0].getDescription());
            if(options[1] != null) inputOption2.setText(options[1].getDescription());
            if(options[2] != null) inputOption3.setText(options[2].getDescription());
            textOwner.setText(ownerFullName);
        }else {
            String descriptionFail = "Falla en carga de descripción";
            String ownerFail = "Falla en carga de organizador";

            inputDescription.setText(descriptionFail);
            textOwner.setText(ownerFail);
        }

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(goToCreateVoting);
            }
        });

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase databaseWrite =  Helper.getWritableDatabase();
                SQLiteDatabase databaseRead = Helper.getReadableDatabase();

                TextView [] inputs = {inputOption1, inputOption2, inputOption3};

                ContentValues columnsVotacion = new ContentValues();
                ContentValues columnsOpcion = new ContentValues();
                ContentValues columnOpcionVotacion = new ContentValues();

                columnsVotacion.put("votacion_descripcion", inputDescription.getText().toString());
                int resultVoting = databaseWrite.update("votacion",  columnsVotacion,
                                    "id_votacion = '" + idString +"'", null);
                String okDescription = "";
                if(resultVoting ==  1 ) okDescription = " actualizada."; else okDescription = " no actualizada.";

                String [] okUpdates = new String[3];

                for(int i = 0; i <  options.length; i++) {

                    if(options[i].getId() != "") {
                        Log.d(this.getClass().getSimpleName().toString(),"Se ACTUALIZA opcion" + (i +1));
                        columnsOpcion.put("opcion_descripcion", inputs[i].getText().toString());
                        int resultOption = databaseWrite.update("opcion", columnsOpcion,
                                "id_opcion = '" + options[i].getId()+ "'", null);

                        if(resultOption == 1) okUpdates[i] = " actualizada.";
                        else okUpdates[i] = " no actualizada.";

                        columnsOpcion.clear();
                    }else {

                        //falta vincular la nueva opcion a la votacion en la tabla opcion_votacion

                        Log.d(this.getClass().getSimpleName().toString(), "Se carga NUEVA opcion " + (i +1));
                        columnsOpcion.put("opcion_descripcion", inputs[i].getText().toString());

                        databaseWrite.insert("opcion",null, columnsOpcion);

                        Cursor lastId = databaseRead.rawQuery("SELECT MAX(id_opcion) FROM opcion;", null);

                        if (lastId.moveToFirst()) {
                            columnOpcionVotacion.put("opcion_votacion_id_opcion", lastId.getString(0).toString());
                            columnOpcionVotacion.put("opcion_votacion_id_votacion", idString);

                            databaseWrite.insert("opcion_votacion", null , columnOpcionVotacion);

                            columnOpcionVotacion.clear();
                        }

                        columnsOpcion.clear();
                    }
                }

                databaseWrite.close();

                String message = "";

                for(int i = 0; i <  options.length; i++) {
                    if(options[i] != null) {

                        message += "\nOPCION " + (i + 1) +  okUpdates[1];
                    }
                }

                Toast.makeText(VerItemVotacion.this,
                        "DESCRIPCION" + okDescription +
                            "\n" + message, Toast.LENGTH_SHORT).show();

                startActivity(goToCreateVoting);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase databaseWrite = Helper.getWritableDatabase();

                int deleteVotingResult = databaseWrite.delete("votacion","id_votacion = '" + idString +"'",null);

                int deleteOptionVotingResult = databaseWrite.delete("opcion_votacion", "opcion_votacion_id_votacion = '" + idString + "'", null);

                //int deleteOptionVotingResult = devolverResultado();

                String [] arrayDeleteOptions = new String[5];

                if (deleteVotingResult == 1) arrayDeleteOptions[0] = "ok";
                else arrayDeleteOptions[0] = "no";

                if (deleteOptionVotingResult == 1) arrayDeleteOptions[1] = "ok";
                else arrayDeleteOptions[1] = "no";

                for(int i = 2; i < options.length; i++) {
                    if(options[i].getId() != "") {

                        int deleteOptionResult = databaseWrite.delete("opcion", "id_opcion = '" + options[i].getId() + "'", null);

                        if(deleteOptionResult == 1 || options[i] == null) arrayDeleteOptions[i] = "ok";
                        else arrayDeleteOptions[i] = "no";
                    }
                }

                Log.d(this.getClass().getSimpleName().toString(),
                        " Votacion borrada : " + arrayDeleteOptions[0] +
                             "   \nVinculos opciones votacion borrados : " + arrayDeleteOptions[1] +
                             "   \nOpcion 1 borrada : " + arrayDeleteOptions[2] +
                             "   \nOpcion 2 borrada : " + arrayDeleteOptions[3] +
                             "   \nOpcion 3 borrada : " + arrayDeleteOptions[4]);

                databaseWrite.close();

                startActivity(goToCreateVoting);
            }

            private int devolverResultado() {

                int deleteOptionVotingResult = databaseWrite.delete("opcion_votacion", "opcion_votacion_id_votacion = '" + idString + "'", null);
                return deleteOptionVotingResult;
            }
        });

        databaseWrite.close();
        databaseRead.close();
    }
}