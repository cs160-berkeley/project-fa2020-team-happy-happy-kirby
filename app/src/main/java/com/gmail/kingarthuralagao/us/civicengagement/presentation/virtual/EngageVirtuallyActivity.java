package com.gmail.kingarthuralagao.us.civicengagement.presentation.virtual;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityEngageVirtuallyBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class EngageVirtuallyActivity extends AppCompatActivity {

    ActivityEngageVirtuallyBinding binding;
    private double raisedDouble;
    private double goalDouble;
    private String raised;
    private String goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEngageVirtuallyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO: Change to event's GoFundMe URL
        String url = "https://www.gofundme.com/f/20rjwcnws0";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    document = Jsoup.connect(url).get();
                    String fullString = document.select(".m-progress-meter-heading").text();
                    System.out.println(fullString);
                    String[] split = fullString.split(" ");
                    raised = split[0];
                    goal = split[3];
                    raisedDouble = Double.parseDouble(raised.substring(1).replaceAll(",", ""));
                    goalDouble = Double.parseDouble(goal.substring(1).replaceAll(",", ""));
                    Log.d("EngageVirtually", "Amount raised: " + raised);
                    Log.d("EngageVirtually", "Amount needed: " + goal);
                } catch (IOException e) {
                    Log.d("EngageVirtually", e.getMessage());
                    e.printStackTrace();
                }
            }

        });
        thread.start();
        try {
            thread.join();
            int progress =  Math.min((int) (100 * (raisedDouble / goalDouble)), 100);
            Log.d("EngageVirtually", "Progress arg: " + progress);
            binding.progressIdc.setProgress(progress);
            binding.fundValueTv.setText(raised);
            binding.goalValueTv.setText(goal + " goal");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}