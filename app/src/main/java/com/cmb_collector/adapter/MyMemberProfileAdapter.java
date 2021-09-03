package com.cmb_collector.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cmb_collector.R;
import com.cmb_collector.activity.ProfileDetailsActivity;
import com.cmb_collector.model.WCMemberProfileClass;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyMemberProfileAdapter extends RecyclerView.Adapter<MyMemberProfileAdapter.MyViewHolder> {

    private Context mContext;
    private List<WCMemberProfileClass> memberProfileClassList = new ArrayList<>();
    byte[] pics;
    Bitmap memPic;

    public MyMemberProfileAdapter(Context mContext, List<WCMemberProfileClass> memberProfileClassList) {
        this.mContext = mContext;
        this.memberProfileClassList = memberProfileClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_profile, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.item_circleImage.setImageResource(memberProfileClassList.get(position).getImages());
        holder.item_tv_no.setText(memberProfileClassList.get(position).getMemberNo());
        holder.item_tv_name.setText(memberProfileClassList.get(position).getName());
        holder.item_tv_phone.setText(memberProfileClassList.get(position).getPhone());
        holder.item_tv_address.setText(memberProfileClassList.get(position).getAddress());
        holder.item_tv_email.setText(memberProfileClassList.get(position).getEmail());

        holder.item_circleImage.setVisibility(View.GONE);
        holder.item_circleImage.setImageBitmap(LoadPic(memberProfileClassList.get(position).getProfPic()));

        Glide.with(mContext)
                .load(LoadPic(memberProfileClassList.get(position).getProfPic()))
                .apply(new RequestOptions()
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.own_profile))
                .into(holder.item_circleImage);

        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileDetailsActivity.class);
                intent.putExtra("getMemberNo", memberProfileClassList.get(position).getMemberNo());
                intent.putExtra("getName", memberProfileClassList.get(position).getName());
                intent.putExtra("getPhone", memberProfileClassList.get(position).getPhone());
                intent.putExtra("getAddress", memberProfileClassList.get(position).getAddress());
                intent.putExtra("getEmail", memberProfileClassList.get(position).getEmail());
                mContext.startActivity(intent);

            /*    //intent.putExtra("getProfPic", memberProfileClassList.get(position).getProfPic());

                //Pair item_circleImage = Pair.create(holder.item_circleImage, ViewCompat.getTransitionName(holder.item_circleImage));
                Pair item_tv_no = Pair.create(holder.item_tv_no, ViewCompat.getTransitionName(holder.item_tv_no));
                Pair item_tv_name = Pair.create(holder.item_tv_name, ViewCompat.getTransitionName(holder.item_tv_name));
                Pair item_tv_phone = Pair.create(holder.item_tv_phone, ViewCompat.getTransitionName(holder.item_tv_phone));
                Pair item_tv_address = Pair.create(holder.item_tv_address, ViewCompat.getTransitionName(holder.item_tv_address));
                Pair item_tv_email = Pair.create(holder.item_tv_email, ViewCompat.getTransitionName(holder.item_tv_email));
                Pair item_tv_no_f = Pair.create(holder.item_tv_no_f, ViewCompat.getTransitionName(holder.item_tv_no_f));
                Pair item_tv_name_f = Pair.create(holder.item_tv_name_f, ViewCompat.getTransitionName(holder.item_tv_name_f));
                Pair item_tv_phone_f = Pair.create(holder.item_tv_phone_f, ViewCompat.getTransitionName(holder.item_tv_phone_f));
                Pair item_tv_address_f = Pair.create(holder.item_tv_address_f, ViewCompat.getTransitionName(holder.item_tv_address_f));
                Pair item_tv_email_f = Pair.create(holder.item_tv_email_f, ViewCompat.getTransitionName(holder.item_tv_email_f));

                ActivityOptions transitionActivityOptions = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, item_circleImage, item_tv_no, item_tv_name, item_tv_phone, item_tv_address, item_tv_email, item_tv_no_f, item_tv_name_f, item_tv_phone_f, item_tv_address_f, item_tv_email_f);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mContext.startActivity(intent, transitionActivityOptions.toBundle());
                }*/

            }
        });

    }

    @Override
    public int getItemCount() {
        return memberProfileClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rl_item;
        private CircleImageView item_circleImage;
        private TextView item_tv_no;
        private TextView item_tv_name;
        private TextView item_tv_phone;
        private TextView item_tv_address;
        private TextView item_tv_email;

        private TextView item_tv_no_f;
        private TextView item_tv_name_f;
        private TextView item_tv_phone_f;
        private TextView item_tv_address_f;
        private TextView item_tv_email_f;
        private LinearLayout ln_all;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rl_item = itemView.findViewById(R.id.rl_item);
            item_circleImage = itemView.findViewById(R.id.item_circleImage);
            item_tv_no = itemView.findViewById(R.id.item_tv_no);
            item_tv_name = itemView.findViewById(R.id.item_tv_name);
            item_tv_phone = itemView.findViewById(R.id.item_tv_phone);
            item_tv_address = itemView.findViewById(R.id.item_tv_address);
            item_tv_email = itemView.findViewById(R.id.item_tv_email);
            item_tv_no_f = itemView.findViewById(R.id.item_tv_no_f);
            item_tv_name_f = itemView.findViewById(R.id.item_tv_name_f);
            item_tv_phone_f = itemView.findViewById(R.id.item_tv_phone_f);
            item_tv_address_f = itemView.findViewById(R.id.item_tv_address_f);
            item_tv_email_f = itemView.findViewById(R.id.item_tv_email_f);
            ln_all = itemView.findViewById(R.id.ln_all);

        }
    }

    private Bitmap LoadPic(String picPath) {
        pics = Base64.decode(picPath,Base64.DEFAULT);
        memPic = BitmapFactory.decodeByteArray(pics, 0, pics.length);
        return memPic;
    }

    public void Onclick() {

    }




}
