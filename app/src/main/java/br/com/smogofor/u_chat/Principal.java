package br.com.smogofor.u_chat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    private String[] itensMenuEsquerda;
    private ArrayAdapter<String> adapterMenuEsquerda;
    private ListView menuEsquerda;
    private AdapterView.OnItemClickListener listener;
    private FirebaseApp app;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private DatabaseReference databaseRef;

    private View loginBtn;
    private View logoutBtn;

    private TextView usernameTxt;

    private String username;

    private void setUsername(String username) {
        if (username == null) {
            username = "Anônimo";
        }
        boolean isLoggedIn = !username.equals("Anônimo");
        auth.signInAnonymously();
        this.username = username;
        this.logoutBtn = findViewById(R.id.logoutBtn);
        this.loginBtn = findViewById(R.id.loginBtn);
        MontarHeader();
//        this.logoutBtn.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
//        this.loginBtn .setVisibility(isLoggedIn ? View.GONE    : View.VISIBLE);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the Firebase app and all primitives we'll use
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // When the user has entered credentials in the login dialog
        LoginDialog.onCredentials(new OnSuccessListener<LoginDialog.EmailPasswordResult>() {
            public void onSuccess(LoginDialog.EmailPasswordResult result) {
                // Sign the user in with the email address and password they entered
                auth.createUserWithEmailAndPassword(result.email, result.password);
                //auth.signInWithEmailAndPassword(result.email, result.password);
            }
        });

        // When the user signs in or out, update the username we keep for them
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // User signed in, set their email address as the user name
                    setUsername(firebaseAuth.getCurrentUser().getEmail());
                }
                else {
                    // User signed out, set a default username
                    setUsername("Anônimo");

                }

            }
        });

        MontarMenuEsquerda();
        setUsername("Android");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.loginBtn) {
            LoginDialog.showLoginPrompt(Principal.this, app);
            return true;
        }
        if (id == R.id.logoutBtn) {
            auth.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //salas públicas permitem aninimato
    //Menu do topo possui configurações app, perfil e logout/login

    //Menu de seleção de salas
    public void MontarMenuDireita(){
        //Realizar busca e montar lista de salas com seus respectivos links


    }

    //Ao selecionar sala possuíra um padrão
    public void MontarMenuEsquerda(){
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu_esquerda);



        if(!auth.getCurrentUser().isAnonymous()){
            //String token = auth.getCurrentUser().getIdToken(true).getResult().getToken();
            //databaseRef = database.getReference();
            //databaseRef = databaseRef.child("Usuarios");
            //databaseRef = databaseRef.child(token);
        }
        databaseRef = database.getReference("Salas");

        itensMenuEsquerda = new String[]{"Administrar","Sala 1", "Sala 2", "Sala 3"};
        menuEsquerda = (ListView) findViewById(R.id.lst_menu_items_esquerda);
        adapterMenuEsquerda = new ArrayAdapter(this,android.R.layout.simple_list_item_1,itensMenuEsquerda);
        menuEsquerda.setAdapter(adapterMenuEsquerda);

        listener = new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!adapterMenuEsquerda.getItem(i).equalsIgnoreCase("Administrar")) {
                    FragmentManager fm = getSupportFragmentManager();
                    TelaPrincipal fragment = new TelaPrincipal();
                    Bundle arguments = new Bundle();
                    arguments.putString("Reference", adapterMenuEsquerda.getItem(i));
                    fragment.setArguments(arguments);

                    fm.beginTransaction()
                            .replace(R.id.areaChat, fragment, "Chat")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START))
                        drawer.closeDrawer(GravityCompat.START);
                }
            }
        };

        menuEsquerda.setOnItemClickListener(listener);
    }

    public void MontarHeader(){
        //busca dados do usuario logado
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu_esquerda);
        FirebaseUser user = auth.getCurrentUser();
        if(user != null && !user.isAnonymous()){
            ((ImageView)findViewById(R.id.imv_foto)).setImageURI(user.getPhotoUrl());
            ((TextView)findViewById(R.id.txt_usuario)).setText(user.getDisplayName());
            ((TextView)findViewById(R.id.txt_email)).setText(user.getEmail());
        }else{
            ((TextView)findViewById(R.id.txt_usuario)).setText("Anônimo");
        }
    }
}
