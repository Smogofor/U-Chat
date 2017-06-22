package br.com.smogofor.u_chat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ListView;

import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    private String[] itensMenuEsquerda;
    private ArrayAdapter<String> adapterMenuEsquerda;
    private ListView menuEsquerda;
    private AdapterView.OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        MontarMenuEsquerda();
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
        if (id == R.id.action_settings) {
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
//        menuEsquerda.requestFocus();
//        menuEsquerda.bringToFront();
    }
}
