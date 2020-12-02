package com.gmail.kingarthuralagao.us.civicengagement.presentation.virtual;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityEngageVirtuallyBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class EngageVirtuallyActivity extends AppCompatActivity {

    ActivityEngageVirtuallyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEngageVirtuallyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the amount raised and goal amount from the Go Fund Me page.
        String url = "https://www.gofundme.com/f/memorial-expenses-for-sig-and-helen-decker";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    document = Jsoup.connect(url).get();
                    String fullString = document.select(".m-progress-meter-heading").text();
                    System.out.println(fullString);
                    String[] split = fullString.split(" ");
                    String raised = split[0];
                    String goal = split[3];
                    Log.d("EngageVirtually", "Amount raised: " + raised);
                    Log.d("EngageVirtually", "Amount needed: " + goal);
                } catch (IOException e) {
                    Log.d("EngageVirtually", e.getMessage());
                    e.printStackTrace();
                }

            }
        });


        binding.progressIdc.setProgress(50);
    }
}