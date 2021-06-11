package com.example.rigbys.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rigbys.Admin.AdminMaintainProductsActivity;
import com.example.rigbys.Model.ProductItemModel;
import com.example.rigbys.Prevalent.Prevalent;
import com.example.rigbys.R;
import com.example.rigbys.Viewholder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

   // private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        //for admin access to maintain products//
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            type = getIntent().getExtras().get("admin").toString();
        }
        //for admin access to maintain products//

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("admin")){
                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
                }
            }
        });

  /*      DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController); */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userProfileTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);

        if (!type.equals("admin")){
            userProfileTextView.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getProfile_image()).placeholder(R.mipmap.profile).into(profileImageView);
        }

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.recyclerView_menu);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ProductItemModel> options =
                new FirebaseRecyclerOptions.Builder<ProductItemModel>().setQuery(productRef
                        .orderByChild("status").equalTo("Approved"), ProductItemModel.class).build();

        FirebaseRecyclerAdapter<ProductItemModel, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<ProductItemModel, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductItemModel model) {
                       holder.productName.setText(model.getName());
                       holder.productDescription.setVisibility(View.GONE);
                       holder.productPrice.setText("Price: "+model.getPrice()+" Tk");
                       Picasso.get().load(model.getLink()).into(holder.productImage);

                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               if (type.equals("admin")){
                                   Intent intent = new Intent(getApplicationContext(), AdminMaintainProductsActivity.class);
                                   intent.putExtra("id", model.getId());
                                   startActivity(intent);

                               }else{
                                   Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                                   intent.putExtra("id", model.getId());
                                   startActivity(intent);
                               }
                           }
                       });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

/*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.nav_cart){
            if(!type.equals("admin")){
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }

        }else if (id==R.id.nav_search){
            if(!type.equals("admin")){
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
            if (type.equals("admin")){
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("admin", "admin");
                startActivity(intent);
            }

        }else if (id==R.id.nav_categories){
            if(!type.equals("admin")){
                startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
            }
            if (type.equals("admin")){
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("admin", "admin");
                startActivity(intent);
            }

        }else if (id==R.id.nav_settings){
            if(!type.equals("admin")){
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }

        }else if (id==R.id.nav_logout) {
            if(!type.equals("admin")){
                Paper.book().destroy();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        }
        return false;
    }
}