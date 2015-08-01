package com.mohamadamin.profilephotos.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mohamadamin.profilephotos.fragments.HomeFragment;
import com.mohamadamin.profilephotos.utils.BusinessUtils;
import com.mohamadamin.profilephotos.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

import com.mohamadamin.profilephotos.activities.ImageActivity;
import com.mohamadamin.profilephotos.modules.CustomImageView;
import com.mohamadamin.profilephotos.R;

public class PictureAdapter extends BaseAdapter {

    File[] list;
    Activity activity;
    int category;

    public PictureAdapter(Activity activity, File[] list, int category) {
        super();
        this.activity = activity;
        this.list = list;
        this.category = category;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        CustomImageView imageView = (CustomImageView) convertView;
        if (imageView == null) {
            imageView = new CustomImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    PopupMenu popupMenu = new PopupMenu(activity, view);
                    popupMenu.getMenuInflater().inflate(R.menu.home_popup, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_image: {
                                    if (PreferenceUtils.isPremium(activity)) {
                                        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                                        intent.setDataAndType(Uri.fromFile(list[position]), "image/*");
                                        intent.putExtra("jpg", "image/*");
                                        activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R.string.choose_as)));
                                    } else BusinessUtils.showUpgradeDialog(activity);
                                    return true;
                                }
                                case R.id.action_share: {
                                    if (PreferenceUtils.isPremium(activity)) {
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("image/jpeg");
                                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(list[position].getAbsolutePath()));
                                        activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R.string.share_with)));
                                    } else BusinessUtils.showUpgradeDialog(activity);
                                    return true;
                                }
                                default:
                                    return false;
                            }
                        }

                    });

                    popupMenu.show();

                    return true;

                }
            });
        }

        Picasso.with(activity)
                .load(list[position])
                .fit()
                .placeholder(R.drawable.warning_white)
                .error(R.drawable.warning_white)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceUtils.isPremium(activity)) {
                    Intent intent = new Intent(activity, ImageActivity.class);
                    intent.putExtra(HomeFragment.CATEGORY, category);
                    intent.putExtra(ImageActivity.CURRENT_POSITION, position);
                    activity.startActivity(intent);
                } else BusinessUtils.showUpgradeDialog(activity);
            }
        });

        return imageView;

    }

}
