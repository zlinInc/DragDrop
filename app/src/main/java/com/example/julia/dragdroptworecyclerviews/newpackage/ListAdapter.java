package com.example.julia.dragdroptworecyclerviews.newpackage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.example.julia.dragdroptworecyclerviews.R;
import com.example.julia.dragdroptworecyclerviews.util.DisplayUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<Integer> list;
    private Listener listener;
    private boolean hasDrop = false;
    private int width = 100;
    private int tag = -1;
    private Context context;

    ListAdapter(List<Integer> list, Listener listener, int tag) {
        this.list = list;
        this.listener = listener;
        this.tag = tag;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {

        if (tag == 2) {

            int px = DisplayUtil.dp2px(context, width);
            int height = DisplayUtil.dp2px(context, 100);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(px, height);
            holder.frameLayout.setLayoutParams(layoutParams);
        }
        holder.text.setImageResource(list.get(position));
        holder.frameLayout.setTag(position);

        if (mOnItemClickLitener != null) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.frameLayout, pos);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    List<Integer> getList() {
        return list;
    }

    void updateList(List<Integer> list) {
        this.list = list;
    }


    void updateWidth(int wid) {
        this.width = wid;
    }

    DragListener getDragInstance() {
        if (listener != null) {
            DragListener dragListener = new DragListener(listener);
//            dragListener.setHasOutsideRecyclerView(hasDrop);//设置外部不能拖动
            return dragListener;
        } else {
            Log.e("ListAdapter", "Listener wasn't initialized!");
            return null;
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        ImageView text;
        @BindView(R.id.frame_layout_item)
        RelativeLayout frameLayout;

        ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void setHasDrop(boolean hasDrop) {
        this.hasDrop = hasDrop;
    }
}
