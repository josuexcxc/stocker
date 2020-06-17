package com.example.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.Utilidades.Utilidades;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class SlideMenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public Typeface font1;
    public TextView textView;
    CircleImageView imageView_profile;
    RequestQueue requestQueue;
    JsonRequest jsonRequest;
    Context context;
    static String id_usuario;
    final private int REQUEST_CODE_ASK_PERMISSION=111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        setContentView(R.layout.activity_slide_menu);
        context=this;
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //enlace a los widgets de foto y nombre del usuario
        View main =navigationView.getHeaderView(0);
        textView=main.findViewById(R.id.tv_name_user);
        imageView_profile=main.findViewById(R.id.img_profile);
        requestPermission();

        // recogiendo la data enviada desde el login
        Bundle data = this.getIntent().getExtras();
        String name = data.getString("nombre");
        String photo = data.getString("path_img");
        id_usuario = data.getString("clave_usuario");


        //consumir el web service para obtener la foto
        String url = Utilidades.ip_server+"/stocker_web_services/"+photo.toString();
        getImageWebService(url);

        textView.setText(name);
        String fuente1= "fonts/malvie.otf";
        this.font1 = Typeface.createFromAsset(getAssets(),fuente1);
        textView.setTypeface(font1);
        textView.setTextSize(20);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_mp)
                .setDrawerLayout(drawer)
                .build();
        final ActionBar actionBar = getSupportActionBar();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int id=destination.getId();
                switch (id){
                    case R.id.nav_share:
                        actionBar.setTitle("");
                        //inicio del loadDialog
                        final LoadDialog loadDialog = new LoadDialog(context,"Cerrando Sesión...");
                        loadDialog.startLoad();
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadDialog.stopLoad();
                                Utilidades.token_id="";
                                Intent i = new Intent(getApplicationContext(),login_activity.class);
                                startActivity(i);
                            }
                        },1000);
                        //fin del loadDialog

                        break;
                    case R.id.nav_mp:
                        getSupportActionBar().setTitle("Menú Principal");
                        break;
                }
            }
        });


    }

    private void requestPermission() {
        int permisoStorage = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permisoCamara = ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA);
        if (permisoCamara != PackageManager.PERMISSION_GRANTED || permisoStorage != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},REQUEST_CODE_ASK_PERMISSION);
            }
        }
    }

    public void titleActionBar(String sec){
        getSupportActionBar().setTitle(sec);
    }

    private void getImageWebService(String url) {
        url = url.replace(" ","%20");
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView_profile.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(imageRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.slide_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

    }
}
