package com.vosto.orders.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vosto.R;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 2013/05/04
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderListFragment extends Fragment{

    private OnItemSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orderlist_fragment,
                container, false);
//        Button button = (Button) view.findViewById(R.id.button1);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateDetail();
//            }
//        });
        return view;
    }

    public interface OnItemSelectedListener {
        public void onRssItemSelected(String link);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet OrderListFragment.OnItemSelectedListener");
        }
    }


    // May also be triggered from the Activity
    public void updateDetail() {
        // Create fake data
        String newTime = String.valueOf(System.currentTimeMillis());
        // Send data to Activity
        listener.onRssItemSelected(newTime);
    }
}
