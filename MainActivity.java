package com.example.onlinedownloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    public TextView txt;
    public Button btn1;
    public Button btn2;
    public Button prv;
    public ImageView imgv;
public TextView imgname;
public Uri downloaduri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.downloadtxt);
        btn1 = findViewById(R.id.pastebtn);
        btn2 = findViewById(R.id.downloadbtn);
        imgv = findViewById(R.id.imageView);
        imgname = findViewById(R.id.downloadtxt2);
        prv = findViewById(R.id.previewbtn);

        //When the paste button is clicked

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                String pastedtxt = "";
                ClipData abc = clip.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);
                pastedtxt = item.getText().toString().trim();
                txt.setText(pastedtxt);
                Toast.makeText(getApplicationContext(),"Url Pasted" , Toast.LENGTH_SHORT).show();


            }
        });

             //When Download button is clicked
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 1);

                } catch (Exception d) {
                    d.printStackTrace();
                    Log.i("permission", "Permission wasnt granted");
                }
                try {
                    saveImage(gtBitmap(),imgname.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    txt.setText(null);
            }
        });
         //When Preview button is Clicked
        prv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(txt.getText().toString()).into(imgv);
            }
        });
    }
 //Method used Save Bitmap of ImageView to Folder of choice
    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getApplicationContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "OnlineDownloader");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + "OnlineDownloader";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }
try {
    saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
    fos.flush();
    fos.close();
    Toast.makeText(this,"Image Downloaded" , Toast.LENGTH_SHORT).show();

}
catch (Exception e)
{
    Toast.makeText(this,"Image Download Failed" + e.toString() , Toast.LENGTH_SHORT).show();

}

    }


    //Method used to get Bitmap of Imageview
    public Bitmap gtBitmap(){

        Bitmap btm = Bitmap.createBitmap(imgv.getWidth(),imgv.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas cnv = new Canvas(btm);
        imgv.draw(cnv);
return btm;
    }






//Method used to download image with url right into download folder

    public void downloadImagenew (String filename, String downloadurl){
        try{
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
             downloaduri = Uri.parse(downloadurl);
            DownloadManager.Request rq = new DownloadManager.Request(downloaduri);
            rq.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                    .setAllowedOverRoaming(true)
                    .setTitle(filename)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,  filename + ".jpg");
            dm.enqueue(rq);
            Toast.makeText(this,"Image Download Started" , Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){
            Toast.makeText(this,"Image Download Failed" , Toast.LENGTH_SHORT).show();
        }

    }




    }


