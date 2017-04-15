package koolkat.fitlite;

/**
 * Created by Admin on 4/14/2017.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<String> oilTypes;
    private List<Integer> oilQuantitities;
    private List<Integer> pricey;
    private List<String> statuses;

    public CustomAdapter(List<String> oilTypes, List<Integer> oilQuantitities, List<Integer> pricey, List<String> statuses) {
        this.oilTypes = oilTypes;
        this.oilQuantitities = oilQuantitities;
        this.pricey=pricey;
        this.statuses = statuses;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView oiltypetv;
        TextView oilquantitytv;
        TextView statustv;
        TextView pricetv;


        public ViewHolder(View itemView) {
            super(itemView);
            oiltypetv = (TextView) itemView.findViewById(R.id.card_oiltype);
            oilquantitytv = (TextView) itemView.findViewById(R.id.card_oilquantity);
            statustv = (TextView) itemView.findViewById(R.id.card_status);
            pricetv = (TextView) itemView.findViewById(R.id.card_price);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, int i) {

        viewHolder.oiltypetv.setText("Oil : "+oilTypes.get(i));
        String quant = oilQuantitities.get(i).toString();
        String price = pricey.get(i).toString();
        viewHolder.oilquantitytv.setText("Quantity : "+quant+" litres");
        viewHolder.pricetv.setText("Price : ₹"+price+"/-");
        viewHolder.statustv.setText("Status : "+statuses.get(i));
    }

    @Override
    public int getItemCount() {
        return oilTypes.size();
    }
}