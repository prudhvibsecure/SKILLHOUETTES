package com.sharmastech.skillhouettes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sharmastech.skillhouettes.R;
import com.sharmastech.skillhouettes.Skillhouettes;
import com.sharmastech.skillhouettes.adapters.SkillsAdapter;
import com.sharmastech.skillhouettes.callbacks.IItemHandler;
import com.sharmastech.skillhouettes.controls.CustomSwipeToRefresh;

public class SkillsFragment extends ParentFragment implements IItemHandler {

    private OnFragmentInteractionListener mListener;

    private View layout;

    private ProgressBar pb_content_bar;

    private TextView tv_content_txt;

    private RecyclerView rv_content_list;

    private CustomSwipeToRefresh swipe_refresh_layout;

    private SkillsAdapter adapter;

    private Skillhouettes activity;

    public SkillsFragment() {
        // Required empty public constructor
    }

    public static SkillsFragment newInstance() {
        return new SkillsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_skills, container, false);

        pb_content_bar = (ProgressBar) layout.findViewById(R.id.pb_content_bar);

        tv_content_txt = (TextView) layout.findViewById(R.id.tv_content_txt);

        swipe_refresh_layout = (CustomSwipeToRefresh) layout.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setColorSchemeResources(R.color.red, R.color.red, R.color.red, R.color.red, R.color.red);
        swipe_refresh_layout.setEnabled(false);
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //getData(true);
            }
        });

        rv_content_list = (RecyclerView) layout.findViewById(R.id.rv_content_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        rv_content_list.setLayoutManager(layoutManager);
        rv_content_list.setItemAnimator(new DefaultItemAnimator());

        adapter = new SkillsAdapter(activity);

        rv_content_list.setAdapter(adapter);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
    }

    private void getData() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) context;
        activity = (Skillhouettes)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFinish(Object results, int requestId) {

    }

    @Override
    public void onError(String errorCode, int requestId) {

    }
}
