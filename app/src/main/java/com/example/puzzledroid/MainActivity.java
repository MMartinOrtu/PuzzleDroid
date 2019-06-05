package com.example.puzzledroid;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener {

    ImageView image;
    TextView btnDivide;
    RadioButton three;
    RadioButton four;
    RadioButton five;
    RadioGroup numbers_group;
    Button btnLoad;
    Button btnNewGame;
    Button btnTopscore;
    GridView mGridView;
    TextView labelTimer;
    private ArrayList<ImageData> chunkedImages;
    private ArrayList<ImageData> originalImages;
    private ImageAdapter imageAdapter;
    private int firstChunkSelected = -1;
    private int secondChunkSelected;
    public int difficulty=3;

    private Chronometer timer;
    long stopOffset;
    String TAG = "TAG";
    Boolean resume = false;
    BBDD_Helper helperDB;
    Date currentTime;

    MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =(Toolbar) findViewById(R.id.app_action_bar);
        setSupportActionBar(toolbar);



        image = (ImageView) findViewById(R.id.imageId);
        btnDivide = (TextView) findViewById(R.id.divideImage);
        btnLoad = (Button) findViewById(R.id.btnLoadImage);
        btnNewGame = (Button) findViewById(R.id.btn_newgame);
        btnTopscore = (Button) findViewById(R.id.btn_topscore);
        three = (RadioButton) findViewById(R.id.three);
        four = (RadioButton) findViewById(R.id.four);
        five = (RadioButton) findViewById(R.id.five);
        numbers_group = (RadioGroup) findViewById(R.id.group_numbers);

        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        btnNewGame.setOnClickListener(this);
        btnTopscore.setOnClickListener(this);

        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(this);

        timer = (Chronometer) findViewById(R.id.Timer);
        labelTimer = (TextView) findViewById(R.id.timerLabel);
    }

    @Override
    public void onClick(View v) {
        int chunkNumber = 0;
        image = (ImageView) findViewById(R.id.imageId);

        switch (v.getId()) {
            case  R.id.three: {
                if (image.getDrawable() == null){
                    break;
                }
                chunkNumber = 9;
                difficulty = 3;
                break;
            }
            case  R.id.four: {
                if (image.getDrawable() == null){
                    break;
                }
                chunkNumber = 16;
                difficulty = 4;
                break;
            }
            case  R.id.five: {
                if (image.getDrawable() == null){
                    break;
                }
                chunkNumber = 25;
                difficulty = 5;
                break;
            }
            case R.id.btnLoadImage: {
                loadImage();
                break;
            }
            case R.id.btn_newgame: {
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
            }
            case R.id.btn_topscore: {
                Intent top_score = new Intent(this, ScoreActivity.class);
                Toast.makeText(this,"Your win!"+difficulty, Toast.LENGTH_LONG).show();
                top_score.putExtra("difficulty", difficulty);
                startActivity(top_score);
                break;
            }
        }
        if (chunkNumber > 0){
            splitImage(image, chunkNumber);
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.help:
                Intent help = new Intent(this, HelpActivity.class);
                startActivity(help);
                break;
            case R.id.new_game:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.top_score:
                Intent top_score = new Intent(this, ScoreActivity.class);
                top_score.putExtra("difficulty", difficulty);
                startActivity(top_score);
                break;
            case R.id.exit:
                this.finish();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }


    private void loadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri path=data.getData();
            image.setImageURI(path);

        }
    }
    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }
    private void splitImage(ImageView image, int chunkNumbers) {
        music = MediaPlayer.create(MainActivity.this, R.raw.musicbc2);
        music.start();

        int rows,cols;
        rows = cols = (int) Math.sqrt(chunkNumbers);
        chunkedImages = new ArrayList<ImageData>(chunkNumbers);
        originalImages = new ArrayList<ImageData>(chunkNumbers);
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(image);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        // Calculate the with and height of the pieces
        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        int sequenceNumber = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {

                chunkedImages.add(new ImageData(Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight),sequenceNumber));
                originalImages.add(new ImageData(Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight),sequenceNumber));
                xCoord += pieceWidth;
                sequenceNumber++;
            }
            yCoord += pieceHeight;
        }

        Collections.shuffle(chunkedImages);

        image.setVisibility(View.GONE);
        btnDivide.setVisibility(View.GONE);
        btnLoad.setVisibility(View.GONE);
        numbers_group.setVisibility(View.GONE);


        imageAdapter = new ImageAdapter(this, chunkedImages);
        mGridView.setAdapter(imageAdapter);
        mGridView.setNumColumns((int) Math.sqrt(chunkedImages.size()));
        mGridView.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        labelTimer.setVisibility(View.VISIBLE);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //int firstChunkSelected = -1;
        //int secondChunkSelected;

        if (firstChunkSelected == -1){
            firstChunkSelected = position;
        } else {
            secondChunkSelected = position;
            Collections.swap(chunkedImages, firstChunkSelected, secondChunkSelected);
            firstChunkSelected = -1;
        }
        imageAdapter.notifyDataSetChanged();
        resolvePuzzle();
    }

    private void resolvePuzzle() {
        int correctChunkImages = 0;
        for (int i=0;i<originalImages.size();i++){
           if (originalImages.get(i).getSequenceNumber() == chunkedImages.get(i).getSequenceNumber()){
               correctChunkImages ++;
           }
           if (correctChunkImages == originalImages.size()){
               helperDB = new BBDD_Helper(this);
               currentTime = Calendar.getInstance().getTime();
               String date = DateFormat.format("dd-MM-yyy", currentTime).toString();
               timer.stop();
               stopOffset = SystemClock.elapsedRealtime() - timer.getBase();

               Toast.makeText(this,"You win!", Toast.LENGTH_LONG).show();

               SQLiteDatabase db = helperDB.getWritableDatabase();
               ContentValues values = new ContentValues();
               values.put(Score_BBDD.COLUMN_2, stopOffset);
               values.put(Score_BBDD.COLUMN_3, date);
               values.put(Score_BBDD.COLUMN_4, difficulty);

               db.insert(Score_BBDD.TABLE_NAME, null, values);

               btnNewGame.setVisibility(View.VISIBLE);
               btnTopscore.setVisibility(View.VISIBLE);
               music.release();
           }
        }
    }
}
