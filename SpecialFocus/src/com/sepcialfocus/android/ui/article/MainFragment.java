/**
 * ������: SpecialFocus
 * �ļ���: MainFragment.java
 * ����: com.sepcialfocus.android.ui.article
 * ����: 2015-9-1����9:42:39
 * Copyright (c) 2015, ����С����ӽ����Ƽ����޹�˾ All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.mike.aframe.MKLog;
import com.mike.aframe.database.KJDB;
import com.mike.aframe.utils.DensityUtils;
import com.mike.aframe.utils.MD5Utils;
import com.mike.aframe.utils.PreferenceHelper;
import com.sepcialfocus.android.BaseApplication;
import com.sepcialfocus.android.BaseFragment;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.ArticleItemBean;
import com.sepcialfocus.android.bean.PagesInfo;
import com.sepcialfocus.android.bean.RollImageBean;
import com.sepcialfocus.android.configs.AppConstant;
import com.sepcialfocus.android.parse.specialfocus.ArticleItemListParse;
import com.sepcialfocus.android.parse.specialfocus.ArticleItemPagesParse;
import com.sepcialfocus.android.ui.adapter.ArticleListAdapter;
import com.sepcialfocus.android.ui.article.ArticleFragment.Loadhtml;
import com.sepcialfocus.android.ui.widget.GuideGallery;
import com.sepcialfocus.android.ui.widget.ImageAdapter;
import com.sepcialfocus.android.utils.SettingsManager;
import com.sepcialfocus.android.widgets.swiptlistview.SwipeListView;

/**
 * 类名: MainFragment <br/>
 * 功能描述: TODO 添加功能描述. <br/>
 * 日期: 2015年9月2日 上午10:27:39 <br/>
 *
 * @author leixun
 * @version 
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
	
	private ArrayList<RollImageBean> images;
	int gallerypisition = 0;
	Timer autoGallery = new Timer();
	private GuideGallery mGallery;
	LinearLayout pointLinear;
	private int position = 0;
	private Thread timeThread = null;
	public boolean timeFlag = true;
	private boolean isExit = false;
	public ImageTimerTask timeTaks = null;
	public ImageAdapter imageAdapter;

	private ArrayList<ArticleItemBean> mArticleList;
	private SwipeRefreshLayout mSwipeLayout;
	private SwipeListView mArticle_listview;
	private ArticleListAdapter mArticleAdapter;
	private View mView;
	private View mHeadView;
	private Activity mActivity;
	private String urls = "";
	
	boolean isPullRrefreshFlag;
	String nextUrl;
	private KJDB kjDb = null;
	
	
	private boolean isRefresh = false;
	private boolean isPullFlag = false;
	public MainFragment(){
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mActivity = getActivity();
		kjDb = KJDB.create(mActivity);
		Bundle args = getArguments();
        if (null !=  args) {
            if (args.containsKey("key")) {
                this.urls = args.getString("key");
            }
        }
        try{
        	mArticleList = (ArrayList<ArticleItemBean>)
        			BaseApplication.globalContext.readObject(MD5Utils.md5(urls));
        	nextUrl = PreferenceHelper.readString(getActivity(), 
        			AppConstant.URL_NEXT_PAGE_FILE, MD5Utils.md5(urls));
        	if(nextUrl!=null && !"".equals(nextUrl)){
        		isPullRrefreshFlag = true;
        	}
        	images = (ArrayList<RollImageBean>)BaseApplication.globalContext.readObject("rollImgs");
        	if(mArticleList==null){
        		mArticleList = new ArrayList<ArticleItemBean>();
            }
        }catch(Exception e){
        	e.printStackTrace();
        	mArticleList = new ArrayList<ArticleItemBean>();
        }
	}
	
	@Override
	protected void initView() {
		mGallery = (GuideGallery)mHeadView.findViewById(R.id.index_gallery);
		pointLinear = (LinearLayout)mHeadView.findViewById(R.id.gallery_point_linear);
		mLoadingLayout = (RelativeLayout)mView.findViewById(R.id.layout_loading_bar);
		mArticle_listview = (SwipeListView)mView.findViewById(R.id.article_listview);
		mSwipeLayout = (SwipeRefreshLayout)mView.findViewById(R.id.swipe_container);
		mArticle_listview = (SwipeListView)mView.findViewById(R.id.article_listview);
		initSwapLayout();
		if(mArticle_listview.getHeaderViewsCount()<1){
			mArticle_listview.addHeaderView(mHeadView);
		}
		mArticle_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mArticleList!=null && mArticleList.size() >= position){
					Intent intent = new Intent(mActivity,ArticleDetailActivity.class);
					intent.putExtra("key", mArticleList.get(position-1));
					startActivity(intent);
				}
			}
		});
		mArticle_listview.setOnBottomListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isPullFlag && isPullRrefreshFlag && !isRefresh){
					isPullFlag = true;
					new Loadhtml(urls+nextUrl).execute("","","");
				} else {
					isPullFlag = false;
					mArticle_listview.onBottomComplete();
				}
			}
		});
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		mView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_main, null);
		mHeadView = LayoutInflater.from(mActivity).inflate(R.layout.layout_roll_img,null);
		initView();
		mArticleAdapter = new ArticleListAdapter(mActivity, mArticleList);
		mArticle_listview.setAdapter(mArticleAdapter);
		initData();
		return mView;
	}
	
	private void initData(){
		if(null==mArticleList || mArticleList.size()==0){
			isRefresh = true;
			new Loadhtml(urls+"/index_1.html").execute("","","");
		}
		if(images!=null){
			getRollImages(images);
		}
	}
	


	@Override
	public void onPause() {
		
		super.onPause();
		if(timeTaks!=null){
			timeTaks.timeCondition = false;
		}
		BaseApplication.globalContext.saveObject(mArticleList, MD5Utils.md5(urls));
		BaseApplication.globalContext.saveObject(images, "rollImgs");
		PreferenceHelper.write(getActivity(), 
				AppConstant.URL_NEXT_PAGE_FILE, MD5Utils.md5(urls),nextUrl);
	}

	@Override
	public void onResume() {
		super.onResume();
		timeFlag = false;
	}

	public void getRollImages(ArrayList<RollImageBean> json){
		images = json;
		if (images!=null && images.size() > 0){
			Message msg = new Message();
			 msg.what = 2;//消息标识
             autoGalleryHandler.sendMessage(msg);
		} else {
			MKLog.d("MainFragment", "get RollImageBean is null !!!");
		}
	}


	class Loadhtml extends AsyncTask<String, String, String>
    {
        Document doc;
        String urls = "";
        public Loadhtml(String urls){
        	this.urls = urls;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                if("".equals(urls)){
                	return null;
                }
            	doc = Jsoup.connect(urls).timeout(5000).get();
                 Document content = Jsoup.parse(doc.toString());
                 if(!isPullFlag){
            		 parseRoolImg(content);
            	 }
                 if(isRefresh){
                	 if(mArticleList.size() == 0){
                		 PagesInfo info = ArticleItemPagesParse.getPagesInfo(urls, content);
                    	 isPullRrefreshFlag = info.getHasNextPage();
                    	 nextUrl = info.getNextPageUrl();
                		 mArticleList.addAll(0, ArticleItemListParse.getArticleItemList(kjDb, content,isRefresh));
                	 }else{
                		 mArticleList.addAll(0, ArticleItemListParse.getArticleItemList(kjDb, content));
                	 }
                 }else{
                	 PagesInfo info = ArticleItemPagesParse.getPagesInfo(urls, content);
                	 isPullRrefreshFlag = info.getHasNextPage();
                	 nextUrl = info.getNextPageUrl();
                	 if(mArticleList.size() == 0){
                		 mArticleList.addAll(ArticleItemListParse.getArticleItemList(kjDb, content,true));
                	 }else{
                		 mArticleList.addAll(ArticleItemListParse.getArticleItemList(kjDb, content));
                	 }
                 }
            } catch (IOException e) {
            	mArticle_listview.onBottomComplete();
                mSwipeLayout.setRefreshing(false);
                isRefresh = false;
            	isPullFlag = false;
                e.printStackTrace();
            } catch(Exception e){
            	mArticle_listview.onBottomComplete();
                mSwipeLayout.setRefreshing(false);
                isRefresh = false;
            	isPullFlag = false;
            	e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//            Log.d("doc", doc.toString().trim());
            setLoadingVisible(false);
            mSwipeLayout.setVisibility(View.VISIBLE);
            mArticleAdapter.notifyDataSetChanged();
            mArticle_listview.onBottomComplete();
        	isRefresh = false;
        	isPullFlag = false;
            mSwipeLayout.setRefreshing(false);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if(mArticleList!=null 
            		&& mArticleList.size()==0){
            	setLoadingVisible(true);
            	mSwipeLayout.setVisibility(View.GONE);
            }
        }
        
    }
	
	final Handler autoGalleryHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
	            switch (message.what) {
	                case 1:
	                	mGallery.setSelection(message.getData().getInt("pos"));
	                    break;
	                case 2:
	                	initGalery();
	                	startGalery();
	                	break;
	            }
            }
    }; 

	public class ImageTimerTask extends TimerTask{
    	public volatile boolean timeCondition = true;
       // int gallerypisition = 0;
        public void run() {
            synchronized (this) {
                while(!timeCondition) {
                    try {
                    	Thread.sleep(100);
                        wait();
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                }
            }
            try {
            	gallerypisition = mGallery.getSelectedItemPosition()+1;
                Message msg = new Message();
                Bundle date = new Bundle();// 存放数据
                date.putInt("pos", gallerypisition);
                msg.setData(date);
                msg.what = 1;//消息标识
                autoGalleryHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public void changePointView(int cur){
    	
    	for (int i = 0 ; i < pointLinear.getChildCount() ; i++){
    		View view = pointLinear.getChildAt(i);
    		if( i == cur){
    			view.setBackgroundResource(R.drawable.feature_point_cur);
    		} else{
    			view.setBackgroundResource(R.drawable.feature_point);
    		}
    	}
    	position = cur;
    }

	
	public void initGalery(){
        mGallery.setImageActivity(this);

        imageAdapter = new ImageAdapter(images,mActivity,this,
        		DensityUtils.getScreenH(mActivity),mGallery.getHeight());  
        mGallery.setAdapter(imageAdapter);  
        pointLinear.removeAllViewsInLayout();
        for (int i = 0; i < images.size(); i++) {
        	ImageView pointView = new ImageView(mActivity);
        	if(i==0){
        		pointView.setBackgroundResource(R.drawable.feature_point_cur);
        	}else
        		pointView.setBackgroundResource(R.drawable.feature_point);
        	pointLinear.addView(pointView);
		}
        mGallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println(arg2+"arg2");
				/*switch (arg2) {
				case 0:
					 uri = Uri.parse("http://www.36939.net/");
					intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
					
					break;
				}*/
				
			}
		});
	}
	
	public void startGalery(){
		
		timeTaks = new ImageTimerTask();
    	autoGallery.scheduleAtFixedRate(timeTaks, 2000, 2000);
    	timeThread = new Thread() {
            public void run() {
                while(!isExit) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    synchronized (timeTaks) {
                    	if(!timeFlag){
                    		timeTaks.timeCondition = true;
                    		timeTaks.notifyAll();
                        }
                    }
                    timeFlag = true;
                }
            };
        };
        timeThread.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void initSwapLayout(){
		mSwipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
		mSwipeLayout.setOnRefreshListener(this);
		
		 SettingsManager settings = SettingsManager.getInstance();
		 mArticle_listview.setSwipeMode(SwipeListView.SWIPE_MODE_NONE);
		 mArticle_listview.setSwipeActionLeft(settings.getSwipeActionLeft());
		 mArticle_listview.setSwipeActionRight(settings.getSwipeActionRight());
		 mArticle_listview.setOffsetLeft(DensityUtils.dip2px(getActivity(),
	                settings.getSwipeOffsetLeft()));
		 mArticle_listview.setOffsetRight(DensityUtils.dip2px(getActivity(),
	                settings.getSwipeOffsetRight()));
		 mArticle_listview.setAnimationTime(settings.getSwipeAnimationTime());
		 mArticle_listview.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
	}

	@Override
	public void onRefresh() {
		isRefresh = true;
		new Loadhtml(urls+"/index_1.html").execute("","","");
	}
	
	private void parseRoolImg(Document content){
		 Element rollImg = content.getElementById("pic");
         Elements rollImgChild= rollImg.children();
         ArrayList<RollImageBean> rollList = new ArrayList<RollImageBean>();
         for(Element bean : rollImgChild){
        	 Elements ele = bean.getElementsByTag("a");
        	 for(Element bean_ele:ele){
        		 if(bean_ele.hasAttr("href") && bean_ele.hasAttr("title")){
        			 RollImageBean item = new RollImageBean();
        			 item.setImgLinkUrl(bean_ele.attr("href"));
        			 item.setTitle(bean_ele.attr("title"));
        			 Elements imgs = bean_ele.children();
        			 if(imgs!=null && imgs.size()>0){
        				 Element img = imgs.get(0);
        				 if(img!=null && img.hasAttr("src")){
        					 item.setImgUrl(img.attr("src"));
        					 rollList.add(item);
        				 }
        			 }
        		 }
        	 }
         }
         getRollImages(rollList);
	}
}

