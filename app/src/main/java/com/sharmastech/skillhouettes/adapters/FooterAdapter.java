package com.sharmastech.skillhouettes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharmastech.skillhouettes.R;
import com.sharmastech.skillhouettes.common.AppSettings;
import com.sharmastech.skillhouettes.imageloader.ImageLoader;
import com.sharmastech.skillhouettes.utils.TraceUtils;
import com.sharmastech.skillhouettes.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by w7u on 1/11/2017.
 */

public class FooterAdapter extends RecyclerView.Adapter<FooterAdapter.ContactViewHolder> {

    private JSONArray array = new JSONArray();

    private Context context = null;

    private View.OnClickListener onClickListener;

    private ImageLoader imageLoader;

    public FooterAdapter(Context context) {
        this.context = context;
        imageLoader = new ImageLoader(context, false);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    @Override
    public void onBindViewHolder(FooterAdapter.ContactViewHolder contactViewHolder, int i) {

        try {

            JSONObject jsonObject = array.getJSONObject(i);

            String image_url = AppSettings.getInstance(context).getPropertyValue("i_path");

            String ad_img=image_url + jsonObject.optString("expectedskillimage");


            imageLoader.DisplayImage(Utils.urlEncode(ad_img), contactViewHolder.iv_icon, 2);



        } catch (Exception e) {
            TraceUtils.logException(e);
        }

    }

    @Override
    public FooterAdapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footer_row_layout, viewGroup,
                false);
        itemView.setOnClickListener(onClickListener);
        return new FooterAdapter.ContactViewHolder(itemView);
    }

    public int getCount() {

        return array.length();
    }

    public long getItemId(int position) {

        return position + 1;
    }

    public void setItems(JSONArray aArray) {

        this.array = aArray;
    }

    public JSONArray getItems() {

        return this.array;
    }

    public void clear() {

        array = null;
        array = new JSONArray();
    }

    public void release() {

        array = null;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {


        protected ImageView iv_icon;


        public ContactViewHolder(View v) {

            super(v);

            iv_icon = (ImageView) v.findViewById(R.id.ft_image);

        }
    }

}