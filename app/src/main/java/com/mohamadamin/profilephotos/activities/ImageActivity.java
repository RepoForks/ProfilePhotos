package com.mohamadamin.profilephotos.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dynamixsoftware.ErrorAgent;
import com.mohamadamin.profilephotos.R;

import java.io.File;

import com.mohamadamin.profilephotos.adapters.PictureAdapter;
import com.mohamadamin.profilephotos.fragments.HomeFragment;
import com.mohamadamin.profilephotos.jazzyviewpager.JazzyViewPager;
import com.mohamadamin.profilephotos.adapters.ImagePagerAdapter;
import com.mohamadamin.profilephotos.utils.FileUtils;
import com.mohamadamin.profilephotos.utils.PreferenceUtils;
import com.mohamadamin.profilephotos.utils.WebUtils;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

import ir.adad.Adad;

public class ImageActivity extends AppCompatActivity {

    Toolbar toolbar;
    JazzyViewPager jazzyViewPager;
    ActionBar actionBar;
    ProgressDialog progressDialog;

    File[] files;
    int category, currentPosition;

    public final static String CURRENT_POSITION="Current Position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!PreferenceUtils.shouldShowAds(this)) Adad.setDisabled(true);
        setContentView(R.layout.activity_image);
        ErrorAgent.register(this, 142L);

        initializeViews();
        organizeViews();
        setUpToolbar();
        WebUtils.checkWebAppOffers(this);

    }

    public void initializeViews() {
        jazzyViewPager = (JazzyViewPager) findViewById(R.id.image_pager);
        toolbar = (Toolbar) findViewById(R.id.image_toolbar);
    }

    public void organizeViews() {
        category = getIntent().getIntExtra(HomeFragment.CATEGORY, -1);
        currentPosition = getIntent().getIntExtra(CURRENT_POSITION, 0);
        new FileLoaderTask().execute();
    }

    public void setUpViewPager() {

        jazzyViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if (getSupportActionBar()!=null) getSupportActionBar().setTitle(files[position].getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        jazzyViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);
        jazzyViewPager.setAdapter(new ImagePagerAdapter(this, files, jazzyViewPager));
        jazzyViewPager.setCurrentItem(currentPosition);

    }

    public void setUpToolbar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : this.finish(); return true;
            case R.id.action_image : {
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.setDataAndType(Uri.fromFile(files[currentPosition]), "image/*");
                intent.putExtra("jpg", "image/*");
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_as)));
                return true;
            }
            case R.id.action_share : {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(files[currentPosition].getAbsolutePath()));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_with)));
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

    private class FileLoaderTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ImageActivity.this, null, "لطفا صبر کنید");
            progressDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            switch (category) {
                case HomeFragment.VIBER : files = FileUtils.getViberFiles(); break;
                case HomeFragment.LINE : files = FileUtils.getLineFiles(); break;
                case HomeFragment.TELEGRAM : files = FileUtils.getTelegramFiles(); break;
                case HomeFragment.WHATSAPP : files = FileUtils.getWhatsAppFiles(); break;
                default: files = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (ImageActivity.this.isFinishing()) return;
            dismissProgressDialog();
            setUpViewPager();
            actionBar.setTitle(files[currentPosition].getName());
        }
    }

    @Override
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

}
