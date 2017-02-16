package rahul.kulhalli.colorsuggestor;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

public class ColorActivity extends AppCompatActivity {

    private Uri imageUri;
    private ImageView imView;

    private Bitmap doBitmapStuff(){
        Bitmap image = null;
        try{
            image = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            return image;
        }catch(FileNotFoundException e){
            Log.d("SOME_IO_EXCEPTION", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        imView = (ImageView) findViewById(R.id.image);

        imageUri = Uri.parse(getIntent().getStringExtra("uri"));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        final Bitmap toShow = doBitmapStuff();

        final Bitmap _toShow = Bitmap.createScaledBitmap(toShow, imView.getWidth(), imView.getHeight(), false);

        if(toShow != null){
            imView.setImageBitmap(_toShow);
        }

        final ImageFragment fragment = (ImageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        imView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(toShow != null){

                    int x_coordinate = (int) motionEvent.getX();
                    int y_coordinate = (int) motionEvent.getY();

                    int color = _toShow.getPixel(x_coordinate, y_coordinate);
                    fragment.updateColor(color);
                }
                else{
                    Toast.makeText(ColorActivity.this, "Sorry, the image couldn't be fetched...", Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });
    }
}
