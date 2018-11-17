package com.infomart.worldmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infomart.worldmap.Pojo.CountryResponse;
import com.infomart.worldmap.R;
import com.infomart.worldmap.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryListHolder> implements Filterable {

    public EventListener mEventListener;
    Context context;
    private List<CountryResponse.CountryDetails> data = new ArrayList<>();
    private List<CountryResponse.CountryDetails> dataFilter = new ArrayList<>();

    public CountryListAdapter(Context context) {
        this.context = context;
    }

    public void addAll(List<CountryResponse.CountryDetails> mData) {
        data.clear();
        dataFilter.clear();
        data.addAll(mData);
        dataFilter.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        dataFilter.clear();
        notifyDataSetChanged();
    }

    public boolean checkSize() {
        if (data.size() == 0)
            return true;
        else
            return false;
    }


    public CountryResponse.CountryDetails getItem(int position) {
        return data.get(position);
    }

    @Override
    public CountryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_country_list, parent, false);
        return new CountryListHolder(v);
    }

    @Override
    public void onBindViewHolder(final CountryListHolder holder, final int position) {

        holder.txtCountryName.setText(Utils.bindText(dataFilter.get(position).getName()));

        holder.txtCapital.setText(" - " + Utils.bindText(dataFilter.get(position).getCapital()));

        Glide.with(context)
                .load(dataFilter.get(position).getImage())
                .error(context.getResources().getDrawable(R.drawable.ic_placeholder))
                .placeholder(context.getResources().getDrawable(R.drawable.ic_placeholder))
                .into(holder.imgFlag);

        holder.rawItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventListener != null) {
                    mEventListener.onClick(dataFilter.get(position));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataFilter.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    public interface EventListener {
        void onClick(CountryResponse.CountryDetails Detail);

    }

    static class CountryListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtCountryName)
        TextView txtCountryName;
        @BindView(R.id.txtCapital)
        TextView txtCapital;
        @BindView(R.id.imgFlag)
        ImageView imgFlag;

        @BindView(R.id.activity_main)
        LinearLayout rawItem;

        public CountryListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataFilter = data;
                } else {
                    List<CountryResponse.CountryDetails> filteredList = new ArrayList<>();
                    for (CountryResponse.CountryDetails row : data) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    dataFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataFilter = (ArrayList<CountryResponse.CountryDetails>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}