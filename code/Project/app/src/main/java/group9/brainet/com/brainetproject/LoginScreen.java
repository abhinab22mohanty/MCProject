package group9.brainet.com.brainetproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginScreen extends Activity implements AdapterView.OnItemSelectedListener {

    private List<String> fileList = new ArrayList<String>();
    String choice,classChoice = "Naive Bayes";
    String dbFilePath = "/sdcard/Android/data/";
    EditText user_name;
    String fog_serverUrl = "http://192.168.43.184:8085/CSEMCServer/FogServer";
   String cloud_serverUrl = "https://96219db2.ngrok.io/MCCloudServer/CloudServer";
    File root,selected_File;
    boolean check_fog = false,check_cloud = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        user_name = (EditText)findViewById(R.id.user_name);
        Spinner serverSpinner = (Spinner) findViewById(R.id.server_spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(LoginScreen.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.servers));

        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        serverSpinner.setAdapter(myAdapter);


        serverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choice = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner classSpinner = (Spinner) findViewById(R.id.class_spinner);
        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(LoginScreen.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.classifiers));

        newAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        classSpinner.setAdapter(newAdapter);


        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                classChoice = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        root = new File(dbFilePath+getPackageName());
        ListDir(root);

        final Button btn_Login = (Button)findViewById(R.id.btnLogin);
//        Button btn_Get = (Button)findViewById(R.id.btnGet);
        getWindow().setFormat(PixelFormat.UNKNOWN);
//displays a video file
        final VideoView mVideoView2 = (VideoView)findViewById(R.id.videoView);
