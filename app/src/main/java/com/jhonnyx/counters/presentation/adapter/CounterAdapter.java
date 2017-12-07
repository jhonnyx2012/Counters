package com.jhonnyx.counters.presentation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.core.presentation.adapter.BaseListAdapter;
import com.core.presentation.adapter.holder.BaseViewHolder;
import com.jhonnyx.counters.R;
import com.jhonnyx.counters.databinding.ItemCounterBinding;
import com.jhonnyx.counters.domain.model.Counter;
import com.jhonnyx.counters.presentation.contract.CounterEventListener;

/**
 * Created by jhonnybarrios on 12/5/17.
 */

public class CounterAdapter extends BaseListAdapter<Counter,CounterAdapter.ViewHolder> {
    private CounterEventListener listener;

    public void setListener(CounterEventListener listener) {
        this.listener = listener;
    }

    @Override protected int getLayoutIdByType(int viewType) {return R.layout.item_counter;}

    @Override protected RecyclerView.ViewHolder createViewHolder(int viewType, View v) {
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseViewHolder<Counter,ItemCounterBinding> {
        ViewHolder(View itemView) {
            super(itemView);
            binder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onDeleteCounter(getItem(getAdapterPosition()));
                }
            });
            binder.ivInc.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onIncrementCounter(getItem(getAdapterPosition()));
                }
            });
            binder.ivDec.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onDecrementCounter(getItem(getAdapterPosition()));
                }
            });
        }

        @Override public void bind(int position, Counter item) {
            binder.tvCount.setText(String.valueOf(item.count));
            binder.tvName.setText(item.title);
            binder.bInitial.setText(String.valueOf(item.title.charAt(0)));
        }
    }
}