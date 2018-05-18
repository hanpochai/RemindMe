package com.example.mariaconcepciondaod.remindme;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class locked_note_list extends AppCompatActivity {


    public static GridView gridView;
    NoteListAdapter adapter = null;
    ArrayList<note> list;
    SQLiteHelper sqlitehelp;
    String type;
    ArrayList<Integer> arrID ;
    ArrayList<String> arrType ;
    ArrayList<String> arrPass ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_note_list);
        sqlitehelp = new SQLiteHelper(this, "Notes.sqlite", null, 1);
        sqlitehelp.queryData("CREATE TABLE IF NOT EXISTS notes (Id INTEGER PRIMARY KEY AUTOINCREMENT, title text, description text, image BLOB, type text, pass text )");


        gridView = findViewById(R.id.locknotegridView);
        list = new ArrayList<>();
        adapter = new NoteListAdapter(this, R.layout.note_items, list);
        gridView.setAdapter(adapter);


        Cursor cursor = sqlitehelp.getData("SELECT * FROM notes where Type ='Locked'");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String desc = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            String type = cursor.getString(4);

            list.add(new note(id, title, desc, image, type));


        }

        adapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int positios, long l) {
                Cursor c = sqlitehelp.getData("Select Id, type,pass from notes where Type ='Locked'");
                arrID = new ArrayList<>();
                arrType = new ArrayList<>();
                arrPass = new ArrayList<>();
                while (c.moveToNext()) {
                    arrID.add(c.getInt(0));
                    arrType.add(c.getString(1));
                    arrPass.add(c.getString(2));

                }

                    final Dialog dialog = new Dialog(locked_note_list.this);
                    dialog.setContentView(R.layout.unlock_note);
                    dialog.setTitle("Unlock Note");

                    final EditText txtUnlockpass = dialog.findViewById(R.id.txtUnlockPassword);
                    Button btnenter = dialog.findViewById(R.id.btnEnter);
                    int width = (int) (locked_note_list.this.getResources().getDisplayMetrics().widthPixels * 0.80);
                    int height = (int) (locked_note_list.this.getResources().getDisplayMetrics().heightPixels * 0.6);


                    dialog.getWindow().setLayout(width, height);

                    dialog.show();

                    btnenter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (txtUnlockpass.getText().toString().equals(arrPass.get(positios))) {


                                Intent intent = new Intent(locked_note_list.this, update_note_activity.class);
                                intent.putExtra("NoteID", arrID.get(positios));
                                startActivity(intent);
                            } else {
                                message("Wrong Pass");
                            }
                        }
                    });

                }

        });

    }
        public void message(String message){
            Toast.makeText(getApplicationContext(),  message,Toast.LENGTH_SHORT).show();

        }

    }