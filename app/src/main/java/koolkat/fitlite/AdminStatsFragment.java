package koolkat.fitlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 4/15/2017.
 */

public class AdminStatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private AdminOrderCustomAdapter adapter;

    final private List<String> usernames = new ArrayList<String>();
    final private List<String> phonenumbers = new ArrayList<String>();
    final private List<Integer> orderNumbers = new ArrayList<Integer>();
    final private List<UserInformation> userInformations = new ArrayList<UserInformation>();

    public AdminStatsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_stats, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.order_recycler_view);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdminOrderCustomAdapter(usernames, phonenumbers, orderNumbers);

        recyclerView.setAdapter(adapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, true);

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usernames.clear();
                phonenumbers.clear();
                orderNumbers.clear();

                collectData((Map<String, Object>) dataSnapshot.getValue());

                recyclerView.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                adapter = new AdminOrderCustomAdapter(usernames, phonenumbers, orderNumbers);

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    final int i = rv.getChildAdapterPosition(child);

                    String usrname = usernames.get(i);
                    String phone = phonenumbers.get(i);
                    Intent intent = new Intent(getContext(), AdminOrderViewActivity.class);
                    intent.putExtra("username", usrname);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        return view;
    }

    void collectData(Map<String, Object> users) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            if (singleUser.get("username").toString().equalsIgnoreCase("narayanasuri") || singleUser.get("username").toString().equalsIgnoreCase("selvakumarasamy"))
                continue;
            usernames.add(singleUser.get("username").toString());
            phonenumbers.add(singleUser.get("phonenumber").toString());
            orderNumbers.add(Integer.parseInt(singleUser.get("numberOfOrders").toString()));

        }

    }
}
