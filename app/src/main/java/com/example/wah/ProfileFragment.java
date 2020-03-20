package com.example.wah;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements AsyncMakePostResponse {

    private ProgressBar progbar;
    RecyclerView recyclerView;
   static ArrayList<ProfilePosts> allPosts;
    ProfilePostsAdapter myadapt;
   static Boolean initialize=true;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance)
    {
        Toast.makeText(getActivity(),"On Create view called",Toast.LENGTH_SHORT).show();
        Log.d("SHOW MSG ZOHAIB", "On Create view");
        progbar= (ProgressBar) getActivity().findViewById(R.id.makePostProgBar);

        View view =  inflater.inflate(R.layout.fragment_profie,container,false);
        TextView userName = view.findViewById(R.id.Profile_TextView_Username);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user;
         recyclerView = view.findViewById(R.id.Profile_RecyclerView_UserPosts);
        LinearLayoutManager llm= new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm );
        // String [] languages = {"Java","C","C++","Python", "JavaScript","Java","C","C++","Python", "JavaScript"};
        /// recyclerView.setAdapter(new MyAdapter(languages));

        //ADD FETCH FROM DB CODE HERE, INSTEAD OF THIS HARD CODE

        Log.d("SHOW MSG ZOHAIB", "On Attach ");
        PostsOnProfileFragment postsOnProfileFragment = PostsOnProfileFragment.getInstance();
        allPosts = postsOnProfileFragment.getProfilePosts();
        myadapt = new ProfilePostsAdapter(allPosts);

            recyclerView.setAdapter(myadapt);



        ///////////////////////////////////////////////////////

        //Toast.makeText(getContext(),"Hello",Toast.LENGTH_LONG);
        TextView btnLogout= view.findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent ToMain= new Intent(getActivity(),MainActivity.class);
                startActivity(ToMain);
            }
        });
        if(mAuth!=null)
        {
            Log.d("Error","Hello");
            user = mAuth.getCurrentUser();
            if(user!=null) {
            String uname =  user.getDisplayName();
          //  Toast.makeText(getActivity(),uname,Toast.LENGTH_LONG);
            Log.d("uname", uname);
            userName.setText(uname);

        }

        }
//        if(user!=null) {
//            String uname =  user.getDisplayName();
//            Toast.makeText(getActivity(),uname,Toast.LENGTH_LONG);
//            Log.d("uname", uname);
//           // userName.setText(uname);
//
//        }
//        else
//        {
//            Toast.makeText(getActivity(),"Hello",Toast.LENGTH_LONG);
//            Log.d("Error","Hello");
//        }
       TextView makePost= (TextView) view.findViewById(R.id.profile_textview_Whatsonurmind);


        makePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getActivity(), Post_Form.class);
                startActivityForResult(i, 111);


            }
        });


        return view;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
    Toast.makeText(getActivity(),"On Attach called",Toast.LENGTH_SHORT).show();
        Log.d("SHOW MSG ZOHAIB", "On Attach");
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
          Toast.makeText(getActivity(),"On Create called",Toast.LENGTH_LONG).show();
       // Toast.makeText(getActivity(),"On Attach called",Toast.LENGTH_SHORT).show();
        Log.d("SHOW MSG ZOHAIB", "On Create");
        Boolean flag= ((DashBoard)getActivity()).newPostFlag;
        Post newPost= ((DashBoard)getActivity()).newPost;

        if(flag==true)
        {

            progbar= (ProgressBar) getActivity().findViewById(R.id.makePostProgBar);
            ProfileFragment.AsyncMakePost makepostAsync= new ProfileFragment.AsyncMakePost();
            makepostAsync.setListener(this);
            makepostAsync.execute(newPost);
        }


        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Toast.makeText(getActivity(),"On Activity Created called",Toast.LENGTH_LONG).show();

        Log.d("SHOW MSG ZOHAIB", "On Activity Created");

        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void notifyMakePostStatus(Boolean flag) {

        Toast.makeText(getActivity(),"notifyMakePostStatus called",Toast.LENGTH_LONG).show();

        if(flag==true)
        {
            Toast.makeText(getActivity(), "Posted Successfully", Toast.LENGTH_LONG).show();
          //  allPosts.add((DashBoard)getActivity()).newPost);

            //ProfilePosts post5 = new ProfilePosts(R.drawable.islamabad,"Islamabad", "1", "Lahore", "5/11/2019");

            Stop dest= ((DashBoard)getActivity()).newPost.getFinalDest();

            String finalDest= dest.getName();
            String src= ((DashBoard)getActivity()).newPost.getSource();
            int date=((DashBoard)getActivity()).newPost.getDate();
            int days=((DashBoard)getActivity()).newPost.getDays();

            String stringdate= Integer.toString(date);
            String stringdays= Integer.toString(days);

            ProfilePosts currpost = new ProfilePosts(R.drawable.defaultpic,finalDest, stringdays  , src, stringdate);
            PostsOnProfileFragment postsOnProfileFragment = PostsOnProfileFragment.getInstance();
            postsOnProfileFragment.addItem(currpost);
            //allPosts.add(0,currpost);
          //  myadapt.notifyItemInserted(0);
            allPosts = postsOnProfileFragment.getProfilePosts();
            myadapt.notifyDataSetChanged();

            ((DashBoard)getActivity()).newPostFlag= false;
            ((DashBoard)getActivity()).newPost=null;
            getActivity().getIntent().removeExtra("PostMadeFlag");
            initialize=false;


        }
        else
        {
         //   Toast.makeText(getActivity(), "Posted Successfully", Toast.LENGTH_LONG).show();
         //   Log.d("MAKE POST STATUS", "FAILED TO POST");
        }




    }


    public class AsyncMakePost extends AsyncTask<Post, Integer, Integer> {


        private AsyncMakePostResponse asyncMakePostResponse;




        void setListener(AsyncMakePostResponse obj)
        {
            this.asyncMakePostResponse= obj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progbar.setMax(10);
            //    progressBar.setProgress(100);
        }

        @Override
        protected Integer doInBackground(Post... posts) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Add a new document with a generated ID
            db.collection("Posts")
                    .add(posts[0])
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("successfully added", "DocumentSnapshot added with ID: " + documentReference.getId());

                            /*
                            for(int i=0; i<10; i++)
                            {

                                    publishProgress(i);
                            }

                             */
                            //publishProgress(10);
                            Toast.makeText(getActivity(), "Async task called", Toast.LENGTH_LONG).show();
                            asyncMakePostResponse.notifyMakePostStatus(true);
                            //    ((DashBoard)getActivity()).newPostFlag= false;
                            //    ((DashBoard)getActivity()).newPost=null;

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("failed to add to db ", "Error adding document", e);
                            asyncMakePostResponse.notifyMakePostStatus(false);
                        }
                    });




            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progbar.setProgress(values[0]);


        }


    }


}
