package com.harry.pullgo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.harry.pullgo.ui.ChangePersonInfo.ChangePersonInfoFragment;
import com.harry.pullgo.ui.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class StudentMainActivity extends AppCompatActivity
                    implements NavigationView.OnNavigationItemSelectedListener,FragmentCallback{

    DrawerLayout drawerLayout;
    NavigationView studentNavigation;

    HomeFragment studentHomeFragment;
    ChangePersonInfoFragment studentChangeInfoFragment;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        toolbar = findViewById(R.id.studentToolbar);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        studentNavigation=findViewById(R.id.navigationViewStudent);
        studentNavigation.setNavigationItemSelectedListener(this);

        studentHomeFragment=new HomeFragment();
        studentChangeInfoFragment=new ChangePersonInfoFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.studentMainFragment,studentHomeFragment).commit();
        getSupportActionBar().setTitle("Home");
    }

    public void logoutButtonClicked(View v){
        finish();
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_additional_setting, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_student_home:
                onFragmentSelected(0,null);
                break;
            case R.id.nav_student_change_info:
                onFragmentSelected(1,null);
                break;
        }
        drawerLayout.closeDrawer(studentNavigation);

        return true;
    }

    @Override
    public void onFragmentSelected(int position, Bundle bundle) {
        Fragment curFragment=null;

        if(position==0){
            curFragment=studentHomeFragment;
            toolbar.setTitle("Home");
        }else if(position==1){
            curFragment=studentChangeInfoFragment;
            toolbar.setTitle("회원정보 변경");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.studentMainFragment,curFragment).commit();
    }
}