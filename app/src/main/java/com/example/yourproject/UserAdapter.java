package com.example.yourproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private Context userContext;
    private List<User> userList;

    public UserAdapter(Context context, List<User> users) {
        userList = users;
        userContext = context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }
    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        UserViewHolder holder;

        LayoutInflater userInflater = (LayoutInflater)
                userContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = userInflater.inflate(R.layout.user, null);

        holder = new UserViewHolder();
        holder.idView = (TextView) view.findViewById(R.id.id);
        holder.firstNameView = (TextView) view.findViewById(R.id.first_name);
        holder.lastNameView = (TextView) view.findViewById(R.id.last_name);
        holder.locationView = (TextView) view.findViewById(R.id.location);
        view.setTag(holder);

        User user = (User) getItem(i);
        holder.idView.setText(user.id == null ? "" : user.id);
        holder.firstNameView.setText(user.firstName == null ? "" : user.firstName);
        holder.lastNameView.setText(user.lastName == null ? "" : user.lastName);
        holder.locationView.setText(user.location == null ? "" : user.location);
        return view;
    }

    public void add(User user) {
        userList.add(user);
        notifyDataSetChanged();
    }


    private static class UserViewHolder {

        public TextView idView;
        public TextView firstNameView;
        public TextView lastNameView;
        public TextView locationView;
    }
}
