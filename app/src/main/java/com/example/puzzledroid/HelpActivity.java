package com.example.puzzledroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HelpActivity extends AppCompatActivity {
    private WebView helpView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpView = (WebView) findViewById(R.id.helpView);
        helpView.setWebViewClient(new WebViewClient());
        helpView.loadUrl("https://github.com/MMartinOrtu/PuzzleDroid");
    }

    @Override
    public void onBackPressed() {
        if (helpView.canGoBack()){
            helpView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
