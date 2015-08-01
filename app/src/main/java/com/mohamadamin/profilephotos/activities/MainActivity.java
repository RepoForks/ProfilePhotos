package com.mohamadamin.profilephotos.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.dynamixsoftware.ErrorAgent;
import com.mohamadamin.profilephotos.R;

import java.util.Locale;

import com.mohamadamin.profilephotos.fragments.HomeFragment;
import com.mohamadamin.profilephotos.utils.BusinessUtils;
import com.mohamadamin.profilephotos.utils.IABUtils;
import com.mohamadamin.profilephotos.utils.LanguageUtils;
import com.mohamadamin.profilephotos.utils.PreferenceUtils;
import com.mohamadamin.profilephotos.utils.SdkUtils;
import com.mohamadamin.profilephotos.utils.WebUtils;

import ir.adad.Adad;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    View slidingPanel;
    ActionBar actionBar;

    TextView viberText, lineText, telegramText, whatsAppText,
            removeAdsText, otherApsText, ratingText, emailText, instagramText;
    TextView[] textViews;

    int[] drawableResources = new int[]{R.drawable.ic_action_viber, R.drawable.ic_action_line,
                                        R.drawable.ic_action_telegram, R.drawable.ic_action_whatsapp};

    Locale defaultLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        IABUtils.initializeIabHelper(this);
        if (SdkUtils.isJellyBeanMR1OrHigher()) defaultLocale = LanguageUtils.setPersianLocale(this);
        if (!PreferenceUtils.shouldShowAds(this)) Adad.setDisabled(true);
        setContentView(R.layout.activity_main);
        ErrorAgent.register(this, 142L);

        initializeViews();
        handleClicks();
        changeSizes();
        organizeToolbar();
        if (savedInstanceState == null) launchHome();
        BusinessUtils.intitUpdateService(this);
        WebUtils.checkWebAppOffers(this);

    }

    public void initializeViews() {
        slidingPanel = findViewById(R.id.main_slider);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        removeAdsText = (TextView) findViewById(R.id.slider_remove_ads);
        otherApsText = (TextView) findViewById(R.id.slider_other_apps);
        ratingText = (TextView) findViewById(R.id.slider_rating);
        emailText = (TextView) findViewById(R.id.slider_email);
        instagramText = (TextView) findViewById(R.id.slider_instagram);
        viberText = (TextView) findViewById(R.id.slider_viber);
        lineText = (TextView) findViewById(R.id.slider_line);
        telegramText = (TextView) findViewById(R.id.slider_telegram);
        whatsAppText = (TextView) findViewById(R.id.slider_whatsapp);
        textViews = new TextView[]{viberText, lineText, telegramText, whatsAppText};
    }

    public void handleClicks() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                switch (view.getId()) {
                    case R.id.slider_viber : {
                        setPressedView(HomeFragment.VIBER);
                        bundle.putInt(HomeFragment.CATEGORY, HomeFragment.VIBER);
                        actionBar.setTitle(getResources().getString(R.string.viber));
                        break;
                    }
                    case R.id.slider_line : {
                        setPressedView(HomeFragment.LINE);
                        bundle.putInt(HomeFragment.CATEGORY, HomeFragment.LINE);
                        actionBar.setTitle(getResources().getString(R.string.line));
                        break;
                    }
                    case R.id.slider_telegram : {
                        setPressedView(HomeFragment.TELEGRAM);
                        bundle.putInt(HomeFragment.CATEGORY, HomeFragment.TELEGRAM);
                        actionBar.setTitle(getResources().getString(R.string.telegram));
                        break;
                    }
                    case R.id.slider_whatsapp : {
                        setPressedView(HomeFragment.WHATSAPP);
                        bundle.putInt(HomeFragment.CATEGORY, HomeFragment.WHATSAPP);
                        actionBar.setTitle(getResources().getString(R.string.whatsapp));
                        break;
                    }
                }
                homeFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
                closeDrawerLayout();
            }
        };
        for (TextView textView : textViews) textView.setOnClickListener(onClickListener);
        View.OnClickListener otherOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.slider_remove_ads : IABUtils.removeAds(MainActivity.this); break;
                    case R.id.slider_other_apps : BusinessUtils.launchOtherApps(MainActivity.this); break;
                    case R.id.slider_rating : BusinessUtils.launchRatingWindow(MainActivity.this); break;
                    case R.id.slider_email : BusinessUtils.sendEmailToDeveloper(MainActivity.this); break;
                    case R.id.slider_instagram : BusinessUtils.launchInstagramPage(MainActivity.this); break;
                }
                closeDrawerLayout();
            }
        };
        if (PreferenceUtils.shouldShowAds(this)) removeAdsText.setOnClickListener(otherOnClickListener);
        else removeAdsText.setVisibility(View.GONE);
        otherApsText.setOnClickListener(otherOnClickListener);
        ratingText.setOnClickListener(otherOnClickListener);
        emailText.setOnClickListener(otherOnClickListener);
        instagramText.setOnClickListener(otherOnClickListener);
    }

    public void setPressedView(int category) {
        for (int i=0; i<drawableResources.length; i++) {
            textViews[i].setBackgroundColor(Color.TRANSPARENT);
            textViews[i].setTextColor(Color.BLACK);
            if (SdkUtils.isJellyBeanMR1OrHigher()) textViews[i].
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableResources[i], 0);
            else textViews[i].
                    setCompoundDrawablesWithIntrinsicBounds(drawableResources[i], 0, 0, 0);
        }
        switch (category) {
            case HomeFragment.VIBER : {
                viberText.setBackgroundColor(Color.parseColor("#DDDDDD"));
                viberText.setTextColor(getResources().getColor(R.color.colorPrimary));
                if (SdkUtils.isJellyBeanMR1OrHigher()) viberText.
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_viber_blue, 0);
                else viberText.
                        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_viber_blue, 0, 0, 0);
                break;
            }
            case HomeFragment.LINE : {
                lineText.setBackgroundColor(Color.parseColor("#DDDDDD"));
                lineText.setTextColor(getResources().getColor(R.color.colorPrimary));
                if (SdkUtils.isJellyBeanMR1OrHigher()) lineText.
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_line_blue, 0);
                else lineText.
                        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_line_blue, 0, 0, 0);
                break;
            }
            case HomeFragment.TELEGRAM : {
                telegramText.setBackgroundColor(Color.parseColor("#DDDDDD"));
                telegramText.setTextColor(getResources().getColor(R.color.colorPrimary));
                if (SdkUtils.isJellyBeanMR1OrHigher()) telegramText.
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_telegram_blue, 0);
                else telegramText.
                        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_telegram_blue, 0, 0, 0);
                break;
            }
            case HomeFragment.WHATSAPP : {
                whatsAppText.setBackgroundColor(Color.parseColor("#DDDDDD"));
                whatsAppText.setTextColor(getResources().getColor(R.color.colorPrimary));
                if (SdkUtils.isJellyBeanMR1OrHigher()) whatsAppText.
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_whatsapp_blue, 0);
                else whatsAppText.
                        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_whatsapp_blue, 0, 0, 0);
                break;
            }
        }
    }

    public void setArrowToggle() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public void launchHome() {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(HomeFragment.CATEGORY, HomeFragment.VIBER);
        setPressedView(HomeFragment.VIBER);
        actionBar.setTitle(getResources().getString(R.string.viber));
        homeFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
    }

    public void organizeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setArrowToggle();
    }

    public void openDrawerLayout() {
        if (drawerLayout != null) drawerLayout.openDrawer(slidingPanel);
    }

    public void closeDrawerLayout() {
        if (drawerLayout != null) drawerLayout.closeDrawer(slidingPanel);
    }

    public void changeSizes() {
        ViewTreeObserver viewTreeObserver = slidingPanel.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                removeLayoutListener(this);
                int width  = slidingPanel.getMeasuredWidth();
                for (TextView textView : textViews) {
                    ViewGroup.LayoutParams params = textView.getLayoutParams();
                    params.width = width;
                    textView.setLayoutParams(params);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void removeLayoutListener(ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (Build.VERSION.SDK_INT >= 16) slidingPanel.getViewTreeObserver().removeOnGlobalLayoutListener(victim);
        else slidingPanel.getViewTreeObserver().removeGlobalOnLayoutListener(victim);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                if (drawerLayout.isDrawerOpen(Gravity.START)) closeDrawerLayout();
                else openDrawerLayout();
                return true;
            }
            case R.id.action_other_apps : {
                BusinessUtils.launchOtherApps(this);
                return true;
            }
            case R.id.action_rating : {
                BusinessUtils.launchRatingWindow(this);
                return true;
            }
            case R.id.action_email : {
                BusinessUtils.sendEmailToDeveloper(this);
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!IABUtils.handleResult(requestCode, resultCode, data)) {
             super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (PreferenceUtils.shouldShowRating(this)) BusinessUtils.showAutoRatingWindow(this);
        else super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (SdkUtils.isJellyBeanMR1OrHigher()) LanguageUtils.setDefaultLocale(this, defaultLocale);
        BusinessUtils.releaseUpdateService(this);
        IABUtils.destroyIabHelper();
    }

}