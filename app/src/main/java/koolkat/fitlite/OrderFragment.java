package koolkat.fitlite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    private final List<String> oilTypes = new ArrayList<>();
    private final List<Integer> oilQuantitities = new ArrayList<>();
    private final List<Integer> pricey = new ArrayList<>();
    private final List<String> statuses = new ArrayList<>();
    private final List<Integer> discounts = new ArrayList<>();
    private final List<String> dates = new ArrayList<>();
    private final List<OilRequest> oilRequestsList = new ArrayList<>();

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;


    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oilTypes.clear();
                oilQuantitities.clear();
                statuses.clear();
                pricey.clear();
                discounts.clear();
                dates.clear();
                FirebaseUser user = firebaseAuth.getCurrentUser();

                Iterable<DataSnapshot> oilInformation = dataSnapshot.child(user.getUid()).child("orders").getChildren();
                for (DataSnapshot info : oilInformation) {
                    OilRequest oilRequest = info.getValue(OilRequest.class);
                    oilRequestsList.add(oilRequest);
                    oilTypes.add(oilRequest.getOilType());
                    oilQuantitities.add(oilRequest.getOilQuantity());
                    statuses.add(oilRequest.getStatus());
                    pricey.add(oilRequest.getPrice());
                    discounts.add(oilRequest.getDiscount());
                    dates.add(oilRequest.getOrderdate());
                }

                recyclerView.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                adapter = new CustomAdapter(oilTypes, oilQuantitities, pricey, statuses, discounts, dates);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
