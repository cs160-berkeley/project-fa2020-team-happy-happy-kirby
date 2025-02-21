package com.gmail.kingarthuralagao.us.civicengagement.presentation.virtual;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentEngageVirtuallyBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class EngageVirtuallyFragment extends Fragment {

    public static EngageVirtuallyFragment newInstance(String link) {
        EngageVirtuallyFragment fragment = new EngageVirtuallyFragment();
        Bundle args = new Bundle();
        args.putString(LINK, link);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String LINK = "goFundMeLink";
    private FragmentEngageVirtuallyBinding binding;
    private double raisedDouble;
    private double goalDouble;
    private String raised;
    private String goal;
    private String mLink;
    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLink = getArguments().getString(LINK);
        }
        binding = FragmentEngageVirtuallyBinding.inflate(getLayoutInflater());

        if (mLink != null && mLink.length() > 0) {
            initializeGoFundMeLinkFetch();
            /* Direct user to GoFundMe page on web browser. */
            binding.progressIdc.setOnClickListener(v -> {
                Log.d("EngageVirtually", "Going to GoFundMe page: " + mLink);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mLink));
                startActivity(browserIntent);
            });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }

    private void initializeGoFundMeLinkFetch() {
        // TODO: Change to event's GoFundMe URL
        //String url = "https://www.gofundme.com/f/20rjwcnws0";
        Thread thread = new Thread(() -> {
            Document document;
            try {
                document = Jsoup.connect(mLink).get();
                String fullString = document.select(".m-progress-meter-heading").text();
                name = document.select(".a-campaign-title").text();
                System.out.println(fullString);
                String[] split = fullString.split(" ");
                Log.d("EngageVirtually", "fullString: " + fullString);
                name = document.select(".a-campaign-title").text();
                raised = split[0];
                goal = split[3];
                raisedDouble = Double.parseDouble(raised.substring(1).replaceAll(",", ""));
                goalDouble = Double.parseDouble(goal.substring(1).replaceAll(",", ""));
                Log.d("EngageVirtually", "name: " + name);
                Log.d("EngageVirtually", "Amount raised: " + raised);
                Log.d("EngageVirtually", "Amount needed: " + goal);
            } catch (IOException e) {
                Log.d("EngageVirtually", e.getMessage());
                e.printStackTrace();
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
            binding.engageVirtuallyTv.setVisibility(View.VISIBLE);
            binding.noInfoAvailableTv.setVisibility(View.GONE);
            binding.fundCv.setVisibility(View.VISIBLE);
            binding.engageVirtuallyTv.setText(getString(R.string.virtualengage_text) + '\n' + name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}