package com.e.cloud.wise.webview;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.e.cloud.wise.webview.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Handler handler = new Handler();
    private Runnable refreshRunnable;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

      binding = FragmentFirstBinding.inflate(inflater, container, false);
      return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.etURL.setText("https://services-pat.auda-target.com/ImageCapture/AUVVC4LUK/9805");
        binding.loadUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUrl(binding.etURL.getText().toString());
            }
        });

        startRefreshingWebView();
    }

    private void startRefreshingWebView() {
        // Create a Runnable to refresh the WebView periodically
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                // Refresh the WebView by invalidating it
                binding.webview.postInvalidate();

                // Repeat this runnable after a delay (e.g., 1 second)
                handler.postDelayed(this, 1000); // Adjust the delay as per your needs
            }
        };

        // Start refreshing the WebView
        handler.post(refreshRunnable);
    }


    private void loadUrl(String url) {
        binding.webview.setVisibility(View.VISIBLE);
        binding.webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebView.setWebContentsDebuggingEnabled(true);
        binding.webview.setWebChromeClient(new WebChromeClient());
        binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webview.requestFocus(View.FOCUS_DOWN);
        WebSettings webSettings = binding.webview.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
//        webSettings.setAppCacheMaxSize(1024 * 8);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setAppCacheEnabled(true);
        String DESKTOP_USER_AGENT = "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Mobile Safari/537.36";
        webSettings.setUserAgentString(DESKTOP_USER_AGENT);
        binding.webview.setWebViewClient(new MyWebViewClient());
        binding.webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("WebView", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return true;
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                // Handle file input here and call filePathCallback with the selected file(s) URI
                return true;
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
                    request.grant(request.getResources());
                }
            }
        });

        binding.webview.loadUrl(url);
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // Return false to allow the WebView to handle the URL loading internally
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d("WebView", description);
            // Handle the error here
            // You can log the error, show an error message, or take any other appropriate action
        }
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}