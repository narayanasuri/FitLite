package koolkat.fitlite.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import koolkat.fitlite.AdminOrderViewActivity;
import koolkat.fitlite.Oil;
import koolkat.fitlite.R;

/**
 * Created by Admin on 4/13/2017.
 */

public class AdminProductsFragment extends Fragment {

    OnFragmentInteractionListener mListener;
    private RecyclerView oilsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    private AdminProductAdapter oilsAdapter;
    int productId;
    List<Oil> oilList = new ArrayList<Oil>();

    private int x = 1;

    public AdminProductsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_order, container, false);

        oilsRecyclerView = (RecyclerView) view.findViewById(R.id.prices_recycler_view);
        databaseReference = FirebaseDatabase.getInstance().getReference();

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

                productId = Integer.parseInt(dataSnapshot.child("numberOfProducts").getValue().toString());

                Iterable<DataSnapshot> oilProducts = dataSnapshot.child("oils").getChildren();

                for (DataSnapshot info : oilProducts) {

                    Oil oil = info.getValue(Oil.class);
                    oilList.add(oil);

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

        oilsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), oilsRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                mListener.onProductLongTouch(position);
                showEditOilDialog(oilList.get(position), position);
            }

            @Override
            public void onLongClick(View view, int position) {
                showDeleteDialog(position);
            }
        }));

        return view;
    }

    public void showDeleteDialog(final int position){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set title
        alertDialogBuilder.setTitle("Alert!");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to delete the oil " + oilList.get(position).getTitle() +"?")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        oilList.remove(position);
                        databaseReference.child("products").child("oils").child(String.valueOf(position+1)).removeValue();
                        Toast.makeText(getContext(), "Oil successfully deleted!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.RED);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.RED);

    }

    public void showEditOilDialog(Oil oil, final int position){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_oil_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = (Spinner) dialogView.findViewById(R.id.dialog_quantity_spinner);
        final EditText titleEt = (EditText) dialogView.findViewById(R.id.add_oil_name_et);
        titleEt.setText(oil.getTitle());
        final EditText priceEt = (EditText) dialogView.findViewById(R.id.add_oil_price_et);
        priceEt.setText(oil.getPrice()+"");

        float quant = oil.getQuantity();

        if(quant == 0.5f)
            spinner1.setSelection(0);
        else if(quant == 1.0f)
            spinner1.setSelection(1);
        else
            spinner1.setSelection(2);

        dialogBuilder.setTitle("Edit Oil");
//        dialogBuilder.setMessage();
        dialogBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                String title = titleEt.getText().toString();
                String priceStr = priceEt.getText().toString();

                int langpos = spinner1.getSelectedItemPosition();
                float quantity;
                switch(langpos) {
                    case 0:
                        quantity = 0.5f;
                        break;
                    case 1:
                        quantity = 1;
                        break;
                    default:
                        quantity = 1.5f;
                        break;
                }

                if(!title.isEmpty() && !priceStr.isEmpty()){
                    int price = Integer.parseInt(priceStr);
                    Oil oil = new Oil(title, price, quantity);
                    databaseReference.child("products").child("oils").child(String.valueOf(position+1)).setValue(oil);
                }

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

        Button nbutton = b.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.RED);
        Button pbutton = b.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.RED);

    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
//        try {
//            mListener = (OnFragmentInteractionListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement OnHeadlineSelectedListener");
//        }
    }

    public interface OnFragmentInteractionListener {
        public void onProductLongTouch(int position);
    }

}