// VideoView mVideoView = new VideoView(this);
        String uriPath = "android.resource://"+getPackageName()+"/" + R.raw.meditate;
        Uri uri2 = Uri.parse(uriPath);
        mVideoView2.setVideoURI(uri2);
        mVideoView2.requestFocus();
        btn_Login.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVideoView2.start();
            }
        });



        mVideoView2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                String server_url;
                System.out.println(choice);
                if(choice.equalsIgnoreCase("Cloud"))
                    server_url = cloud_serverUrl;
                else
                    server_url = fog_serverUrl;
                new LoginAsyncTask (LoginScreen.this).execute(user_name.getText().toString(),server_url,classChoice);


            }
        });

    }



    private void ListDir(File root) {

        File[] files = root.listFiles();
        fileList.clear();

        for(File file:files){
            fileList.add(file.getName());

        }
        Spinner spinner = (Spinner) findViewById(R.id.file_spinner);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> file_list = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,fileList);
        file_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(file_list);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        File[] files = root.listFiles();

        for(File file:files){
            if(file.getName().toString().equals(item))
            {
                selected_File =  file;
            }

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class LoginAsyncTask extends AsyncTask<String, String, String> {

        public ProgressDialog dialog = new ProgressDialog(LoginScreen.this);;
        private boolean errorLog = true;
        int serverResponse = 0;
        long startTimer, endTimer;
        public LoginAsyncTask(LoginScreen activity) {

        }

        @Override
        protected void onPreExecute() {
            startTimer = System.currentTimeMillis();
            dialog.setTitle("Login Loader");
            dialog.setMessage("Authenticating......");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // do background work here
            String url = args[1];
            String file_name = args[0];
            String classifier = args[2];

            int id = 1;

            if(classifier.equalsIgnoreCase("Random Forest"))
                id = 4;
            else if(classifier.equalsIgnoreCase("KNN"))
                id = 2;
            else if(classifier.equalsIgnoreCase("SVM"))
                id = 3;
            else
                id = 1;

            //Get File





            File edf = selected_File;
//            System.out.println(edf.getPath());
            String content_type = "text/csv";



            OkHttpClient client = new OkHttpClient();
            RequestBody file_body = RequestBody.create(MediaType.parse(content_type),edf);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id",Integer.toString(id))
                    .addFormDataPart("type",content_type)
                    .addFormDataPart("uploaded_file",file_name,file_body)
                    .build();


            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                System.out.println(request);
                System.out.println(response);

                if(response.code() == 200){
                    errorLog = false;
//                    throw new IOException("Error : "+response );

                }
                else {
                    serverResponse = response.code();
                    errorLog = true;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

//            System.out.println(dbFile.getPath());
//            System.out.println(content_type);

            return null;
        }

        @Override
        protected void onPostExecute(String args) {
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            endTimer = System.currentTimeMillis();
            long timer = endTimer - startTimer;
            if(!errorLog){
                Toast.makeText(getApplicationContext(),"Authenticated in "+Long.toString(timer)+" ms",Toast.LENGTH_SHORT).show();
                Intent passInfoIntent = new Intent(LoginScreen.this,EnterScreen.class);
                startActivity(passInfoIntent);


            }else if(errorLog && serverResponse == 0){

                Toast.makeText(getApplicationContext(),"Server took too long to respond",Toast.LENGTH_SHORT).show();
            }
            else if(errorLog && serverResponse == 404){
                Toast.makeText(getApplicationContext(),"Error!! Not authenticated",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(),"Oops!!Looks like the request failed",Toast.LENGTH_SHORT).show();



        }
    }


//    public class PrefAsyncTask extends AsyncTask<String, String, String> {
//
//        public ProgressDialog dialog = new ProgressDialog(LoginScreen.this);;
//        private boolean errorLog2 = true;
//        int serverResponse = 0;
//        long startTimer, endTimer;
//        public PrefAsyncTask(LoginScreen activity) {
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            dialog.setTitle("Please Wait");
//            dialog.setMessage("Relaying you choice to server......");
//            dialog.show();
//            startTimer = System.currentTimeMillis();
//        }
//
//        @Override
//        protected String doInBackground(String... args) {
//            // do background work here
//            int id = 1;
//            String classifier = args[0];
//            String url = args[1];
//            String name = args[2];
//
//            if(classifier.equalsIgnoreCase("Random Forest"))
//                id = 4;
//            else if(classifier.equalsIgnoreCase("KNN"))
//                id = 2;
//            else if(classifier.equalsIgnoreCase("SVM"))
//                id = 3;
//            else
//                id = 1;
//
//
//            System.out.println(id);
//            OkHttpClient client = new OkHttpClient();
//
//            url = HttpUrl.parse(url).newBuilder()
//                        .addQueryParameter("id",Integer.toString(id))
//                        .addQueryParameter("name",name)
//                        .build().toString();
//
//            Request request = new Request.Builder()
//                                    .url(url)
//                                    .build();
//
//            Response response = null;
//            try {
//                response = client.newCall(request).execute();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//
//            System.out.println(request);
//            System.out.println(response);
//
//            if(response.code() == 200){
//                errorLog2 = false;
////                    throw new IOException("Error : "+response );
//
//            }
//            else {
//                serverResponse = response.code();
//                errorLog2 = true;
//            }
//
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String args) {
//            // do UI work here
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//                endTimer = System.currentTimeMillis();
//            }
//            long timeTaken = endTimer - startTimer;
//            if(!errorLog2){
//                Toast.makeText(getApplicationContext(),timeTaken+" ms",Toast.LENGTH_SHORT).show();
//
//            }else if(errorLog2 && serverResponse == 0){
//
//                Toast.makeText(getApplicationContext(),"Server took too long to respond",Toast.LENGTH_SHORT).show();
//            }
//            else if(errorLog2 && serverResponse == 404){
//                Toast.makeText(getApplicationContext(),"Error!! Bad Request",Toast.LENGTH_SHORT).show();
//            }
//            else
//                Toast.makeText(getApplicationContext(),"Oops!!Looks like the request failed",Toast.LENGTH_SHORT).show();
//
//
//
//        }
//    }


}
