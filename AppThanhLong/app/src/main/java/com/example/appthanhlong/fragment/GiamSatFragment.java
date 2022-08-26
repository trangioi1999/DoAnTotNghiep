package com.example.appthanhlong.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.appthanhlong.R;

public class GiamSatFragment extends Fragment {
        WebView webv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_giam_sat, container, false);
        webv = (WebView) view.findViewById(R.id.webv);
        WebSettings webSettings= webv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        webv.getSettings().setBuiltInZoomControls(true);
        webv.getSettings().setDisplayZoomControls(false);
        webv.getSettings().setUseWideViewPort(true);
        webv.getSettings().setLoadWithOverviewMode(true);
        webv.getSettings().setDomStorageEnabled(true);
        webv.setInitialScale(1);
        webv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webv.loadUrl("http://14.161.8.73:83/GetData.cgi?CH=1#.XZsBhQDb5Ek.link");
        return view;
    }

}
