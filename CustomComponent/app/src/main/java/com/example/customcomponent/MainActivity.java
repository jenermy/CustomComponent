package com.example.customcomponent;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditTextWithClear editTextWithClear;
    private Button popupBtn;
    private MyPopupWindow myPopupWindow;
    private TextView timeConsumingTv;
    private ListView listView;
    private ViewStub viewStub;
    private Button refreshBtn;
    private ListViewAdapter adapter;
    private PullToRefreshListView pullToRefreshListView;
    private PullToRefreshExpandableListView pullToRefreshExpandableListView;
    private static long sumConsumingTime = 0; //Adapter绘制用时
    private GetData getData;
    private ExpandableListView expandableListView;
    private ExpandableAdapter expandableAdapter;
    String[] parentList = new String[100];
    ArrayList<String> childOne;
    ArrayList<String> childTwo;
    ArrayList<String> childThree;
    ArrayList<String> childFour;
    Map<String,ArrayList<String>> dataSet;
    int parentCount = 0;
    private View noDataView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextWithClear = (EditTextWithClear)findViewById(R.id.editTextWithClear);
        editTextWithClear.setIconIvBackground(android.R.drawable.ic_menu_upload_you_tube);
        popupBtn = (Button)findViewById(R.id.popupBtn);
        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPopupWindow = new MyPopupWindow(MainActivity.this);
                myPopupWindow.setMaxValue(2020);
                myPopupWindow.setMinValue(1957);
                Calendar calendar = Calendar.getInstance();
                myPopupWindow.setValue(calendar.get(Calendar.YEAR));
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.6f;
                getWindow().setAttributes(params);
                myPopupWindow.showAtLocation(popupBtn, Gravity.BOTTOM,0,0);
                myPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                    }
                });
            }
        });
        initData();
        initList();
    }

    private void initList(){
        refreshBtn = (Button)findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childFour = new ArrayList<>();
                childFour.add("just like this");
                childFour.add("take me hand");
                childFour.add("shape of you");
                parentList[parentCount++] = "bboom";
                dataSet.put(parentList[parentCount -1],childFour);
                childThree.add("体面");
                //childview和parentview都有刷新
                expandableAdapter.notifyDataSetChanged();
            }
        });
        timeConsumingTv = (TextView)findViewById(R.id.timeConsumingTv);
        listView = (ListView)findViewById(R.id.listView);
        viewStub = (ViewStub)findViewById(R.id.viewStub);
        adapter = new ListViewAdapter(MainActivity.this);
