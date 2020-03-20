package com.example.wah;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class PostsOnProfileFragment {
    private static final PostsOnProfileFragment ourInstance = new PostsOnProfileFragment();
    ArrayList<ProfilePosts> profilePosts;
    public static PostsOnProfileFragment getInstance() {
        return ourInstance;
    }

    private PostsOnProfileFragment() {
        profilePosts = new ArrayList<ProfilePosts>();



        ProfilePosts post1 = new ProfilePosts(R.drawable.babusar, "Babusar", "3", "Lahore", "21/1/2020");
        ProfilePosts post2 = new ProfilePosts(R.drawable.fairymeadows, "Fairy Meadows", "5", "Islamabad", "11/1/2020");
        ProfilePosts post3 = new ProfilePosts(R.drawable.gilgit, "Gilgit", "8", "Lahore", "5/1/2020");
        ProfilePosts post4 = new ProfilePosts(R.drawable.hunza, "Hunza", "10", "Islamabad", "21/11/2019");
        ProfilePosts post5 = new ProfilePosts(R.drawable.islamabad, "Islamabad", "1", "Lahore", "5/11/2019");
        ProfilePosts[] posts = {post1, post2, post3, post4, post5};

        profilePosts.add(post1);
        profilePosts.add(post2);
        profilePosts.add(post3);
        profilePosts.add(post4);
        profilePosts.add(post5);

   //     AsyncGetPost asyncGetPost= new AsyncGetPost();
   //    asyncGetPost.execute(profilePosts);





    }

    public void addItem (ProfilePosts profilePost)
    {
        profilePosts.add(0,profilePost);
    }

    public ArrayList<ProfilePosts> getProfilePosts() {
        return profilePosts;


    }


    public class AsyncGetPost extends AsyncTask<ArrayList<ProfilePosts>, Integer, Integer>
    {


        @Override
        protected Integer doInBackground(ArrayList<ProfilePosts>... arrayLists) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Posts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {


                                for (QueryDocumentSnapshot document : task.getResult()) {
                                   // Log.d("success", document.getId() + " => " + document.getData());
                                    Log.d("POST", document.get("finalDest").toString());
                                    Map<String, Object> myhash = new HashMap<String, Object>();
                                   myhash= document.getData();

                                   Object obj=  myhash.get("source");

                                   String src= obj.toString();
                                  String name= myhash.get("finalDest.name").toString();
                                  // Stop finaldest=new  Stop ( (myhash.get("finalDest.name").toString()
                                   Log.d("name", name);

                                    Log.d("src", src);
                                   // Post p= new Post(src, ,  )

                                   //myhash.get()
                                   // Log.d("POST", "HELLO");
                                }
                            } else {
                                Log.w("hello", "Error getting documents.", task.getException());
                            }
                        }
                    });



            return 6;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

/*
            Stop dest= ((DashBoard)getActivity()).newPost.getFinalDest();

            String finalDest= dest.getName();
            String src= ((DashBoard)getActivity()).newPost.getSource();

            ProfilePosts currpost = new ProfilePosts(R.drawable.defaultpic,finalDest, "10", src, "29/02/2020");
            PostsOnProfileFragment postsOnProfileFragment = PostsOnProfileFragment.getInstance();
            postsOnProfileFragment.addItem(currpost);
  */
        }
    }
}
