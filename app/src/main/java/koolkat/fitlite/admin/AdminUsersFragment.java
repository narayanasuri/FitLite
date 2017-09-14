package koolkat.fitlite.admin;

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

import koolkat.fitlite.AdminOrderViewActivity;
import koolkat.fitlite.R;
import koolkat.fitlite.UserInformation;

/**
 * Created by Admin on 4/15/2017.
 */

public class AdminUsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth firebaseAuth;
    private AdminUserAdapter adapter;

    final private List<String> names = new ArrayList<>();
    final private List<String> phonenumbers = new ArrayList<>();
    final private List<String> uid = new ArrayList<>();
    final private List<Integer> orderNumbers = new ArrayList<>();
    final private List<UserInformation> userInformations = new ArrayList<>();

    public AdminUsersFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_users, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.users_recycler_view);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdminUserAdapter(names, phonenumbers, orderNumbers);

        recyclerView.setAdapter(adapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, true);

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                names.clear();
                phonenumbers.clear();
                orderNumbers.clear();
                uid.clear();

                Iterable<DataSnapshot> userinformation = dataSnapshot.getChildren();
                for (DataSnapshot info : userinformation) {
                    UserInformation user = info.getValue(UserInformation.class);
                    userInformations.add(user);
                    if (user.getUniqueid().equalsIgnoreCase("PEFyeeNaAbOtsoUqVeVJPSIrYjn2") || user.getName().equalsIgnoreCase("selvakumarasamy"))
                        continue;
                    names.add(user.getName());
                    phonenumbers.add(user.getPhonenumber());
                    orderNumbers.add(user.getNumberOfOrders());
                    uid.add(user.getUniqueid());
                }


                recyclerView.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                adapter = new AdminUserAdapter(names, phonenumbers, orderNumbers);

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

                    String usrname = names.get(i);
                    String phone = phonenumbers.get(i);
                    Intent intent = new Intent(getContext(), AdminOrderViewActivity.class);
                    intent.putExtra("username", usrname);
                    intent.putExtra("uid",uid.get(i));
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


}
