package com.example.mariaconcepciondaod.remindme;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class update_note_activity extends AppCompatActivity {
    int id;

    TextView txtTitle,txtDesc;
    ImageView imgView;
    final int REQUEST_CODE_GALLERY=999,REQUEST_CODE_CAPTURE=888;
    String title;
    String desc;
    byte[] image;
    int num;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_note_activity);
init();
        Bundle bandle = getIntent().getExtras();
        num = bandle.getInt("NoteID");
        Cursor cursor =Notelist.sqlitehelp.getData("SELECT * FROM notes where ID = "+ num +"" );

        while (cursor.moveToNext()){
             id = cursor.getInt(0);
             title = cursor.getString(1);
             desc = cursor.getString(2);
             image = cursor.getBlob(3);

        }

        Notelist.sqlitehelp.deletePassData(num);
        txtTitle.setText(title);
        txtDesc.setText(desc);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
       imgView.setImageBitmap(bitmap);



        final ImageView imgfullview = findViewById(R.id.imgUpdate);

        imgfullview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(update_note_activity.this);
                dialog.setContentView(R.layout.image_activity);
                dialog.setTitle("Update Note");
                ImageView imageFull = dialog.findViewById(R.id.imageFullView);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
imageFull.setImageBitmap(bitmap);


                int width = (int) (update_note_activity.this.getResources().getDisplayMetrics().widthPixels * 1);
                int height = (int) (update_note_activity.this.getResources().getDisplayMetrics().heightPixels * 0.95);


                dialog.getWindow().setLayout(width,height);

                dialog.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.updatemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.updatemenuSave){
            Notelist.sqlitehelp.updateData(
                    num,
                    txtTitle.getText().toString().trim(),
                    txtDesc.getText().toString().trim(),
                    MainActivity.imageViewToByte(imgView)


            );
            message("Update Successfully");
            Intent i = new Intent(update_note_activity.this,Notelist.class);
            startActivity(i);
        }

        else if(item.getItemId()==R.id.updatemenuGallery ){
            ActivityCompat.requestPermissions(
                    update_note_activity.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY
            );
        } else if(item.getItemId()==R.id.updatemenuCamera ){
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i,REQUEST_CODE_CAPTURE);
        }
        else if(item.getItemId()==R.id.updatemenuPersonalLock ){

            final Dialog dialog = new Dialog(update_note_activity.this);
            dialog.setContentView(R.layout.lock_note);
            dialog.setTitle("Lock Note");

            final EditText txtFirstlockpass= dialog.findViewById(R.id.txtlockPassword);
            final EditText txtConfirmlockpass = dialog.findViewById(R.id.txtFinalLockPassword);
            Button btnenter = dialog.findViewById(R.id.btnLockEnter);
            int width = (int) (update_note_activity.this.getResources().getDisplayMetrics().widthPixels * 1);
            int height = (int) (update_note_activity.this.getResources().getDisplayMetrics().heightPixels * 0.9);


            dialog.getWindow().setLayout(width,height);

            dialog.show();


            btnenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (txtFirstlockpass.getText().toString().equals(txtConfirmlockpass.getText().toString())){
                        Notelist.sqlitehelp.updatePassData(
                                num,"Locked", txtConfirmlockpass.getText().toString()
                        );
message(txtConfirmlockpass.getText().toString());
                        message("Note Locked..");
                        Intent i = new Intent(update_note_activity.this,Notelist.class);
                        startActivity(i);
                        dialog.dismiss();

                    }else{
                        message("Wrong Combination of Password");
                        txtConfirmlockpass.setText("");
                        txtFirstlockpass.setText("");

                    }

                }
            });



        }


        else if(item.getItemId()==R.id.updatemenuDefaultLock ){



            int passcount =Notelist.sqlitehelp.getPassCount();

            if (passcount > 0 ){
            Cursor c = Notelist.sqlitehelp.getData("Select * from defaultPassword");
           String defPass="";
            while (c.moveToNext()) {
               defPass=c.getString(0);

            }
            message(defPass);
            Notelist.sqlitehelp.updatePassData(
                    num,"Locked", defPass
            );

            message("Note Locked..");
            Intent i = new Intent(update_note_activity.this,Notelist.class);
            startActivity(i);}
            else
            {
                message("Default Password is Note Set!");
            }
        }
        else if(item.getItemId()==R.id.updatemenuDelete ){


            final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(update_note_activity.this);
            dialogDelete.setTitle("Warning!!");
            dialogDelete.setMessage("Are you sure you want to delete this note?");
            dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Notelist.sqlitehelp.deleteData(num);

                    message("Delete Successfull!!");

                    Intent intent = new Intent(update_note_activity.this,Notelist.class);
                    startActivity(intent);

                }

            }

            );  dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
        dialogDelete.show();}
        return super.onOptionsItemSelected(item);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }
            else {
                message("You don't have permission to access file from gallery");

            }return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){



            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgView.setImageBitmap(bitmap);
                imgView.setEnabled(true);

            } catch (FileNotFoundException e) {
                message(e.toString());
            }

        }else if(requestCode == REQUEST_CODE_CAPTURE && resultCode == RESULT_OK){
            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(bitmap);

        }


        super.onActivityResult(requestCode, resultCode, data);
    }
    public void message(String message){
        Toast.makeText(getApplicationContext(),  message,Toast.LENGTH_SHORT).show();

    }
    public void init(){
        txtTitle = findViewById(R.id.titleUpdate);
        txtDesc=findViewById(R.id.descUpdate);
        imgView = findViewById(R.id.imgUpdate);
    }
}


