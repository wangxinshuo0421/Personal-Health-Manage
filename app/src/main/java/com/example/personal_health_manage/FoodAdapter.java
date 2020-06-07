package com.example.personal_health_manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FoodAdapter extends ArrayAdapter<Food> {
    public int resourceId;
    public FoodAdapter(Context context, int textViewResourceId, List<Food> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Food food = getItem(position); //获取当前项的实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView foodImage = (ImageView)view.findViewById(R.id.food_image);
        TextView foodName = (TextView)view.findViewById(R.id.food_name);
        TextView foodInclusion = (TextView)view.findViewById(R.id.food_inclusion);
        TextView foodInclusionContent = (TextView)view.findViewById(R.id.food_inclusion_content);
        TextView foodUnit = (TextView)view.findViewById(R.id.food_inclusion_unit);
        foodImage.setImageResource(food.getImageId());
        foodName.setText(food.getName());
        foodInclusion.setText(food.getInclusion());
        foodInclusionContent.setText(food.getInclusion_content());
        foodUnit.setText(food.getUnit());
        return view;
    }
}