//        listView.setAdapter(adapter);
        pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.pullToRefreshListView);
        pullToRefreshListView.setAdapter(adapter);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData = new GetData(pullToRefreshListView);
                getData.execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData = new GetData(pullToRefreshListView);
                getData.execute();
            }
        });
        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(MainActivity.this,"已经到底啦",Toast.LENGTH_LONG).show();
            }
        });
        expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);
        expandableAdapter = new ExpandableAdapter(MainActivity.this,dataSet);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Toast.makeText(MainActivity.this,"i="+i+":i1="+i1,Toast.LENGTH_LONG).show();
                return true;
            }
        });
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if((int)view.getTag(R.layout.activity_main) == -1){
                    Toast.makeText(MainActivity.this,"父项的第"+view.getTag(R.layout.item_layout)+"项被选中",Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(MainActivity.this,"父项的第"+view.getTag(R.layout.item_layout)+"项的子项的第"+view.getTag(R.layout.activity_main)+"项被选中",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        pullToRefreshExpandableListView = (PullToRefreshExpandableListView)findViewById(R.id.pullToRefreshExpandableListView);
       if(noDataView == null) {
           noDataView = viewStub.inflate();
       }
        pullToRefreshExpandableListView.setEmptyView(noDataView);
       pullToRefreshExpandableListView.setMode(PullToRefreshBase.Mode.BOTH);
        ExpandableListView expandableListView = pullToRefreshExpandableListView.getRefreshableView();
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.setGroupIndicator(null);
        expandableListView.setDivider(null);
        expandableListView.setChildDivider(null);
        pullToRefreshExpandableListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {

            }
        });
    }
    private void initData(){
        dataSet = new HashMap<>();
        parentList[0] = "chinese";
        parentList[1] = "korea";
        parentList[2] = "american";
        parentCount = 3;
        childOne = new ArrayList<>();
        childTwo = new ArrayList<>();
        childThree = new ArrayList<>();
        childOne.add("王俊凯");
        childOne.add("白敬亭");
        childOne.add("刘昊然");
        childOne.add("李易峰");
        childTwo.add("tara");
        childTwo.add("bigbang");
        childTwo.add("boom");
        childThree.add("说散就散");
        childThree.add("空空如也");
        dataSet.put(parentList[0],childOne);
        dataSet.put(parentList[1],childTwo);
        dataSet.put(parentList[2],childThree);
    }


    class ExpandableAdapter extends BaseExpandableListAdapter{
        private Context mContext;
        private Map<String,ArrayList<String>> mDataSet;
        public ExpandableAdapter(Context context,Map<String,ArrayList<String>> dataSet){
            this.mContext = context;
            this.mDataSet = dataSet;
        }
        @Override
        public int getChildrenCount(int i) {
            return mDataSet.get(parentList[i]).size();
        }
        @Override
        public Object getChild(int i, int i1) {
            return mDataSet.get(parentList[i]).get(i1);
        }

        @Override
        public int getGroupCount() {
            return mDataSet.size();
        }

        @Override
        public Object getGroup(int i) {
            return mDataSet.get(parentList[i]);
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            if(view == null) {
                TextView textView = new TextView(mContext);
                view = textView;
            }
            //设置tag可以区分开子项和父项，以便设置各自的点击事件
            view.setTag(R.layout.activity_main,i1);
            view.setTag(R.layout.item_layout,i);
            if(view instanceof TextView){
                ((TextView) view).setText((String)getChild(i,i1));
            }
            return view;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if(view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.item_layout,null);
            }
            //设置tag可以区分开子项和父项，以便设置各自的点击事件
            view.setTag(R.layout.activity_main,-1);
            view.setTag(R.layout.item_layout,i);
            TextView textView = (TextView)view.findViewById(R.id.contentTv);
            textView.setText(parentList[i]);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

    class GetData extends AsyncTask<Void,Void,String>{
        PullToRefreshListView pullToRefreshListView;
        public GetData(PullToRefreshListView pullToRefreshListView){
            this.pullToRefreshListView =pullToRefreshListView;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            }catch (Exception e){
                e.printStackTrace();
            }
            return "说散就散";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            timeConsumingTv.setText(s);
            pullToRefreshListView.onRefreshComplete();
        }
    }

    public  class ListViewAdapter extends BaseAdapter{
        private Context mContext;
        public ListViewAdapter(Context context){
            this.mContext = context;
        }
        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            long startTime = System.nanoTime();
            if(view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.item_layout,null);
                holder = new ViewHolder(view);
            }else{
                holder = (ViewHolder)view.getTag();
            }
            if(holder != null){
                holder.contentTv.setText(i+"");
            }
//            long endTime = System.nanoTime();
//            sumConsumingTime = sumConsumingTime + endTime - startTime;
//            timeConsumingTv.setText(sumConsumingTime/1000000+"");
            return view;
        }
        //内部类里面不能声明静态类和静态变量，除非内部类是静态类
//        static  class ViewHolder{
//            ImageView pictureIv;
//            TextView contentTv;
//            public ViewHolder(View view){
//                pictureIv = (ImageView)view.findViewById(R.id.pictureIv);
//                contentTv = (TextView)view.findViewById(R.id.contentTv);
//                view.setTag(this);
//            }
//        }
    }
    static  class ViewHolder{
        ImageView pictureIv;
        TextView contentTv;
        public ViewHolder(View view){
            pictureIv = (ImageView)view.findViewById(R.id.pictureIv);
            contentTv = (TextView)view.findViewById(R.id.contentTv);
            view.setTag(this);
        }
    }
}
