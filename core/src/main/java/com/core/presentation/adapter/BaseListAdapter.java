package com.core.presentation.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.core.R;
import com.core.presentation.adapter.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<T,V extends BaseViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int VIEW_TYPE_FOOTER= 34444;
    protected static final int VIEW_TYPE_ITEM = 5454;
    protected static final int VIEW_TYPE_LOADING = 65656;
    protected com.core.presentation.adapter.OnItemClickListener<T> onItemClickListener;
    protected List<T> list;
    private boolean hasFooter;
    private boolean shouldLoadMore;
    private boolean isItemClickable;
    private boolean isLoading;
    private int itemsPerPage;
    private int lastPage;
    private OnItemLongClickListener<T> longClickListener;

    public BaseListAdapter() {
        this.list = new ArrayList<>();
        this.onItemClickListener = null;
        setDefaults(false,false,false);
    }

    public BaseListAdapter(List<T> list) {
        this.list = list;
        this.onItemClickListener = null;
        setDefaults(false,false,false);
    }

    public BaseListAdapter(List<T> list, com.core.presentation.adapter.OnItemClickListener<T> listener) {
        this.list=list;
        this.onItemClickListener=listener;
        setDefaults(false,false,true);
    }

    public void setDefaults(boolean hasFooter,boolean shouldLoadMore,boolean isItemClickable) {
        setHasFooter(hasFooter);
        this.shouldLoadMore=shouldLoadMore;
        setItemClicklable(isItemClickable);
    }

    public void setOnItemClickListener(com.core.presentation.adapter.OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        setItemClicklable(true);
    }

    public void setOnLoadMoreListener(final RecyclerView recyclerView, final int itemsPerPage, final OnLoadMoreListener mOnLoadMoreListener) {
        shouldLoadMore = true;
        isLoading=false;
        this.itemsPerPage=itemsPerPage;
        try {
            NestedScrollView nestedScrollView = (NestedScrollView) recyclerView.getParent().getParent();
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (v.getChildAt(v.getChildCount() - 1) != null) {
                        if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                                scrollY > oldScrollY) {
                            attempToLoadMore(recyclerView,mOnLoadMoreListener);
                        }
                    }
                }
            });
        } catch (Exception e) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastVisiblePosition >= getItemCount()-2) {
                        attempToLoadMore(recyclerView,mOnLoadMoreListener);
                    }
                }
            });
        }
    }

    private void attempToLoadMore(View view, final OnLoadMoreListener loadMoreListener) {
        if (!isLoading) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    int page = getPage();
                    if (loadMoreListener != null&&page!=lastPage&&page>1) {
                        setLoading(true);
                        lastPage=page;
                        loadMoreListener.onLoadMore(page);
                    }
                }
            });
        }
    }

    private int getPage() {
        int page;
        if(getList() == null || getList().size()==0)
            page=1;
        else
            page=(getList().size()/itemsPerPage)+1;
        return page;
    }

    public void setLoading(boolean loading) {
        if(list==null)return;
        isLoading = loading;
        notifyItemChanged(list.size());
    }

    protected void setItemClicklable(boolean clicklable) {
        this.isItemClickable=clicklable;
    }

    @Override
    public int getItemViewType(int position) {
        int footerIndex = (list==null?0:list.size())+(shouldLoadMore ? 1 : 0);
        if (hasFooter&&(position ==footerIndex)) {
            return VIEW_TYPE_FOOTER;
        }
        if (shouldLoadMore&&(position == ((list==null?0:list.size())))) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int idLayout = viewType == VIEW_TYPE_LOADING ? R.layout.item_loading : getLayoutIdByType(viewType);
        ViewDataBinding binding= DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),idLayout,parent,false);
        View v= binding.getRoot();
        if (viewType == VIEW_TYPE_FOOTER) {
            return getNewFooterViewHolder(v);
        }else if(viewType==VIEW_TYPE_LOADING)
            return new LoadingViewHolder(v);
        return createViewHolder(viewType,v);
    }

    protected abstract RecyclerView.ViewHolder createViewHolder(int viewType, View v);

    public void remove(int adapterPosition) {
        list.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public void addAtFirst(T item) {
        list.add(0,item);
        notifyItemInserted(0);
    }

    public void add(T item) {
        list.add(item);
        notifyItemInserted(list.size()-1);
    }

    public void setList(List<T> items, boolean notify) {
        list=items;
        if(notify)
            notifyDataSetChanged();
    }

    protected RecyclerView.ViewHolder getNewFooterViewHolder(View v) {
        return null;
    }

    protected abstract int getLayoutIdByType(int viewType);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(isItemClickable)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null&&getItem(holder.getAdapterPosition())!=null)
                        onItemClickListener.onItemClick(holder.getAdapterPosition(),getItem(holder.getAdapterPosition()));
                }
            });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(longClickListener!=null)
                    longClickListener.onItemLongClick(holder,holder.getAdapterPosition(),getItem(holder.getAdapterPosition()));
                return longClickListener!=null;
            }
        });
        if (holder instanceof LoadingViewHolder)
            onBindLoadingViewHolder((LoadingViewHolder) holder);
        else if(getItem(position)!=null)
            bindViewHolder((V) holder,position,getItem(position));
    }

    protected void bindViewHolder(V holder, int position, T item) {
        holder.bind(position,item);
    }

    private void onBindLoadingViewHolder(LoadingViewHolder holder) {
        holder.progressBar.setIndeterminate(true);
        holder.progressBar.setVisibility(isLoading? View.VISIBLE: View.GONE);
    }

    public T getItem(int position) {
        return position<0||list.size()==0||position>=list.size()?null:list.get(position);
    }

    @Override
    public int getItemCount() {
        int count = list==null?0:list.size();
        if(hasFooter)
            count++;
        if(shouldLoadMore)
            count++;
        return count;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    public List<T> getList() {
        return list;
    }



    public void setList(List<T> items) {
        setList(items,true);
    }

    public boolean isItemVisible(LinearLayoutManager layoutManager, int i) {
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        return i>=firstVisibleItem&&i<=lastVisibleItem;
    }

    public boolean isItemVisible(int firstVisibleItem,int lastVisibleItem, int i) {
        return i>=firstVisibleItem&&i<=lastVisibleItem;
    }

    /**
     * Return true if item was added, and return false if item was changed
     * @param item
     * @return
     */
    public boolean addOrUpdate(T item,boolean notify) {
        int index = list.indexOf(item);
        if(index==-1){
            list.add(item);
            if(notify)
                notifyItemInserted(list.size()-1);
        }else{
            list.set(index,item);
            if(notify)
                notifyItemChanged(index);
        }
        return index!=-1;
    }

    public boolean addOrUpdate(T item) {
        return addOrUpdate(item,true);
    }

    public void addAll(List<T> newItems) {
        int itemCount = list.size();
        list.addAll(newItems);
        notifyItemRangeInserted(itemCount,newItems.size());
    }

    public void remove(T item) {
        int index = list.indexOf(item);
        if(index!=-1) {
            list.remove(index);
            notifyItemRemoved(index);
        }
    }

    public T getLastItem() {
        return list!=null&&!list.isEmpty()?list.get(list.size()-1):null;
    }

    public void updateItem(T value) {
        int index = list.indexOf(value);
        if(index!=-1){
            updateItem(value,index);
        }
    }

    public void updateItem(T item, int i) {
        list.set(i,item);
        notifyItemChanged(i);
    }

    public void updateAll(List<T> items) {
        for (T item : items) {
            updateItem(item);
        }
    }

    public void setOnLongClickListener(OnItemLongClickListener<T> longClickListener) {
        this.longClickListener=longClickListener;
    }

    public static abstract class OnItemLongClickListener<T>{
        public void onItemLongClick(RecyclerView.ViewHolder holder, int adapterPosition, T item){
            onItemLongClick(adapterPosition,item);
        }

        public abstract void onItemLongClick(int adapterPosition, T item);
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar= itemView.findViewById(R.id.progressBar);
        }
    }
}