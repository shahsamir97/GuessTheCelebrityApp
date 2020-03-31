package com.example.guessthecelebrityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    DownloadDataTask downloadDataTask;

    ArrayList<String> nameList;
    ArrayList<String> urlArrayList;
    int correctAnswerPosition;
    int correctCelebPosition;
    Button button1, button2, button3, button4;
    ImageView imageView;
    TextView answerTextView, scoreTextView;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameList = new ArrayList<String>();
        urlArrayList = new ArrayList<String>();

        answerTextView = findViewById(R.id.answerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        imageView = findViewById(R.id.imageView);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        DownloadDataTask downloadTask = new DownloadDataTask();
        String webContent = "";
        try {
            webContent =  downloadTask.execute("http://cdn.posh24.se/kandisar").get();
            Log.i("WebContent :", webContent);

        } catch (Exception exception) {
            Log.i("EXCEPTION INFO: ",exception.getMessage());
        }
        Pattern pattern1 = Pattern.compile("<div class=\"name\">\n" +
                "\t\t\t\t\t\t\t(.*?)\n" +
                "\t\t\t\t\t\t</div>");          //&&img src=\"(.*?)\"");
        Pattern pattern2 = Pattern.compile("img src=\"(.*?)\"");
        Matcher matcher1 = pattern1.matcher(webContent);
        Matcher matcher2 = pattern2.matcher(webContent);

        Log.i("###Separater", "Manipulated content");
        String content ="";
        while (matcher1.find()) {
            nameList.add(matcher1.group(1));
        }
        while (matcher2.find()) {
                urlArrayList.add(matcher2.group(1));
        }
        Log.i("Names ###", nameList.toString());
        Log.i("content info #", urlArrayList.toString());
        generateOptions();
        downloadCelebrityImage();
    }

    private void generateOptions() {
        Random random = new Random();
        int numberOfCelebrity = nameList.size();
        correctAnswerPosition = random.nextInt(3)+1;
        if (correctAnswerPosition == 0) {
            correctAnswerPosition = 1;
        }
        int firstNamePos = random.nextInt(numberOfCelebrity-1) + 1;
        int secondNamePos = random.nextInt(numberOfCelebrity-1) + 1;
        int thirtNamePos = random.nextInt(numberOfCelebrity - 1) + 1;
        correctCelebPosition = random.nextInt(numberOfCelebrity - 1) + 1;


        String firstCelebName = nameList.get(firstNamePos);
        String secondCelebName = nameList.get(secondNamePos);
        String thirdCelebName = nameList.get(thirtNamePos);
        String correctCelebName = nameList.get(correctCelebPosition);

        if (correctAnswerPosition == 1) {
            button1.setText(correctCelebName);
            button2.setText(firstCelebName);
            button3.setText(secondCelebName);
            button4.setText(thirdCelebName);
        } else if (correctAnswerPosition == 2) {
            button1.setText(firstCelebName);
            button2.setText(correctCelebName);
            button3.setText(secondCelebName);
            button4.setText(thirdCelebName);
        } else if (correctAnswerPosition == 3) {
            button1.setText(firstCelebName);
            button2.setText(secondCelebName);
            button3.setText(correctCelebName);
            button4.setText(thirdCelebName);
        } else {
            button1.setText(firstCelebName);
            button2.setText(secondCelebName);
            button3.setText(thirdCelebName);
            button4.setText(correctCelebName);
        }

    }

    public void downloadCelebrityImage() {
        String celebrityImageUrl = urlArrayList.get(correctCelebPosition);
        DownloadImageTask downloadImageTask = new DownloadImageTask();
        try {
            Bitmap celebrityImage = downloadImageTask.execute(celebrityImageUrl).get();
            imageView.setImageBitmap(celebrityImage);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void checkAnswer(View view) {
        if (view.getTag().toString().equals(Integer.toString(correctAnswerPosition))) {
            answerTextView.setText("Correct!");
            score++;
            scoreTextView.setText("Score :"+Integer.toString(score));
            generateOptions();
            downloadCelebrityImage();
        } else {
            answerTextView.setText("Wrong!");
            generateOptions();
            downloadCelebrityImage();
        }
    }
}
