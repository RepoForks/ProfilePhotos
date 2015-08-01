package com.mohamadamin.profilephotos.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mohamadamin.profilephotos.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import com.mohamadamin.profilephotos.jazzyviewpager.JazzyViewPager;
import com.mohamadamin.profilephotos.jazzyviewpager.OutlineContainer;
import uk.co.senab.photoview.PhotoView;

public class ImagePagerAdapter extends PagerAdapter {

    File[] files;
    Context context;
    JazzyViewPager jazzyViewPager;

    public ImagePagerAdapter(Context context, File[] files, JazzyViewPager jazzyViewPager) {
        super();
        this.files = files;
        this.context = context;
        this.jazzyViewPager = jazzyViewPager;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.pager_item, null);
        PhotoView imageView = (PhotoView) view.findViewById(R.id.pager_item_image);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.home_popup, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_image: {
                                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                                intent.setDataAndType(Uri.fromFile(files[position]), "image/*");
                                intent.putExtra("jpg", "image/*");
                                context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.choose_as)));
                                return true;
                            }
                            case R.id.action_share: {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("image/jpeg");
                                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(files[position].getAbsolutePath()));
                                context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_with)));
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

        Picasso.with(context)
                .load(files[position])
                .placeholder(R.drawable.warning_white)
                .error(R.drawable.warning_white)
                .into(imageView);

        jazzyViewPager.setObjectForPosition(view, position);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView(jazzyViewPager.findViewFromObject(position));
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == object;
        } else {
            return view == object;
        }
    }

}
