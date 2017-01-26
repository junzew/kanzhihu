package com.example.junze.kanzhihu.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junze.kanzhihu.NightModeHelper;
import com.example.junze.kanzhihu.R;
import com.example.junze.kanzhihu.fragment.KanZhiHuFragment;
import com.example.junze.kanzhihu.fragment.ThemeFragment;
import com.example.junze.kanzhihu.fragment.TopStoriesFragment;
import com.example.junze.kanzhihu.fragment.ZhiHuRiBaoFragment;
import com.example.junze.kanzhihu.services.LogService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private ViewPager viewPager;
    private Fragment kanZhiHuFragment;
    private Fragment zhiHuRiBaoFragment;
    private Fragment topStoriesFragment;
    private Fragment themeFragment;
    private List<Fragment> fragments;
    private MainFragmentAdapter adapter;
    private TabLayout tabLayout;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private NightModeHelper mNightModeHelper;

    private boolean isNightModeEnabled;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isNightModeEnabled", isNightModeEnabled);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNightModeHelper = new NightModeHelper(this, R.style.AppTheme);
        if(savedInstanceState != null) {
            isNightModeEnabled = savedInstanceState.getBoolean("isNightModeEnabled");
        } else {
            Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            // 22,23,0,1,2,3,4,5
            if ((hour >= 22 && hour <= 24) || (hour >= 0 && hour <= 5)) {
                isNightModeEnabled = true;
                mNightModeHelper.night();
            } else {
                isNightModeEnabled = false;
                mNightModeHelper.notNight();
            }

        }
        setContentView(R.layout.activity_main);

        //getWindow().setBackgroundDrawableResource(R.color.background);

        fragments = new ArrayList<>();
//        kanZhiHuFragment = new KanZhiHuFragment();
//        fragments.add(kanZhiHuFragment);
        topStoriesFragment = new TopStoriesFragment();
        fragments.add(topStoriesFragment);
        zhiHuRiBaoFragment = new ZhiHuRiBaoFragment();
        fragments.add(zhiHuRiBaoFragment);
        themeFragment = new ThemeFragment();
        fragments.add(themeFragment);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tl);
        List<String> titles = new ArrayList<>();
//        titles.add("看知乎");
        titles.add("热门");
        titles.add("知乎日报");
        titles.add("主题列表");
        adapter.setTitles(titles);
        tabLayout.setupWithViewPager(viewPager);

//        Intent serviceIntent = new Intent(this, LogService.class);
//        startService(serviceIntent);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_drawer);

        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                        new String[] { isNightModeEnabled ? "日间模式" : "夜间模式", "搜索用户"});
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(itemsAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch(position) {
                    case 0:
                        TextView t = (TextView) view;
                        if (isNightModeEnabled) {
                            t.setText("日间模式");
                        } else {
                            t.setText("夜间模式");
                        }
                        isNightModeEnabled = !isNightModeEnabled;
                        mNightModeHelper.toggle();
                        break;
                    case 1:
                        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;
                    default:
                        break;
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getTitle().toString());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("个人");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO
                switch(item.getItemId()) {
                    case R.id.action_about:
                        showDialog();
                        break;
                }
                return true;
            }
        });

    }

    private void showDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Developed by: "+getString(R.string.developer) + "\n" + getString(R.string.website))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //处理其他菜单点击事件

        return super.onOptionsItemSelected(item);
    }



    private class MainFragmentAdapter extends FragmentPagerAdapter {
        private List<String> titles;

        public void setTitles(List<String> titles) {
            this.titles = titles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
