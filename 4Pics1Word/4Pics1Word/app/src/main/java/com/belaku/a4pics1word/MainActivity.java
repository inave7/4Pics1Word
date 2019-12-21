package com.belaku.a4pics1word;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    ArrayList<String> alphabets = new ArrayList<String>() {
        {
            add("A");
            add("B");
            add("C");
            add("D");
            add("E");
            add("F");
            add("G");
            add("H");
            add("I");
            add("J");
            add("K");
            add("L");
            add("M");
            add("N");
            add("O");
            add("P");
            add("Q");
            add("R");
            add("S");
            add("T");
            add("U");
            add("V");
            add("W");
            add("X");
            add("Y");
            add("Z");

        }
    };

    private Button BtnNext;

    private TextView textViewAns1, textViewAns2, textViewAns3, textViewAns4, textViewAns5, textViewAns6, textViewAns7,
                        textViewOpts1, textViewOpts2, textViewOpts3, textViewOpts4, textViewOpts5, textViewOpts6, textViewOpts7,
                        textViewOpts8, textViewOpts9, textViewOpts10, textViewOpts11, textViewOpts12;
   // private ImageView imageView1, imageView2, imageView3, imageView4;
    private ImageView imageView;
    private int randomQuestion, screenHeight, screenWidth;
    private RelativeLayout AnswerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getScreenDimens();
        findViewByIds();


       Random random = new Random();
        randomQuestion = random.nextInt(6);
        makeToast("Question - " + randomQuestion);

    loadImage(randomQuestion);

    }

    private void loadImage(int no) {

        switch (no) {
            case 1 :
             //   loadFromDb(1);

                break;
            case 2 :
                
                break;
            case 3 :
                
                break;
            case 4 :
                
                break;
            case 5 :
                
                break;
            case 6 :
                
                break;
            case 7 :
                
                break;


        }
    }



    private void loadOptions(String str) {
        int length = str.length();

        for (int i = 0 ; i < (-1 * (length - 12)); i++) {
            textViewOpts1.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts2.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts3.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts4.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts5.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts6.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts7.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts8.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts9.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts10.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts11.setText(alphabets.get(new Random().nextInt(25)));
            textViewOpts12.setText(alphabets.get(new Random().nextInt(25)));
        }


    }


    private void findViewByIds() {

        BtnNext = findViewById(R.id.btn_next);
        BtnNext.setOnClickListener(this);
        imageView = findViewById(R.id.imgv);

        /*imageView1 = findViewById(R.id.imgv1);
        imageView2 = findViewById(R.id.imgv2);
        imageView3 = findViewById(R.id.imgv3);
        imageView4 = findViewById(R.id.imgv4);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);*/

        textViewAns1 = findViewById(R.id.tx_ans1);
        textViewAns2 = findViewById(R.id.tx_ans2);
        textViewAns3 = findViewById(R.id.tx_ans3);
        textViewAns4 = findViewById(R.id.tx_ans4);
        textViewAns5 = findViewById(R.id.tx_ans5);
        textViewAns6 = findViewById(R.id.tx_ans6);
        textViewAns7 = findViewById(R.id.tx_ans7);


        AnswerLayout = findViewById(R.id.layout_answer);

        textViewOpts1 = findViewById(R.id.tx_opt1);
        textViewOpts2 = findViewById(R.id.tx_opt2);
        textViewOpts3 = findViewById(R.id.tx_opt3);
        textViewOpts4 = findViewById(R.id.tx_opt4);
        textViewOpts5 = findViewById(R.id.tx_opt5);
        textViewOpts6 = findViewById(R.id.tx_opt6);
        textViewOpts7 = findViewById(R.id.tx_opt7);
        textViewOpts8 = findViewById(R.id.tx_opt8);
        textViewOpts9 = findViewById(R.id.tx_opt9);
        textViewOpts10 = findViewById(R.id.tx_opt10);
        textViewOpts11 = findViewById(R.id.tx_opt11);
        textViewOpts12 = findViewById(R.id.tx_opt12);

        textViewOpts1.setOnClickListener(this);
        textViewOpts2.setOnClickListener(this);
        textViewOpts3.setOnClickListener(this);
        textViewOpts4.setOnClickListener(this);
        textViewOpts5.setOnClickListener(this);
        textViewOpts6.setOnClickListener(this);
        textViewOpts7.setOnClickListener(this);
        textViewOpts8.setOnClickListener(this);
        textViewOpts9.setOnClickListener(this);
        textViewOpts10.setOnClickListener(this);
        textViewOpts11.setOnClickListener(this);
        textViewOpts12.setOnClickListener(this);




    }

    private void getScreenDimens() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tx_opt1 :
                SetAnswer(textViewOpts1.getText().toString());
                break;
            case R.id.tx_opt2 :
                SetAnswer(textViewOpts2.getText().toString());
                break;
            case R.id.tx_opt3 :
                SetAnswer(textViewOpts3.getText().toString());
                break;
            case R.id.tx_opt4 :
                SetAnswer(textViewOpts4.getText().toString());
                break;
            case R.id.tx_opt5 :
                SetAnswer(textViewOpts5.getText().toString());
                break;
            case R.id.tx_opt6 :
                SetAnswer(textViewOpts6.getText().toString());
                break;
            case R.id.tx_opt7 :
                SetAnswer(textViewOpts7.getText().toString());
                break;
            case R.id.tx_opt8 :
                SetAnswer(textViewOpts8.getText().toString());
                break;
            case R.id.tx_opt9 :
                SetAnswer(textViewOpts9.getText().toString());
                break;
            case R.id.tx_opt10 :
                SetAnswer(textViewOpts10.getText().toString());
                break;
            case R.id.tx_opt11 :
                SetAnswer(textViewOpts11.getText().toString());
                break;
            case R.id.tx_opt12 :
                SetAnswer(textViewOpts12.getText().toString());
                break;
            case R.id.btn_next :
                Random random = new Random();
                randomQuestion = random.nextInt(8);
                makeToast("Question - " + randomQuestion);
                loadImage(randomQuestion);
                break;

        }
    }

    private void showImage(ImageView imageView) {

        Bitmap bm =((BitmapDrawable)imageView.getDrawable()).getBitmap();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

    }

    ;

    public void SetAnswer(String stringAnswer) {
        if (textViewAns1.getText().length() == 0)
            textViewAns1.setText(stringAnswer);
        else if (textViewAns2.getText().length() == 0)
            textViewAns2.setText(stringAnswer);
        else if (textViewAns3.getText().length() == 0)
            textViewAns3.setText(stringAnswer);
        else if (textViewAns4.getText().length() == 0)
            textViewAns4.setText(stringAnswer);
        else if (textViewAns5.getText().length() == 0)
            textViewAns5.setText(stringAnswer);
        else if (textViewAns6.getText().length() == 0)
            textViewAns6.setText(stringAnswer);
        else if (textViewAns7.getText().length() == 0)
            textViewAns7.setText(stringAnswer);
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                makeToast("Error - " +  e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s,Toast.LENGTH_SHORT).show();
    }
}
