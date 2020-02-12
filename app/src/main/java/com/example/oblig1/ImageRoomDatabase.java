package com.example.oblig1;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Image.class}, version = 1)
public abstract class ImageRoomDatabase extends RoomDatabase {

    public abstract ImageDao imageDao();

    private static ImageRoomDatabase INSTANCE;

    static ImageRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ImageRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ImageRoomDatabase.class, "image_database")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
