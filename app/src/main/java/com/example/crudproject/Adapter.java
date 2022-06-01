package com.example.crudproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    List<Course> courses;
    Context ct;

    public Adapter(Context ct, String json){
        this.ct = ct;
        courses = JsonCourseParser.parse(json);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.class_row,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Course course = courses.get(position);

        holder.id.setText(""+course.getId());
        holder.title.setText(course.getTitle());
        holder.desc.setText(course.getDescription());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView id;
        TextView title;
        TextView desc;

        public MyViewHolder(@NotNull View itemView){
            super(itemView);
            id = itemView.findViewById(R.id.idText_View);
            title = itemView.findViewById(R.id.titleText_View);
            desc = itemView.findViewById(R.id.descText_View);
        }
    }
}
