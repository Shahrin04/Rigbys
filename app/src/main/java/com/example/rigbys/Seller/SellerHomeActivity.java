package com.example.rigbys.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.rigbys.Buyer.MainActivity;
import com.example.rigbys.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SellerHomeActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNav
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.navigation_home:
                    fragment = new SellerHomeFragment();
                    break;
                case R.id.navigation_add:
                    fragment = new SellerAddFragment();
                    break;
                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent logout_intent = new Intent(getApplicationContext(), MainActivity.class);
                    logout_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logout_intent);
                    finish();
                return true;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.seller_frame_layout, fragment).commit();
            return true;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        ///////IMPORTANT//////////
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(bottomNav);
        ///////IMPORTANT//////////
        getSupportFragmentManager().beginTransaction().replace(R.id.seller_frame_layout, new SellerHomeFragment()).commit();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//       NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

}