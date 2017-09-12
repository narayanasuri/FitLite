package koolkat.fitlite.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import koolkat.fitlite.Oil;
import koolkat.fitlite.R;

/**
 * Created by Admin on 4/13/2017.
 */

public class AdminProductsFragment extends Fragment {

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private RecyclerView oilsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdminProductAdapter oilsAdapter;
    int productId;
    List<Oil> oilList = new ArrayList<Oil>();
    List<String> oilTitles = new ArrayList<String>();
    List<String> oilQuantities = new ArrayList<String>();
    List<String> oilPrices = new ArrayList<String>();

    private int x = 1;

    public AdminProductsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_order, container, false);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.product_fragment_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Toast Message!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        oilsRecyclerView = (RecyclerView) view.findViewById(R.id.prices_recycler_view);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        oilsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        oilsRecyclerView.setLayoutManager(layoutManager);
        oilsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        oilsAdapter = new AdminProductAdapter(oilList);

        oilsRecyclerView.setAdapter(oilsAdapter);

        databaseReference.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                oilList.clear();
                oilTitles.clear();
                oilPrices.clear();
                oilQuantities.clear();

                productId = Integer.parseInt(dataSnapshot.child("numberOfProducts").getValue().toString());

                Iterable<DataSnapshot> oilProducts = dataSnapshot.child("oils").getChildren();

                for (DataSnapshot info : oilProducts) {

                    Oil oil = info.getValue(Oil.class);
                    oilList.add(oil);
                    String title = oil.getTitle();
                    String quantity = oil.getQuantity()+"";
                    String price = oil.getPrice()+"";
                    oilTitles.add(title);
                    oilQuantities.add(quantity);
                    oilPrices.add(price);

                    oilsRecyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getContext());
                    oilsRecyclerView.setLayoutManager(layoutManager);
                    oilsRecyclerView.setItemAnimator(new DefaultItemAnimator());

                    oilsAdapter = new AdminProductAdapter(oilList);

                    oilsRecyclerView.setAdapter(oilsAdapter);

                }

                Log.i("Oil Id", productId+"");
                Log.i("Oils", oilList.size()+"");
                Log.i("Oil Product Adapter", oilsAdapter.getItemCount()+"");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
