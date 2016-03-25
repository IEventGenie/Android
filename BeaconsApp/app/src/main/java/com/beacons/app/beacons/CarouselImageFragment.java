package com.beacons.app.beacons;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.beacons.app.beaconsapp.R;
import com.beacons.app.utilities.CircleTransform;
import com.squareup.picasso.Picasso;


public class CarouselImageFragment extends Fragment {

    ImageView image;


    public static CarouselImageFragment newInstance(String url) {
        CarouselImageFragment fragment = new CarouselImageFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    public CarouselImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_carousel_dialog, container, false);

        image = (ImageView) rootView.findViewById(R.id.carousel_image);

        String url = getArguments().getString("url");

        /*ImageLoader imgLoader = MyVolley.getInstance(getActivity().getApplicationContext())
                .getImageLoader();

        image.setImageUrl(url, imgLoader);*/

        Picasso.with(getActivity().getApplicationContext())
                .load(url)
                .into(image);

        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}