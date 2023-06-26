package com.example.acn4av_mansilla_angel_gaston_tp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class HelperDatabase extends SQLiteOpenHelper {


    public HelperDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(this.getClass().getSimpleName().toString(),">>>Se crea base de datos<<<");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS usuario;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS voto;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS opcion;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS votacion;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS usuario_votacion;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS opcion_votacion;");

        sqLiteDatabase.execSQL(
            "CREATE TABLE 'usuario'" +
                "(" +
                    "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "usuario_nombre TEXT NOT NULL," +
                    "usuario_apellido TEXT NOT NULL," +
                    "usuario_dni TEXT NOT NULL," +
                    "usuario_mail TEXT NOT NULL," +
                    "usuario_password TEXT NOT NULL," +
                    "usuario_calle TEXT NOT NULL," +
                    "usuario_altura TEXT NOT NULL," +
                    "usuario_admin BOOL NOT NULL" +
                ");");
        sqLiteDatabase.execSQL(
                "INSERT INTO usuario(usuario_nombre,usuario_apellido,usuario_dni,usuario_mail," +
                            "usuario_password,usuario_calle,usuario_altura, usuario_admin)" +
                "VALUES('Valentin','Gomez','32897654','vgomez@gmail.com','123','Tucuman','456',false)," +
                "('Ursula','Lopez','22897654','ulopez@gmail.com','123','Lope de Vega','765',false)," +
                "('Estefania','Zuluaga','34897654','ezuluaga@gmail.com','123','Rivadavia','8350',false)," +
                "('Anibal','Estevanes','34987567','aesteves@gmail.com','123','Boyaca','1678',false)," +
                "('Luisa','Dominguez','21897543','ldominguez@gmail.com','123','Independencia','2365',false)," +
                "('Angel Gaston','Mansilla','29437757','gaston@gmail.com','123','Tucuman','456',false), " +
                "('usuario','default','12345678','a','a','direccion','default',true);"
        );
        sqLiteDatabase.execSQL(
            "CREATE TABLE IF NOT EXISTS 'voto'" +
                "(" +
                    "id_voto INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "voto_id_opcion INTEGER  NOT NULL," +
                    "voto_id_usuario INTEGER NOT NULL," +
                    "voto_id_votacion INTEGER NOT NULL," +
                    "FOREIGN KEY(voto_id_opcion) REFERENCES opcion(id_opcion)," +
                    "FOREIGN KEY(voto_id_usuario) REFERENCES usuario(id_usuario)," +
                    "FOREIGN KEY(voto_id_votacion) REFERENCES votacion(id_votacion)" +
                ");"
        );

        sqLiteDatabase.execSQL(
            "CREATE TABLE IF NOT EXISTS 'opcion'" +
                "(" +
                    "id_opcion INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "opcion_descripcion TEXT NOT NULL" +
                ");"
        );
        sqLiteDatabase.execSQL("INSERT INTO opcion" +
                "(opcion_descripcion)" +
                "VALUES('Playa')," +
                "('Montana');"
        );

        sqLiteDatabase.execSQL(
            "CREATE TABLE IF NOT EXISTS 'votacion'" +
                "(" +
                    "id_votacion INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "votacion_id_usuario_organizador INTEGER NOT NULL," +
                    "votacion_descripcion TEXT NOT NULL," +
                    "FOREIGN KEY(votacion_id_usuario_organizador) REFERENCES usuario(id_usuario)" +

                ");"
        );
        sqLiteDatabase.execSQL("INSERT INTO votacion(votacion_id_usuario_organizador, " +
                                "votacion_descripcion)" +
                                "VALUES('6','Playa o montana?');"
        );

        sqLiteDatabase.execSQL("CREATE TABLE usuario_votacion" +
                "(" +
                "id_usuario_votacion INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario_votacion_id_usuario_votante INT NOT NULL," +
                "usuario_votacion_id_votacion INT NOT NULL," +
                "usuario_votacion_id_opcion INT NOT NULL," +
                "FOREIGN KEY(usuario_votacion_id_usuario_votante) REFERENCES usuario(id_usuario)," +
                "FOREIGN KEY(usuario_votacion_id_votacion) REFERENCES votacion(id_votacion)," +
                "FOREIGN KEY(usuario_votacion_id_opcion) REFERENCES opcion(id_opcion)" +
                ");"
        );

        sqLiteDatabase.execSQL("CREATE TABLE opcion_votacion" +
                "(" +
                "id_opcion_votacion INTEGER PRIMARY KEY AUTOINCREMENT," +
                "opcion_votacion_id_votacion INTEGER NOT NULL," +
                "opcion_votacion_id_opcion INTEGER NOT NULL," +
                "FOREIGN KEY(opcion_votacion_id_votacion) REFERENCES votacion(id_votacion)," +
                "FOREIGN KEY(opcion_votacion_id_opcion) REFERENCES opcion(id_opcion));"
        );
        sqLiteDatabase.execSQL("INSERT INTO opcion_votacion" +
                "(opcion_votacion_id_votacion, opcion_votacion_id_opcion)" +
                "VALUES('1','1')," +
                "('1','2');"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
