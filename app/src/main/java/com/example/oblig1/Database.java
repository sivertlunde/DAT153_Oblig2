package com.example.oblig1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Database extends BaseActivity {

    private ArrayList<Image> images;
    private ListView list;
    private CustomList adapter;
    private FloatingActionButton addButton;
    private ImageRepository repo;


    public void addPhoto(View view){
        Intent intent = new Intent(this, AddPhoto.class);
        startActivity(intent);
    }

    private void allImagesFromDb(){

        class GetAllImages extends AsyncTask<Void, Void, List<Image>> {

            @Override
            protected List<Image> doInBackground(Void... voids) {
                List<Image> imageList = repo.getImageDao().getAllImages();
                return imageList;
            }

            @Override
            protected void onPostExecute(List<Image> imageList) {
                super.onPostExecute(imageList);
                images = (ArrayList<Image>) imageList;
                adapter = new
                        CustomList(Database.this, images);
                list=(ListView)findViewById(R.id.list);
                list.setAdapter(adapter);
                makeEventListeners();
            }
        }
        GetAllImages gai = new GetAllImages();
        gai.execute();
    }

    private void deleteImage(final Image toBeRemoved){

        class DeleteImage extends AsyncTask<Void, Void, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                Integer numDeleted = repo.getImageDao().deleteImage(toBeRemoved.getImageId());
                return numDeleted;
            }

            @Override
            protected void onPostExecute(Integer numDeleted) {
                super.onPostExecute(numDeleted);
                if (numDeleted > 0){
                    adapter.remove(toBeRemoved);
                    images.remove(toBeRemoved);
                    Toast.makeText(Database.this, "You deleted " +toBeRemoved.getName()+ " from the database", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Database.this, "Something went wrong, try again later", Toast.LENGTH_SHORT).show();
                }
            }
        }
        DeleteImage di = new DeleteImage();
        di.execute();
    }

    private void makeEventListeners(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Database.this, "You Clicked at " +images.get(position).getName(), Toast.LENGTH_SHORT).show();

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(Database.this);
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete "+images.get(pos).getName()+ " from the database?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Image toBeRemoved = images.get(pos);
                                deleteImage(toBeRemoved);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(Database.this, "Deletion cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        repo = new ImageRepository(getApplication());
        allImagesFromDb();
    }

    public int getCount(){
        return images.size();
    }


}
