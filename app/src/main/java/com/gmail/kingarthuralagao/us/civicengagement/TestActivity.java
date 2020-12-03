package com.gmail.kingarthuralagao.us.civicengagement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.kingarthuralagao.us.civilengagement.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        /*
        try {
            try (LanguageServiceClient language = LanguageServiceClient.create()) {

                // The text to analyze
                String text = "My dog just got murdered. I'm so sad.!";
                Document doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();

                // Detects the sentiment of the text
                Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

                Log.i("TestAct", "Text: %s%n " + text);
                Log.i("TestAct", "Sentiment: %s, %s%n " + sentiment.getScore() + " " + sentiment.getMagnitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}