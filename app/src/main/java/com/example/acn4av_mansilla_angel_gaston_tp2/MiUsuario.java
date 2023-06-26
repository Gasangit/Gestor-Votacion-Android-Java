package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MiUsuario extends AppCompatActivity {
    private final String presentClass = this.getClass().getSimpleName().toString();

    TextView inputFirstName, inputLastName, inputDni, inputEmail, inputStreet, inputStreetNumber;
    TextView inputPassword, inputRepitePassword;
    Button buttonModify, buttonDelete, buttonMap, buttonReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_usuario);

        inputFirstName = findViewById(R.id.myUserInputName);
        inputLastName = findViewById(R.id.myUserInputLastName);
        inputDni = findViewById(R.id.myUserInputDni);
        inputEmail = findViewById(R.id.myUserInputEmail);
        inputStreet = findViewById(R.id.myUserInputStreet);
        inputStreetNumber = findViewById(R.id.myUserInputStreetNumber);
        inputPassword = findViewById(R.id.myUserInputPassword);
        inputRepitePassword = findViewById(R.id.myUserInputRepitePassword);

        buttonReturn = findViewById(R.id.myUserButtonReturn);
        buttonModify = findViewById(R.id.myUserButtonModify);
        buttonDelete = findViewById(R.id.myUserButtonDelete);
        buttonMap = findViewById(R.id.myUserButtonMap);

        Log.d(presentClass, "Se obtiene BUNDLE de INTENT anterior");
        Bundle idUserReception = getIntent().getExtras();
        String idUser = idUserReception.getString("idUser");

        Intent goToThis = new Intent(MiUsuario.this, MiUsuario.class);

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
                Log.d(presentClass,   selectResult.getColumnCount() + " columnas obtenidas");
                Log.d(presentClass, "Se crea : UserObject");
                Log.d(presentClass, "Es admin : " + selectResult.getInt(8));

            boolean isAdmin = false;
            if(selectResult.getInt(8) == 1) isAdmin = true;

            UsuarioObjeto UserObject = new UsuarioObjeto(
                    selectResult.getString(0), selectResult.getString(1),
                    selectResult.getString(2), selectResult.getString(3),
                    selectResult.getString(4), selectResult.getString(5),
                    selectResult.getString(6), selectResult.getString(7),
                    isAdmin
            );

            Log.d(presentClass, UserObject.getNombre() + " " +UserObject.getApellido() + " " + UserObject.getDni());

            inputFirstName.setText(UserObject.getNombre());
            inputLastName.setText(UserObject.getApellido());
            inputDni.setText(UserObject.getDni());
            inputEmail.setText(UserObject.getMail());
            inputStreet.setText(UserObject.getCalle());
            inputStreetNumber.setText(UserObject.getAltura());
            inputPassword.setText(UserObject.getPass());
            inputRepitePassword.setText(UserObject.getPass());

            buttonMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String address = inputStreet.getText().toString() + " " + inputStreetNumber.getText().toString();
                    Uri map = Uri.parse("geo:0,0?q=" + Uri.encode(address));

                    Intent goToMap = new Intent(Intent.ACTION_VIEW, map);
                    startActivity(goToMap);
                }
            });

            buttonReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent goToStart = new Intent(MiUsuario.this, Inicio.class);
                    goToStart.putExtras(idUserReception);
                    startActivity(goToStart);
                }
            });

            buttonModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(inputPassword.getText().toString().equals(inputRepitePassword.getText().toString())) {

                        boolean emptyInput = false;

                        String arrayUpdate [][] = {
                                {"usuario_nombre", inputFirstName.getText().toString()},
                                {"usuario_apellido", inputLastName.getText().toString()},
                                {"usuario_dni", inputDni.getText().toString()},
                                {"usuario_mail", inputEmail.getText().toString()},
                                {"usuario_password", inputPassword.getText().toString()},
                                {"usuario_calle", inputStreet.getText().toString()},
                                {"usuario_altura", inputStreetNumber.getText().toString()}
                        };

                        ContentValues updateUser = new ContentValues();

                        for(int i = 0; i < arrayUpdate.length; i++) {

                            Log.d(presentClass, "Se suma dato a ContentValues " + "Columna : " + arrayUpdate[i][0] + " dato : " + arrayUpdate[i][1]);
                            updateUser.put(arrayUpdate[i][0], arrayUpdate[i][1]);
                            Log.d(presentClass, "ContentValues vacio ? : " + updateUser.isEmpty());

                            if(arrayUpdate[i][1].equals("")) emptyInput = true;
                            if(emptyInput) {
                                Log.d(presentClass,"Hay un campo vacio");
                                updateUser.clear();
                                break;
                            }
                        }

                        if(emptyInput) {
                            Log.d(presentClass, "Existe un campo vacio se muestra TOAST");
                            Toast.makeText(MiUsuario.this,
                                    "Debe completar todos los campos",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            SQLiteDatabase databaseWrite = Helper.getWritableDatabase();
                            int updateResult = databaseWrite.update("usuario", updateUser, "id_usuario = '" + idUser + "'",null);
                            databaseWrite.close();

                            if(updateResult == 1) {
                                Log.d(presentClass, "UPDATE usuario id : " + idUser);
                                Toast.makeText(MiUsuario.this, "Se actualizaron los datos", Toast.LENGTH_SHORT).show();
                                goToThis.putExtras(idUserReception);
                                startActivity(goToThis);
                            }
                        }
                    }else {

                        Toast.makeText(MiUsuario.this, "Password y repeticion no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        Log.d(presentClass, "Se CIERRA BASE DE DATOS");
        databaseRead.close();
    }
}