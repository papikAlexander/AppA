package com.alex.appa.activities;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.alex.appa.R;
import com.alex.appa.adapters.MyRecyclerAdapter;
import com.alex.appa.adapters.SectionsPagerAdapter;


public class MainActivity extends AppCompatActivity {

    final Uri LINK_URI =  Uri.parse("content://com.alex.appa.providers.dbA/links");

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewHistory);

        switch (id){

            case R.id.action_setting_sort_status:


                adapter = new MyRecyclerAdapter(this,
                        getContentResolver().query(LINK_URI, null, null, null, "status ADS"));
                recyclerView.setAdapter(adapter);
                return true;

            case R.id.action_settings_sort_data:

                adapter = new MyRecyclerAdapter(this,
                        getContentResolver().query(LINK_URI, null, null, null, "data ADS"));
                recyclerView.setAdapter(adapter);

                return true;

            case R.id.action_setting_delete_history:

                getContentResolver().delete(LINK_URI, null, null);
                getSupportLoaderManager().getLoader(0).forceLoad();

                return true;
            case R.id.action_setting_refresh:
                getSupportLoaderManager().getLoader(0).forceLoad();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
