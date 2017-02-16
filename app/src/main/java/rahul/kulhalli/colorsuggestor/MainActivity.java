package rahul.kulhalli.colorsuggestor;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    final int INTERNET_PERMISSION_REQUEST_CODE = 2;
    final int EXT_WRITE_PERMISSION_REQUEST_CODE = 3;
    final int EXT_READ_PERMISSION_REQUEST_CODE = 4;

    final int CAMERA_REQUEST_INTENT_CODE = 0;
    static final String DIR_NAME = "color_matcher";
    static final String IMAGE_NAME = "my_image.jpg";

    private Uri imageUri;

    private void permCheck(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_REQUEST_CODE);
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXT_WRITE_PERMISSION_REQUEST_CODE);
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXT_READ_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case CAMERA_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission granted!
                    Toast.makeText(MainActivity.this, "Camera permission has been granted!", Toast.LENGTH_LONG).show();
                }else{
                    //boo!
                    Toast.makeText(MainActivity.this, "Boo, camera permission was not granted!", Toast.LENGTH_LONG).show();
                }
            }

            case INTERNET_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this, "Internet permission has been granted!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Boo, internet permission was not granted!", Toast.LENGTH_LONG).show();
                }
            }

            case EXT_WRITE_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this, "Ext. Storage write permission has been granted!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Boo, Ext. Storage write permission was not granted!", Toast.LENGTH_LONG).show();
                }
            }

            case EXT_READ_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this, "Ext. Storage read permission has been granted!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Boo, Ext. Storage read permission was not granted!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private File setUpDirectory(){
        File directory = new File(Environment.getExternalStorageDirectory()+"/"+DIR_NAME);

        if(directory.exists()){
            return directory;
        }

        if(directory.mkdir()){
            return  directory;
        }

        //else,
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView cameraButton = (ImageView) findViewById(R.id.camera);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    permCheck();
                }

                File myImageFile = new File(setUpDirectory()+"/"+IMAGE_NAME);
                imageUri = Uri.fromFile(myImageFile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_INTENT_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_INTENT_CODE && resultCode == RESULT_OK){
            Uri tempUri = null;

            if(data != null){
                tempUri = data.getData();
            }
            else{
                tempUri = imageUri;
            }

            Log.d("IMAGE_URI", tempUri.toString());

            //Intent goToCameraActivity = new Intent(Intent.ACTION_VIEW);
            //goToCameraActivity.setClass(MainActivity.this, ColorActivity.class);
            Intent goToCameraActivity = new Intent(MainActivity.this, ColorActivity.class);
            goToCameraActivity.putExtra("uri", tempUri.toString());
            startActivity(goToCameraActivity);
        }
    }
}
