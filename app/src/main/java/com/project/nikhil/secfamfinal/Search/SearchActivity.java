package com.project.nikhil.secfamfinal.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.*;
import com.project.nikhil.secfamfinal.Model.User;
import com.project.nikhil.secfamfinal.Post.PostActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchAdapter userAdapter;
    private List<User> userList;
    String data;
    ArrayList<String> nameList;
    ArrayList<String> sexList;
    ArrayList <String> Urilist;
    ArrayList<String> Imagelist;
    EditText search_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        search_bar = findViewById(R.id.search_bar);

        userList = new ArrayList<>();
        nameList=new ArrayList<>();
        Urilist=new ArrayList<>();
        sexList=new ArrayList<>();
        Imagelist=new ArrayList<>();
        userAdapter = new SearchAdapter(getApplicationContext(),userList,true);
        recyclerView.setAdapter(userAdapter);


        final Client client = new Client("NPINR9OFVH", "c65501f44b41c4a82deb285e74f2b8d9");
        // Index index = client.getIndex("secfam");
        final Index index= client.getIndex("Users");


        JSONObject settings = null;

        //index.setSettingsAsync(settings, null);

        try {
            settings = new JSONObject()
                    .put("searchableAttributes", "name")
                    .put("searchableAttributes", "email")
                     .put("unretrievableAttributes","bio");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert settings != null;

       index.setSettingsAsync(settings,null);

        search_bar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN)
                    return false;


                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                {



                    CompletionHandler completionHandler = new CompletionHandler() {
                        @Override
                        public void requestCompleted(JSONObject content, AlgoliaException error) {
                            // [...]

                            try {
                                parseJsonObject(content);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    if(!search_bar.getText().toString().isEmpty())
                    index.searchAsync(new Query(search_bar.getText().toString()),completionHandler);

                    return false;
                }
                return  false;
            }
        });
        // readUsers();




    }



    private void parseJsonObject(JSONObject responseJsonObject) throws JSONException {
        userList.clear();

        ArrayList<User> MARKET_CAP_ARRAY_LIST = new ArrayList<>();

        JSONArray marketCapArray = responseJsonObject.optJSONArray("hits");
        for (int i=0; i< marketCapArray.length(); i++){
            JSONObject restaurantJSON = marketCapArray.getJSONObject(i);

            String name = null;
            try {
                name = restaurantJSON.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String id = null;
            try {
                id = restaurantJSON.getString("objectID");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String image=null;
            try {
               image= restaurantJSON.getString("image");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // JSONArray array1 = (JSONArray) marketCapArray.get(i);

            User user=new User(name,id,image,0,null,null,null,null);
            userList.add(user);
            Toast.makeText(getApplicationContext(),""+userList.size(),Toast.LENGTH_SHORT).show();


        }
        userAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //   startActivity(new Intent(SearchActivity.this, PostActivity.class));

    }
}
