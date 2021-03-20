package com.project.nikhil.secfamfinal.Search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.Model.User;
import com.project.nikhil.secfamfinal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapter userAdapter;
    private List<User> userList;
    String data;
    ArrayList<String> nameList;
    ArrayList<String> sexList;
    ArrayList <String> Urilist;
    ArrayList<String> Imagelist;
    EditText search_bar;
    String name;
    String names,uids,sex,image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search_bar = view.findViewById(R.id.search_bar);

        userList = new ArrayList<>();
        nameList=new ArrayList<>();
        Urilist=new ArrayList<>();
        sexList=new ArrayList<>();
        Imagelist=new ArrayList<>();
        userAdapter = new SearchAdapter(getContext(),userList,true);
        recyclerView.setAdapter(userAdapter);


        final Client client = new Client("FJL6U13KJH", "06ed43d2702f9a061bf764767e2ea407");
        // Index index = client.getIndex("secfam");
        final Index index= client.initIndex("Users");

        search_bar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;

                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                {
                    //Toast.makeText(getContext(),"clicked",Toast.LENGTH_LONG).show();


                    CompletionHandler completionHandler = new CompletionHandler() {
                        @Override
                        public void requestCompleted(JSONObject content, AlgoliaException error) {
                            // [...]

                            try {
                                // Toast.makeText(getContext(),""+content,Toast.LENGTH_LONG).show();
                                //  System.out.println(content);

                                parseJsonObject(content);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    index.searchAsync(new com.algolia.search.saas.Query(search_bar.getText().toString()),completionHandler);

                }
                return false;
            }
        });
        // readUsers();
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                //  searchUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  searchUsers(s.toString());


            }


        });

        JSONObject settings = null;

        //index.setSettingsAsync(settings, null);

        try {
            settings = new JSONObject()
                    .put("searchableAttributes", "name")
                    .put("searchableAttributes", "email");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert settings != null;
        index.setSettingsAsync(settings, null);



        return view;
    }

    private void searchUsers(final String s){

        Query query= FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("name")
                .startAt(s)
                .endAt(s+"\uf8ff");

        final Query query2 = FirebaseDatabase.getInstance().getReference().child("Police").orderByChild("name")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    User user = snapshot.getValue(User.class);

                    userList.add(user);
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(!s.equals(User.class.getName()))
        {
            Toast.makeText(getContext(),"not here",Toast.LENGTH_LONG).show();}

    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_bar.getText().toString().equals("")) {
                    userList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            User user = snapshot.getValue(User.class);

                            userList.add(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void parseJsonObject(JSONObject responseJsonObject) throws JSONException {
        userList.clear();

        ArrayList<User> MARKET_CAP_ARRAY_LIST = new ArrayList<>();

        JSONArray marketCapArray = responseJsonObject.optJSONArray("hits");

        for (int i=0; i< marketCapArray.length(); i++){
            JSONObject restaurantJSON = marketCapArray.getJSONObject(i);

            String name = restaurantJSON.getString("name");
            String id = restaurantJSON.getString("id");
            String image = restaurantJSON.getString("image");
            // JSONArray array1 = (JSONArray) marketCapArray.get(i);

            User user=new User(name,id,image,0,null,null,null,null);
            userList.add(user);


        }
        userAdapter.notifyDataSetChanged();

    }


}
