package com.mohamadamin.profilephotos.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.mohamadamin.profilephotos.utils.FileUtils;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

import java.io.File;

import com.mohamadamin.profilephotos.R;
import com.mohamadamin.profilephotos.adapters.PictureAdapter;

public class HomeFragment extends Fragment {

    View mainLayout;
    GridView gridView;
    LinearLayout warningLayout;
    ProgressDialog progressDialog;

    File[] files;
    int category;
    PictureAdapter adatper;

    public static final int VIBER=0, LINE=1, TELEGRAM=2, WHATSAPP=3;
    public static final String CATEGORY =  "Category";

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        category = getArguments().getInt(CATEGORY);
        mainLayout = inflater.inflate(R.layout.fragment_home, container, false);
        if (isAdded()) showContent();
        return mainLayout;
    }

    public void showContent() {
        initializeViews();
        inflateLists();
    }

    public void initializeViews() {
        gridView = (GridView) mainLayout.findViewById(R.id.home_grid);
        warningLayout = (LinearLayout) mainLayout.findViewById(R.id.home_warning_container);
    }

    public void inflateLists() {
        new FileLoaderTask().execute();
    }

    private class FileLoaderTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), null, "لطفا صبر کنید");
            progressDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            switch (category) {
                case VIBER : files = FileUtils.getViberFiles(); break;
                case LINE : files = FileUtils.getLineFiles(); break;
                case TELEGRAM : files = FileUtils.getTelegramFiles(); break;
                case WHATSAPP : files = FileUtils.getWhatsAppFiles(); break;
                default: files = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (getActivity().isFinishing()) return;
            dismissProgressDialog();

            if(files!=null && files.length>0) {
                adatper = new PictureAdapter(getActivity(), files, category);
                AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adatper);
                alphaInAnimationAdapter.setAbsListView(gridView);
                gridView.setAdapter(alphaInAnimationAdapter);
            } else warningLayout.setVisibility(View.VISIBLE);

        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

}
