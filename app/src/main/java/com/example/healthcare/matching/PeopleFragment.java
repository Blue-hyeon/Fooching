package com.example.healthcare.matching;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.healthcare.MainActivity;
import com.example.healthcare.R;
import com.example.healthcare.StartActivity;
import com.example.healthcare.chat.MessageActivity;
import com.example.healthcare.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PeopleFragment
 * create an instance of this fragment.
 */
public class PeopleFragment extends Fragment {
    ArrayList<UserModel> search_list = new ArrayList<>();
    ArrayList<UserModel> userModels;
    PeopleFragmentRecyclerViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_people,container,false);
        userModels = new ArrayList<>();

        EditText editText = view.findViewById(R.id.matching_search_et);
//        PeopleFragmentRecyclerViewAdapter adapter = new PeopleFragmentRecyclerViewAdapter();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = editText.getText().toString();
                search_list.clear();
                if(searchText.equals("")){
                    adapter.setItems(userModels);
                }
                else {
                    // 검색 단어를 포함하는지 확인
                    Log.e("3333333333", String.valueOf(userModels.size()));
                    for (int a = 0; a < userModels.size(); a++) {
                        if (userModels.get(a).userName.toLowerCase().contains(searchText.toLowerCase())) {
                            search_list.add(userModels.get(a));
                        }
                        if (userModels.get(a).comment!=null && userModels.get(a).comment.toLowerCase().contains(searchText.toLowerCase())) {
                            search_list.add(userModels.get(a));
                        }
                        adapter.setItems(search_list);
                    }
                    Log.e("3333333333", String.valueOf(search_list.size()));
                }
            }
        });
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.peoplefragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());
        adapter = new PeopleFragmentRecyclerViewAdapter(userModels);
        recyclerView.setAdapter(adapter);
        return view;
    }
    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private ArrayList<UserModel> items;
        private PeopleFragmentRecyclerViewAdapter(ArrayList<UserModel> list){
            items = list;
            notifyDataSetChanged();
        }
        public PeopleFragmentRecyclerViewAdapter() {
            userModels = new ArrayList<>();

            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModels.clear();

                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                        UserModel userModel = snapshot.getValue(UserModel.class);

                        if(userModel.uid.equals(myUid)){
                            continue;
                        }
                        userModels.add(userModel);
                    }
                    notifyDataSetChanged();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);


            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with
                    (holder.itemView.getContext())
                    .load(items.get(position).profileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder)holder).imageView);
            ((CustomViewHolder)holder).textView.setText(items.get(position).userName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // MatchingAtivity 이동
                    Intent intent = new Intent(v.getContext(),MatchingActivity.class);
                    intent.putExtra("destinationUid",items.get(position).uid);
                    startActivity(intent);
//                    Intent intent = new Intent(v.getContext(),MessageActivity.class);
//                    intent.putExtra("destinationUid",userModels.get(position).uid);
//                    startActivity(intent);
                }
            });
            if(items.get(position).comment !=null){
                ((CustomViewHolder) holder).textView_comment.setText(items.get(position).comment);
            }

//            Glide.with
//                    (holder.itemView.getContext())
//                    .load(userModels.get(position).profileImageUrl)
//                    .apply(new RequestOptions().circleCrop())
//                    .into(((CustomViewHolder)holder).imageView);
//            ((CustomViewHolder)holder).textView.setText(userModels.get(position).userName);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // MatchingAtivity 이동
//                    Intent intent = new Intent(v.getContext(),MatchingActivity.class);
//                    intent.putExtra("destinationUid",userModels.get(position).uid);
//                    startActivity(intent);
////                    Intent intent = new Intent(v.getContext(),MessageActivity.class);
////                    intent.putExtra("destinationUid",userModels.get(position).uid);
////                    startActivity(intent);
//                }
//            });
//            if(userModels.get(position).comment !=null){
//                ((CustomViewHolder) holder).textView_comment.setText(userModels.get(position).comment);
//            }

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;
            public TextView textView_comment;
            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.frienditem_imageview);
                textView = (TextView) view.findViewById(R.id.frienditem_textview);
                textView_comment = (TextView) view.findViewById(R.id.frienditem_state_comment);
            }
        }
        public void setItems(ArrayList<UserModel> list){
            items = list;
            notifyDataSetChanged();
        }
    }

}