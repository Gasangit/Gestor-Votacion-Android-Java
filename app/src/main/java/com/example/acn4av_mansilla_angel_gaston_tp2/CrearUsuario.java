package com.example.acn4av_mansilla_angel_gaston_tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class CrearUsuario extends AppCompatActivity {

    private final String presentClass = this.getClass().getSimpleName();
    private TextView inputFirstName, inputLastName, inputDni, inputEmail, inputPassword,
            inputPasswordRepite, inputStreet, inputNumber;
    CheckBox checkIsAdmin;
    private Button buttonRegister, buttonReturn;
    private HelperDatabase SqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);


        inputFirstName = findViewById(R.id.createUserInputFirstName);
        inputLastName = findViewById(R.id.createUserInputLastName);
        inputDni = findViewById(R.id.createUserInputDni);
        inputEmail = findViewById(R.id.createUserInputMail);
        inputPassword = findViewById(R.id.createUserInputPassword);
        inputPasswordRepite = findViewById(R.id.createUserInputRepitePassword);
        inputStreet = findViewById(R.id.createUserInputStreet);
        inputNumber = findViewById(R.id.createUserInputStreetNumber);

        checkIsAdmin = findViewById(R.id.createUserCheckBoxAdmin);

        buttonReturn = findViewById(R.id.createUserButtonReturn);
        buttonRegister = findViewById(R.id.createUserButtonCreate);

        Bundle idUserReception = getIntent().getExtras();
        String idUser = idUserReception.getString("idUser");

        Log.d(this.getClass().getSimpleName(), "Se instanciaron los elementos de la VISTA (inputs y button)");

        SqliteHelper = new HelperDatabase(CrearUsuario.this, "dbAppVotes",null,1);
        Log.d(this.getClass().getSimpleName(), "Se instanció el HELPER");

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gotoUsersManager = new Intent(CrearUsuario.this, UsersManager.class);
                gotoUsersManager.putExtras(idUserReception);
                startActivity(gotoUsersManager);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(presentClass, "Se accionó botón de CREAR");
                SQLiteDatabase databaseRead = SqliteHelper.getReadableDatabase();

                Cursor mailVerification = databaseRead.rawQuery(
                        "SELECT usuario_mail" +
                                " FROM usuario " +
                                "WHERE usuario_mail = '" +
                                inputEmail.getText().toString() + "'", null);

                if(mailVerification.moveToFirst()) {
                    Toast.makeText(
                            CrearUsuario.this,
                            "El MAIL que acaba de ingresar se encuentra" +
                                    " en uso NO PUEDE ASIGNARSE A OTRO USUARIO", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase database = SqliteHelper.getWritableDatabase();
                    Log.d(presentClass, "Se instanció la base de datos");

                    if(inputPassword.getText().toString().equals(inputPasswordRepite.getText().toString())) {
                        Log.d(presentClass, "Password y repetición coinciden");

                        boolean emptyInput = false;

                        boolean isAdmin = false;
                        if(checkIsAdmin.isChecked()) isAdmin = true;

                        String arrayInsert [][] = {
                                {"usuario_nombre", inputFirstName.getText().toString()},
                                {"usuario_apellido", inputLastName.getText().toString()},
                                {"usuario_dni", inputDni.getText().toString()},
                                {"usuario_mail", inputEmail.getText().toString()},
                                {"usuario_password", inputPassword.getText().toString()},
                                {"usuario_calle", inputStreet.getText().toString()},
                                {"usuario_altura", inputNumber.getText().toString()}
                        };

                        ContentValues ColumnsValues = new ContentValues();
                        Log.d(presentClass, "Se instanció el contendor de valores a ingresar en la base de datos");

                        for(int i = 0; i < arrayInsert.length; i++) {

                            Log.d(presentClass, "Se suma dato a ContentValues " + "Columna : " + arrayInsert[i][0] + " dato : " + arrayInsert[i][1]);
                            ColumnsValues.put(arrayInsert[i][0], arrayInsert[i][1]);
                            Log.d(presentClass, "ContentValues vacio ? : " + ColumnsValues.isEmpty());

                            if(arrayInsert[i][1].equals("")) emptyInput = true;
                            if(emptyInput) {
                                Log.d(presentClass,"Hay un campo vacio");
                                ColumnsValues.clear();
                                break;
                            }
                        }

                        ColumnsValues.put("usuario_admin", isAdmin);

                        if(emptyInput) {
                            Log.d(presentClass, "Existe un campo vacio se muestra TOAST");
                            Toast.makeText(CrearUsuario.this,
                                    "Debe completar todos los campos",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            database.insert("usuario", null, ColumnsValues);
                            Log.d(presentClass, "Se realizó insert a tabla usuario");

                            database.close();
                            Log.d(presentClass, "Base de datos cerrada");

                            inputFirstName.setText("");
                            inputLastName.setText("");
                            inputDni.setText("");
                            inputEmail.setText("");
                            inputPassword.setText("");
                            inputPasswordRepite.setText("");
                            inputStreet.setText("");
                            inputNumber.setText("");
                            checkIsAdmin.setChecked(false);

                            Intent goToUsersManager = new Intent(CrearUsuario.this, UsersManager.class);
                            startActivity(goToUsersManager);
                        }

                    }else {

                        Log.d(presentClass, "No coinciden password y repetición");
                        Toast.makeText(CrearUsuario.this, "Password y repetición no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}