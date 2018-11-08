package com.example.julia.dragdroptworecyclerviews.newpackage;

import android.content.ClipData;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.julia.dragdroptworecyclerviews.R;
import com.example.julia.dragdroptworecyclerviews.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements Listener {

    @BindView(R.id.rvTop)
    RecyclerView rvTop;
    @BindView(R.id.rvBottom)
    RecyclerView rvBottom;
    @BindView(R.id.tvEmptyListTop)
    TextView tvEmptyListTop;
    @BindView(R.id.tvEmptyListBottom)
    TextView tvEmptyListBottom;
    List<Integer> topList;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    private List<Integer> list1 = new ArrayList<>();
    private List<Integer> list2 = new ArrayList<>();
    private List<Integer> list3 = new ArrayList<>();
    private List<Integer> list4 = new ArrayList<>();
    private List<Integer> list5 = new ArrayList<>();
    private ListAdapter bottomListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //以下是切换tab的假数据
        for (int i = 0; i < 2; i++) {
            list1.add(R.mipmap.pic_b);
        }
        for (int i = 0; i < 4; i++) {
            list2.add(R.mipmap.pic_g);
        }
        for (int i = 0; i < 3; i++) {
            list3.add(R.mipmap.pic_h);
        }
        for (int i = 0; i < 5; i++) {
            list4.add(R.mipmap.pic_i);
        }
        for (int i = 0; i < 1; i++) {
            list5.add(R.mipmap.pic_d);
        }
        selectTab(1);
        initTopRecyclerView();
        initBottomRecyclerView();

        tvEmptyListTop.setVisibility(View.GONE);
        tvEmptyListBottom.setVisibility(View.GONE);
    }

    private void initTopRecyclerView() {
        rvTop.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        //这个是toprecycview的假数据
        topList = new ArrayList<>();
        topList.add(R.mipmap.pic_a);
        topList.add(R.mipmap.pic_b);
        //tag：因为上下recycl使用的同一个适配器  但是下边需要设置不同大小的item，所以tag=2表示是下边的recyclerview 适配器才知道
        final ListAdapter topListAdapter = new ListAdapter(topList, this, 1);
        rvTop.setAdapter(topListAdapter);
        //下边是dragListener的设置：之前全部使用ItemTouchHelper来拖拽  后来发现rvBottom要拖上来还是必须让上部添加DragListener
        DragListener dragListener = topListAdapter.getDragInstance();
        rvTop.setOnDragListener(dragListener);
        //recyview分割线：
        rvTop.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL_LIST));
        //设置item的点击事件 进行删除操作
        topListAdapter.setOnItemClickLitener(new ListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {

                ImageView imageView = view.findViewById(R.id.img_edit);
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Integer> list = topListAdapter.getList();
                        list.remove(position);
                        topListAdapter.updateList(list);
                        topListAdapter.notifyDataSetChanged();
                    }
                });
            }

        });

        /**
         * ItemTouchHelper是recyclerview原生的拖拽工具类
         */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                int dragFlags;

                //可以设定是上下左右都能滑动 这里设置的只能左右滑动
//                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, 0);
            }

            //这个是拖动item切换位置使用的：
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(topList, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(topList, i, i - 1);
                    }
                }
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //侧滑删除可以使用
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            /**
             * 长按选中Item的时候开始调用
             * 长按高亮
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//                    viewHolder.itemView.setBackgroundColor(Color.RED);
//                    //获取系统震动服务//震动70毫秒
//                    Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
//                    vib.vibrate(70);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原高亮
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                topListAdapter.notifyDataSetChanged(); //完成拖动后刷新适配器，这样拖动后删除就不会错乱
            }
        });
        helper.attachToRecyclerView(rvTop);
    }

    //初始化下边的recyclerview
    private void initBottomRecyclerView() {
        rvBottom.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        //分割线
        rvBottom.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL_LIST));
        //底部假数据
        List<Integer> bottomList = new ArrayList<>();
        bottomList.add(R.mipmap.pic_c);
        bottomList.add(R.mipmap.pic_d);

        bottomListAdapter = new ListAdapter(bottomList, this, 2);
        bottomListAdapter.setHasDrop(true);
        rvBottom.setAdapter(bottomListAdapter);

        /**
         * 实例化ItemTouchHelper对象,然后添加到RecyclerView
         */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFrlg, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //侧滑删除可以使用；
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            /**
             * 长按选中Item的时候开始调用
             * 长按高亮
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//                    viewHolder.itemView.setBackgroundColor(Color.RED);
//                    //获取系统震动服务//震动70毫秒
//                    Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
//                    vib.vibrate(70);
                    View v = viewHolder.itemView;
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        v.startDragAndDrop(data, shadowBuilder, v, 0);
                    } else {
                        v.startDrag(data, shadowBuilder, v, 0);
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原高亮
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
            }
        });

        helper.attachToRecyclerView(rvBottom);
    }

    //这个是原始demo之前脱光之后 显示的，这里不要
    @Override
    public void setEmptyListTop(boolean visibility) {
        tvEmptyListTop.setVisibility(visibility ? View.VISIBLE : View.GONE);
        rvTop.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    //这个是原始demo之前脱光之后 显示的，这里不要
    @Override
    public void setEmptyListBottom(boolean visibility) {
        tvEmptyListBottom.setVisibility(visibility ? View.VISIBLE : View.GONE);
        rvBottom.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }
//以下是tab的点击事件

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                selectTab(1);
                //切换数据显示
                bottomListAdapter.updateList(list1);
                //设置不同item的宽高
                bottomListAdapter.updateWidth(170);
                bottomListAdapter.notifyDataSetChanged();
                break;
            case R.id.tv2:
                selectTab(2);
                bottomListAdapter.updateList(list2);
                bottomListAdapter.updateWidth(100);
                bottomListAdapter.notifyDataSetChanged();
                break;
            case R.id.tv3:
                selectTab(3);
                bottomListAdapter.updateList(list3);
                bottomListAdapter.updateWidth(130);
                bottomListAdapter.notifyDataSetChanged();
                break;
            case R.id.tv4:
                selectTab(4);
                bottomListAdapter.updateList(list4);
                bottomListAdapter.updateWidth(110);
                bottomListAdapter.notifyDataSetChanged();
                break;
            case R.id.tv5:
                selectTab(5);
                bottomListAdapter.updateList(list5);
                bottomListAdapter.updateWidth(180);
                bottomListAdapter.notifyDataSetChanged();


                break;
        }
    }

    private void selectTab(int index) {
        reInitTabs();
        switch (index) {
            case 1:
                tv1.setBackground(getResources().getDrawable(R.drawable.bg_copy_orange));
                break;
            case 2:
                tv2.setBackground(getResources().getDrawable(R.drawable.bg_copy_orange));
                break;
            case 3:
                tv3.setBackground(getResources().getDrawable(R.drawable.bg_copy_orange));
                break;
            case 4:
                tv4.setBackground(getResources().getDrawable(R.drawable.bg_copy_orange));
                break;
            case 5:
                tv5.setBackground(getResources().getDrawable(R.drawable.bg_copy_orange));
                break;

        }

    }

    private void reInitTabs() {
        tv1.setBackground(getResources().getDrawable(R.drawable.bg_copy));
        tv2.setBackground(getResources().getDrawable(R.drawable.bg_copy));
        tv3.setBackground(getResources().getDrawable(R.drawable.bg_copy));
        tv4.setBackground(getResources().getDrawable(R.drawable.bg_copy));
        tv5.setBackground(getResources().getDrawable(R.drawable.bg_copy));
    }
}
