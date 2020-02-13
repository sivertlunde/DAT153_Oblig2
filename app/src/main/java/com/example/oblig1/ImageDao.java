package com.example.oblig1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDao {
    @Query("SELECT * FROM image")
    List<Image> getAllImages();

    @Insert
    void addImage(Image image);

    @Query("DELETE FROM image WHERE imageId = :id")
    int deleteImage(int id);

    @Query("SELECT * FROM image WHERE imageId NOT LIKE :previousId ORDER BY RANDOM() LIMIT 1;")
    Image getRandomImage(int previousId);

    @Query("DELETE FROM image")
    void nukeTable();
}
