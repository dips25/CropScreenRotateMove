package com.layout.swiiiipe.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.InputStream;

public class CropActivity extends AppCompatActivity {

    CropView cropView;


    RelativeLayout mainFRame;

    Rect srcRect, destRect;

    ImageView erase;
    ImageView scale;
    ImageView mWand;

    ImageView rotate;

    ImageView save;

    ProgressDialog progressDialog;

    MyDrawingSurface md;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Uri uri = Uri.parse(data);

        try {

            InputStream is = getContentResolver().openInputStream(uri);

            byte[]bytes = new byte[is.available()];

            is.read(bytes,0,bytes.length);

            Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

            md = (MyDrawingSurface) findViewById(R.id.mysurface);
            md.setImage(bmp);

        } catch (Exception ex) {


        }



        //File file =  new File(path);

        //byte[] bytes = intent.getByteArrayExtra("data");


        erase = (android.widget.ImageView) findViewById(R.id.erase);
        scale = (android.widget.ImageView) findViewById(R.id.scale);
        mWand=  (android.widget.ImageView) findViewById(R.id.mwand);
        rotate = (ImageView) findViewById(R.id.rotate);





        ColorStateList colorStateList = ContextCompat.getColorStateList(this , R.color.icon_tint);
        erase.setImageTintList(colorStateList);
        scale.setImageTintList(colorStateList);
        mWand.setImageTintList(colorStateList);
        rotate.setImageTintList(colorStateList);

        erase.setSelected(false);
        erase.invalidate();

        scale.setSelected(false);
        scale.invalidate();

        mWand.setSelected(false);
        mWand.invalidate();

        rotate.setSelected(false);
        rotate.invalidate();










        //Bitmap bitmap = reduceSize(file , Utils.getScreenWidth(this) , 1000);


//        srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        destRect = new Rect(0, 0, Utils.getScreenWidth(this) , 600);
//
//        float srcAspect = bitmap.getWidth() / bitmap.getHeight();
//        float destAspect = Utils.getScreenWidth(this)/Utils.getScreenHeight(this);
//
//        int total = bitmap.getWidth() + bitmap.getHeight();
//
//        if (bitmap.getWidth() > destRect.width()) {
//
//            destRect = new Rect(0 , 0 , Utils.getScreenWidth(this) , (int)(total/srcAspect));
//
//        } else {
//
//            destRect = new Rect(0 , 0 , (int)(total*srcAspect) , 600);
//
//        }



//        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth() , bitmap.getHeight() , Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap1);
//        canvas.drawBitmap(bitmap , 0 , 0 , null);


//
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Utils.getScreenWidth(this)
//                , 600);
//        params.bottomMargin = Utils.PixeltoDp(this , 50);
//        md.setLayoutParams(params);

        erase.setOnClickListener((v) -> {

            scale.setSelected(false);
            scale.invalidate();

            mWand.setSelected(false);
            mWand.invalidate();

            rotate.setSelected(false);
            rotate.invalidate();

            erase.setSelected(true);
            erase.invalidate();

            md.setMode(1);


        });

        scale.setOnClickListener((v) -> {

            erase.setSelected(false);
            erase.invalidate();

            mWand.setSelected(false);
            mWand.invalidate();

            rotate.setSelected(false);
            rotate.invalidate();

            scale.setSelected(true);
            scale.invalidate();

            md.setMode(2);

        });



        mWand.setOnClickListener((v)->{

           md.setMode(5);

            scale.setSelected(false);
            scale.invalidate();

            erase.setSelected(false);
            erase.invalidate();

            rotate.setSelected(false);
            rotate.invalidate();

            mWand.setSelected(true);
            mWand.invalidate();

//            md.setMode(3);
        });

//        rotate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                erase.setSelected(false);
//                erase.invalidate();
//
//                mWand.setSelected(false);
//                mWand.invalidate();
//
//                scale.setSelected(false);
//                scale.invalidate();
//
//                rotate.setSelected(true);
//                rotate.invalidate();
//
//                md.setMode(4);
//
//            }
//        });

//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                md.saveImageToGallery(CropActivity.this);
//
//            }
//        });

        scale.setSelected(true);
        scale.invalidate();

        md.setMode(2);

        //mainFRame.addView(md);

        //md.setMode(2);
    }

    private Bitmap reduceSize(File bitmapFile , int width , int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bitmap1 = BitmapFactory.decodeFile(bitmapFile.getPath() , options);

        int halfWidth = 0;
        int halfHeight = 0;
        int inSampleSize=1;

        if (options.outWidth>width || options.outHeight>height) {

            halfWidth = width/2;
            halfHeight = height/2;

            while ((halfWidth/inSampleSize>=width) && (halfHeight/inSampleSize>=height)) {

                inSampleSize*=2;
            }
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds=  false;

        Bitmap bmp = BitmapFactory.decodeFile(bitmapFile.getPath() , options);
        return bmp;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.rotate) {

            if (md!=null) {

                md.rotate(90);

            }

            return true;

        } else if (item.getItemId() == R.id.save) {

            if (md!=null) {

                md.saveImageToGallery(this);

            }

            return true;
        }

        return false;
    }
}
