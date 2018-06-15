package com.example.nitishkumar.interviewpreparation;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView questionTextView, answerTextView;
    private Button backwardButtonView, forwardButtonView;
    private RequestQueue mQueue;
    private String url;
    private ArrayList<String> question, answer;
    private int index;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        jsonParsing();
        bannerADD();
    }

    @Override
    protected void onPause() {
        if (mAdView != null)
        {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAdView != null)
        {
            mAdView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null)
        {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void bannerADD()
    {
        if (TextUtils.isEmpty(getString(R.string.banner_home_footer)))
        {
            Toast.makeText(this, "Please Mention Your Banner addID in string.xml", Toast.LENGTH_SHORT).show();
            return;
        }
//        mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId(getString(R.string.banner_home_footer));

        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int i) {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://courses.learncodeonline.in"));
                startActivity(intent);
            }
        });

        mAdView.loadAd(adRequest);
    }

    private void jsonParsing()
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        forwardButtonView.setVisibility(View.VISIBLE);
                        backwardButtonView.setVisibility(View.VISIBLE);
                        try
                        {
                            JSONArray jsonArray = response.getJSONArray("questions");
                            for (int i =0; i < jsonArray.length(); i++)
                            {
                                JSONObject questionAnswer = jsonArray.getJSONObject(i);

                                question.add(questionAnswer.getString("question"));
                                answer.add(questionAnswer.getString("Answer"));
                            }
                            questionTextView.setText("Question 1: " + question.get(0));
                            answerTextView.setText("Answer : " + answer.get(0));
                            setQuestionAnswerInView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void init()
    {
        progressBar = (ProgressBar)findViewById(R.id.serverFetchProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        questionTextView = (TextView)findViewById(R.id.questionText);
        answerTextView = (TextView)findViewById(R.id.answerText);
        backwardButtonView = (Button)findViewById(R.id.backButton);
        forwardButtonView = (Button)findViewById(R.id.forwardButton);
        mQueue = Volley.newRequestQueue(this);
        url = "https://learncodeonline.in/api/android/datastructure.json";
        question = new ArrayList<String>();
        answer = new ArrayList<String>();
        index = 0;

        mAdView = (AdView)findViewById(R.id.adView);
    }

    private void setQuestionAnswerInView()
    {

        forwardButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index++;

                if(index > question.size()-1)
                {
                    index = 0;
                }
//                Toast.makeText(MainActivity.this, index + " " + question.size(), Toast.LENGTH_SHORT).show();
                int temp = index + 1;
                questionTextView.setText("Question "+ temp +" : " + question.get(index));
                answerTextView.setText("Answer : " + answer.get(index));
            }
        });

        backwardButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index--;

                if (index < 0)
                {
                    index = question.size() - 1;
                }

                int temp = index + 1;
                questionTextView.setText("Question "+ temp +" : " + question.get(index));
                answerTextView.setText("Answer : " + answer.get(index));
            }
        });

    }
}