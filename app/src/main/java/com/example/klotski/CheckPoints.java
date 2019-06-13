package com.example.klotski;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CheckPoints extends AppCompatActivity {
    public void backStart(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    private class Member{
        private int id;
        private int image_res;
        private String name;

        public Member(int i, int resourceId, String name) {
            this.id = i;
            this.image_res = resourceId;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public int getImage_res() {
            return image_res;
        }

        public String getName() {
            return name;
        }
    }
    public static final String EXTRA_INT = "Level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_points);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.points_recycler);
        GridLayoutManager layoutManager=new GridLayoutManager(this,4);

        recyclerView.setLayoutManager(layoutManager);
        List<Member> pointList = new ArrayList<>();
        TypedArray images =  getResources().obtainTypedArray(R.array.image_points);
        String[] names = getResources().getStringArray(R.array.text_points);
        for (int i = 0; i < images.length(); i++)
            pointList.add(new Member(i, images.getResourceId(i, 0), names[i]));
        recyclerView.setAdapter(new MemberAdapter(this, pointList));
        ((ImageView) findViewById(R.id.title_image)).setBackgroundColor(Color.TRANSPARENT);
    }
    private class MemberAdapter extends
            RecyclerView.Adapter<MemberAdapter.MyViewHolder> {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<Member> pointsList;

        private MemberAdapter(Context context, List<Member> pointsList) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.pointsList = pointsList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            View itemView;
            TextView mTextView;
            ImageView mImageView;

            public MyViewHolder(View myView) {
                super(myView);
                this.itemView = myView;
                mTextView = (TextView) itemView.findViewById(R.id.item_name);
                mImageView = (ImageView) itemView.findViewById(R.id.item_image);
            }
        }
        @NonNull
        @Override
        public MemberAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = layoutInflater.inflate(
                    R.layout.points_item, viewGroup, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MemberAdapter.MyViewHolder myViewHolder, final int i) {
            final Member member = pointsList.get(i);
            myViewHolder.mTextView.setText(member.getName());
            myViewHolder.mImageView.setImageResource(member.getImage_res());

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GameActivity.class);
                    intent.putExtra(EXTRA_INT, i);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pointsList.size();
        }
    }
}
