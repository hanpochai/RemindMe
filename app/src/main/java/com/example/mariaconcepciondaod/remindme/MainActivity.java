package com.example.mariaconcepciondaod.remindme;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    EditText edtTitle, edtDesc;
    Button btnChoose, btnAdd, btnList;
    ImageView imageView;
    final int REQUEST_CODE_GALLERY = 999, REQUEST_CODE_CAPTURE=888;
    public static SQLiteHelper sqLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        sqLiteHelper = new SQLiteHelper(this, "Notes.sqlite",null,1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS notes (Id INTEGER PRIMARY KEY AUTOINCREMENT, title text, description text, image BLOB, type text, pass text )");


    }


    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap =((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void message(String message){
        Toast.makeText(getApplicationContext(),  message,Toast.LENGTH_SHORT).show();

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
                imageView.setImageBitmap(bitmap);
                imageView.setEnabled(true);

            } catch (FileNotFoundException e) {
                message(e.toString());
            }

        }else if(requestCode == REQUEST_CODE_CAPTURE && resultCode == RESULT_OK){
Bitmap bitmap=(Bitmap) data.getExtras().get("data");
imageView.setImageBitmap(bitmap);

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public void init(){
        edtTitle = findViewById(R.id.txtTitle);
        edtDesc = findViewById(R.id.txtDesc);


        imageView = findViewById(R.id.imageView);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu,menu);
         return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menuSave){
            try {
                sqLiteHelper.insertData(
                        edtTitle.getText().toString().trim(),
                        edtDesc.getText().toString().trim(),
                        imageViewToByte(imageView)

                );
                message("Added successfully");
                edtTitle.setText("");
                edtDesc.setText("");
                imageView.setImageResource(R.mipmap.ic_launcher);
                Intent intent = new Intent(MainActivity.this,Notelist.class);
                startActivity(intent);
            }
            catch (Exception e){
                message(e.getMessage());
            }
        }

        if(item.getItemId()==R.id.menuGallery){
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY
            );
        }

        if(item.getItemId()==R.id.menuCamera){
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i,REQUEST_CODE_CAPTURE);
        }


        return super.onOptionsItemSelected(item);
    }
}


