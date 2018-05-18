package com.example.mariaconcepciondaod.remindme;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Notelist extends AppCompatActivity
{
GridView gridView,lockGridVuew;
FloatingActionButton fab_new;
ArrayList<note> list;
NoteListAdapter adapter = null;
    ImageView imgUpdate;
    ImageView imgViewlock;
    String type;
    ArrayList<Integer> arrID ;
    ArrayList<String> arrType ;
    ArrayList<String> arrPass ;
    public static SQLiteHelper sqlitehelp;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list_activity);
        sqlitehelp = new SQLiteHelper(this, "Notes.sqlite", null, 1);
        sqlitehelp.queryData("CREATE TABLE IF NOT EXISTS notes (Id INTEGER PRIMARY KEY AUTOINCREMENT, title text, description text, image BLOB, type text, pass text )");
        sqlitehelp.queryData("CREATE TABLE IF NOT EXISTS defaultPassword (pass text)");

        fab_new = (FloatingActionButton) findViewById(R.id.floatingAdd);
//Changes
        fab_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Notelist.this, MainActivity.class);
                startActivity(i);
            }
        });
        gridView = findViewById(R.id.notegridView);

        list = new ArrayList<>();
        adapter = new NoteListAdapter(this, R.layout.note_items, list);
        gridView.setAdapter(adapter);



        Cursor cursor = sqlitehelp.getData("SELECT * FROM notes where Type ='Unlocked'");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String desc = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
type = cursor.getString(4);

            list.add(new note(id, title, desc, image,type));


            UpdateNoteList();
        }

        adapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int positios, long l) {
                Cursor c = sqlitehelp.getData("Select Id, type,pass from notes where type ='Unlocked'");
                 arrID = new ArrayList<>();
                 arrType = new ArrayList<>();
               arrPass = new ArrayList<>();
                while (c.moveToNext()) {
                    arrID.add(c.getInt(0));
                    arrType.add(c.getString(1));
                    arrPass.add(c.getString(2));

                }

                if (arrType.get(positios).equals("Unlocked")){
                Intent intent = new Intent(Notelist.this, update_note_activity.class);
                intent.putExtra("NoteID", arrID.get(positios));
                startActivity(intent);}
                else
                {

                }
            }
        });




    }
    @Override
    public void onBackPressed() {
        finish();
        finish();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }
    public void UpdateNoteList(){

        Cursor cursors = sqlitehelp.getData("SELECT * FROM notes where Type ='Unlocked'");
        list.clear();
        while (cursors.moveToNext()){
            int id = cursors.getInt(0);
            String title = cursors.getString(1);
            String desc = cursors.getString(2);
            byte[] image = cursors.getBlob(3);
            String type = cursors.getString(4);
            list.add(new note(id,title,desc,image,type));

        }

        adapter.notifyDataSetChanged();

    }


    public void message(String message){
        Toast.makeText(getApplicationContext(),  message,Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 888){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,888);
            }
            else {
                message("You don't have permission to access file from gallery");

            }return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 888 && resultCode == RESULT_OK && data != null){


            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgUpdate.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menuLocked){
            Intent i = new Intent(Notelist.this,locked_note_list.class);
            startActivity(i);
        }

        if(item.getItemId()==R.id.setPass){

int passcount =sqlitehelp.getPassCount();
if (passcount > 0){

    message("Default Password is already Set : " + sqlitehelp.getPass());
}else {
    final Dialog dialog = new Dialog(Notelist.this);
    dialog.setContentView(R.layout.lock_note);
    dialog.setTitle("Set Default Password");

    final EditText txtFirstlockpass = dialog.findViewById(R.id.txtlockPassword);
    final EditText txtConfirmlockpass = dialog.findViewById(R.id.txtFinalLockPassword);
    Button btnenter = dialog.findViewById(R.id.btnLockEnter);
    int width = (int) (Notelist.this.getResources().getDisplayMetrics().widthPixels * 1);
    int height = (int) (Notelist.this.getResources().getDisplayMetrics().heightPixels * 0.9);


    dialog.getWindow().setLayout(width, height);

    dialog.show();


    btnenter.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (txtFirstlockpass.getText().toString().equals(txtConfirmlockpass.getText().toString())) {
                sqlitehelp.insertDataForDefaultPassword(txtConfirmlockpass.getText().toString());

                message("Default Password Set");

                dialog.dismiss();

            } else {
                message("Wrong Combination of Password");
                txtConfirmlockpass.setText("");
                txtFirstlockpass.setText("");

            }

        }
    });
}
        }
        return super.onOptionsItemSelected(item);
    }
}


