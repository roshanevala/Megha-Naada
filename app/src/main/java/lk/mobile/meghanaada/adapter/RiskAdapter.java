package lk.mobile.meghanaada.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lk.mobile.meghanaada.R;
import lk.mobile.meghanaada.model.Risk;

/**
 * Created by ravi on 16/11/17.
 */

public class RiskAdapter extends RecyclerView.Adapter<RiskAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Risk> riskList;
    private List<Risk> riskListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(riskListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public RiskAdapter(Context context, List<Risk> riskList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.riskList = riskList;
        this.riskListFiltered = riskList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Risk risk = riskListFiltered.get(position);
        holder.name.setText(risk.getName());
        holder.phone.setText(risk.getDistrict());

    }

    @Override
    public int getItemCount() {
        return riskListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    riskListFiltered = riskList;
                } else {
                    List<Risk> filteredList = new ArrayList<>();
                    for (Risk row : riskList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getDistrict().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    riskListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = riskListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                riskListFiltered = (ArrayList<Risk>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Risk risk);
    }
}
