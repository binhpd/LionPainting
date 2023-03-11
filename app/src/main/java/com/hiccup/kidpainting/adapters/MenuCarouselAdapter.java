package com.hiccup.kidpainting.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by binh on 10/5/2015.
 */
//public final class MenuCarouselAdapter extends CarouselAdapter<PhotoModel> {
//    public static final class PhotoItem extends CarouselItem<PhotoModel>
//    {
//
//        private ImageView image;
//
//        private TextView name;
//
//        private Context context;
//
//        public PhotoItem(Context context)
//        {
//            super(context, R.layout.item_menu);
//            this.context = context;
//        }
//
//        @Override
//        public void extractView(View view)
//        {
//            image = (ImageView) view.findViewById(R.id.image);
////            name = (TextView) view.findViewById(R.id.name);
//        }
//
//        @Override
//        public void update(PhotoModel photoModel)
//        {
//            image.setImageResource(getResources().getIdentifier(photoModel.image, "drawable", context.getPackageName()));
////            name.setText(photoModel.name);
//        }
//
//    }
//
//    public MenuCarouselAdapter(Context context, List<PhotoModel> photoModels)
//    {
//        super(context, photoModels);
//    }
//
//    @Override
//    public CarouselItem<PhotoModel> getCarouselItem(Context context)
//    {
//        return new PhotoItem(context);
//    }
//
//}
