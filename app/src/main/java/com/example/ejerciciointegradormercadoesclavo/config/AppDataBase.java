package com.example.ejerciciointegradormercadoesclavo.config;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ejerciciointegradormercadoesclavo.model.Articulo;
import com.example.ejerciciointegradormercadoesclavo.model.ArticuloRoomDao;

@Database(version = 1, exportSchema = false, entities = Articulo.class)
public abstract class AppDataBase extends RoomDatabase {

    //Exponemos el Dao a partir de la DB
    public abstract ArticuloRoomDao articuloRoomDao();

    //Singleton
    public static AppDataBase INSTANCE = null;

    public static AppDataBase getInstance(Context context) {
        /*Primero pregunto si sigue siendo null, para generar la Database.
        Una vez generada ya no va a volver a entrar en este método porque ya no vale null.
        Esa es la magia del SINGLETON (Patrón de diseño).*/
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context,
                            AppDataBase.class,
                            "db-articulos")
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }
}