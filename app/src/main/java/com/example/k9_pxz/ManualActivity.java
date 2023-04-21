package com.example.k9_pxz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import android.webkit.WebView;

import Util.FileUtils;
import Util.Rev;

public class ManualActivity extends AppCompatActivity {
    private static final String TAG = "ManualActivity";
    //
    TextView tvManualRev;
    WebView webview ;
    PDFView pdfViewK9;

    //revision
    private Rev rev = new Rev();

    // for PDF view.
    //PDFView pdfView;
    //locATION OF THE MANUAL
    String pdfurl = "https://drive.google.com/file/d/1hzyLk88dUOuFaFJyDvU_75f4rKXdtHmK/view?usp=sharing";

    String urlManual="https://mindorks.s3.ap-south-1.amazonaws.com/courses/MindOrks_Android_Online_Professional_Course-Syllabus.pdf";

    //"http://docs.google.com/gview?embedded=true&url=https://drive.google.com/file/d/1hzyLk88dUOuFaFJyDvU_75f4rKXdtHmK/view?usp=sharing"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        //screen full size
        screenFullSize();
        //load layout
        loadLayout(R.layout.activity_manual);
        //remove menu bar
        removeMenuBar();
        //remove action bar from top
        removeActionBar();
        //init
        initGUI();
        //Adding revision
        displaySoftRev(rev.APP_REV_PAGE_13);
        //init PDF
        //initPDF();

        //web view
        //initWebView();

        loadPdf();
    }

    //GUI
    private void initGUI() {
        tvManualRev = (TextView) findViewById(R.id.tvManualRev);
        webview = (WebView) findViewById(R.id.webview);
        pdfViewK9=findViewById(R.id.pdfView);
    }

    //load layout
    private void loadLayout(int layout) {
        try {
            //setContentView(R.layout.activity_main);
            setContentView(layout);
        } catch (Exception e) {
            Log.d(TAG, "loadLayout: ex:" + e.getMessage());
        }
    }

    //display software revision
    private void displaySoftRev(String revision) {
        if (revision != null) {
            if (tvManualRev != null) {
                tvManualRev.setText(revision);
            }
        }
    }

    //remove action bar
    private void removeActionBar() {
        // Take instance of Action Bar
        // using getSupportActionBar and
        // if it is not Null
        // then call hide function
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } catch (Exception e) {
            Log.d(TAG, "removeActionBar: ex:" + e.getMessage());
        }

    }

    //remove action bar
    private void screenFullSize() {
        try {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            Log.d(TAG, "screenFullSize: ex:" + e.getMessage());
        }
    }

    //remove menu bar
    private void removeMenuBar() {
        try {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);


        } catch (Exception e) {
            Log.d(TAG, "removeMenuBar: ex:" + e.getMessage());
        }
    }

    //load manual
    private void initPDF() {
        Log.d(TAG, "initPDF: ");
        // initializing our pdf view.
        //pdfView = findViewById(R.id.idPDFView);
        new RetrivePDFfromUrl().execute(pdfurl);
    }

    private void loadPdf(){
        try {
            if(pdfViewK9!=null){
                pdfViewK9.fromAsset("k9_pxz_3_2_6.pdf").load();
            }
        }catch (Exception e){
            Log.d(TAG, "loadPdf: ex:"+e.getMessage());
        }
    }

    //
    private void initWebView(){
        webview.loadUrl("http://www.google.com");


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "initWebView: "+webview.getSettings());
            webview.getWebViewClient();
            Log.d(TAG, "initWebView: "+webview.getWebViewClient());
            webview.getSettings().setSupportZoom(true);
            webview.getSettings().setJavaScriptEnabled(true);
            //String  url= FileUtils.getExtension(urlManual);
            String  url=urlManual;
            //String  url1= FileUtils.openFile(this,urlManual);
            Log.d(TAG, "initWebView: url:"+url);
            webview.loadUrl("https://docs.google.com/gview?embedded=true&url=$url");
        }*/

        //webview.getSettings().setJavaScriptEnabled(true);
        //webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfurl);
    }

    // create an async task class for loading pdf file from URL.
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: enter ");
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                Log.d(TAG, "doInBackground: url:"+url);
                // below is the step where we are
                // creating our connection.
                Log.d(TAG, "doInBackground: creating our connection");

                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                Log.d(TAG, "doInBackground: connection res:"+urlConnection);
                //
                Log.d(TAG, "doInBackground:url response: "+urlConnection.getResponseCode());

                //
                Log.d(TAG, "doInBackground: con. mesa:"+ urlConnection.getResponseMessage());
                //urlConnection.getResponseMessage();

                if (urlConnection.getResponseCode() == 200) {
                    Log.d(TAG, "doInBackground:response is success ");
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    Log.d(TAG, "doInBackground:inputStream " + inputStream);
                }

            } catch (IOException e) {
                Log.d(TAG, "doInBackground: ex:" + e.getMessage());
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            Log.d(TAG, "onPostExecute:after the execution of our async ");
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            //pdfView.fromStream(inputStream).load();
        }
    }

}