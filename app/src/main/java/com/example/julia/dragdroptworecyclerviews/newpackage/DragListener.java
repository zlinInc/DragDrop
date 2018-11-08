package com.example.julia.dragdroptworecyclerviews.newpackage;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.View;

import com.example.julia.dragdroptworecyclerviews.R;

import java.util.List;

/**
 * 方法描述:View提供的拖动监听
 **/
public class DragListener implements View.OnDragListener {

    private boolean isDropped = false;
    private Listener listener;
//    private boolean hasOutsideRecyclerView = false;

    DragListener(Listener listener) {
        this.listener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:  //开始拖拽
//                if (hasOutsideRecyclerView) {
//
//                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:  //离开了当前view

                break;
            case DragEvent.ACTION_DRAG_ENTERED:
//                if (hasOutsideRecyclerView) {
//
//                }
                break;
            case DragEvent.ACTION_DROP://放下的时候 会判断拖到哪个位置了，通过resId来判断
                isDropped = true;
                int positionTarget = -1;

                View viewSource = (View) event.getLocalState();
                int viewId = v.getId();//这个是最后被影响的的view：经过打印是可以是recyclerview 也可以是item    event是被拖动的view
                final int flItem = R.id.frame_layout_item;
                final int tvEmptyListTop = R.id.tvEmptyListTop;
                final int tvEmptyListBottom = R.id.tvEmptyListBottom;
                final int rvTop = R.id.rvTop;
                final int rvBottom = R.id.rvBottom;

                RecyclerView target;
                switch (viewId) {
                    case tvEmptyListTop:
                    case rvTop:
                        target = (RecyclerView) v.getRootView().findViewById(rvTop);
                        break;
                    case tvEmptyListBottom:
                    case rvBottom:
                        target = (RecyclerView) v.getRootView().findViewById(rvBottom);
                        break;
                    default:
                        //自己内部拖动的切换位置时候走这里
                        target = (RecyclerView) v.getParent();
                        positionTarget = (int) v.getTag();
                }
                if (viewSource != null) {
                    RecyclerView source = (RecyclerView) viewSource.getParent();

                    ListAdapter adapterSource = (ListAdapter) source.getAdapter();
                    int positionSource = (int) viewSource.getTag();
                    int sourceId = source.getId();

                    Integer list = adapterSource.getList().get(positionSource);
                    List<Integer> listSource = adapterSource.getList();
                    //这里的删除原本没有if条件 后边是为了添加 拖上去的时候不删除下边的 只是复制一个上去，做的限制
                    if (((RecyclerView) viewSource.getParent()).getId() != rvBottom || target.getId() != rvTop) {//不让底部拖上去的时候底部被删除
                        listSource.remove(positionSource);// 能删除才能换位置
                    }
                    adapterSource.updateList(listSource);
                    adapterSource.notifyDataSetChanged();
                    //接受方进行的动作： 通过获取adapter来修改list 从而重新加载
                    ListAdapter adapterTarget = (ListAdapter) target.getAdapter();
                    List<Integer> customListTarget = adapterTarget.getList();
                    if (positionTarget >= 0) {
                        customListTarget.add(positionTarget, list);
                    } else {
                        customListTarget.add(list);
                    }
                    adapterTarget.updateList(customListTarget);
                    adapterTarget.notifyDataSetChanged();
                    //以下不用管 是之前显示拖光item的时候进行显示的
                    if (sourceId == rvBottom && adapterSource.getItemCount() < 1) {
                        listener.setEmptyListBottom(true);
                    }
                    if (viewId == tvEmptyListBottom) {
                        listener.setEmptyListBottom(false);
                    }
                    if (sourceId == rvTop && adapterSource.getItemCount() < 1) {
                        listener.setEmptyListTop(true);
                    }
                    if (viewId == tvEmptyListTop) {
                        listener.setEmptyListTop(false);
                    }
                }
                break;
        }

        if (!isDropped && event.getLocalState() != null) {
            ((View) event.getLocalState()).setVisibility(View.VISIBLE);
        }
        return true;
    }

//    public boolean isHasOutsideRecyclerView() {
//        return hasOutsideRecyclerView;
//    }
//
//    public void setHasOutsideRecyclerView(boolean hasOutsideRecyclerView) {
//        this.hasOutsideRecyclerView = hasOutsideRecyclerView;
//    }

}