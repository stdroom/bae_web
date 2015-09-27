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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.mike.aframe.MKLog;
import com.mike.aframe.database.KJDB;
import com.mike.aframe.utils.DensityUtils;
import com.mike.aframe.utils.MD5Utils;
import com.sepcialfocus.android.BaseApplication;
import com.sepcialfocus.android.BaseFragment;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.ArticleItemBean;
import com.sepcialfocus.android.bean.RollImageBean;
import com.sepcialfocus.android.ui.adapter.ArticleListAdapter;
import com.sepcialfocus.android.ui.adapter.MainPagerAdapter;
import com.sepcialfocus.android.ui.article.ArticleFragment.Loadhtml;
import com.sepcialfocus.android.ui.widget.GuideGallery;
import com.sepcialfocus.android.ui.widget.ImageAdapter;
import com.sepcialfocus.android.ui.widget.PullToRefreshView;

/**
 * 类名: MainFragment <br/>
 * 功能描述: TODO 添加功能描述. <br/>
 * 日期: 2015年9月2日 上午10:27:39 <br/>
 *
 * @author leixun
 * @version 
 */
public class MainFragment extends BaseFragment{
	
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
	private PullToRefreshView mArticle_pullview;
	private ListView mArticle_listview;
	private ArticleListAdapter mArticleAdapter;
	private View mView;
	private View mHeadView;
	private Activity mActivity;
	private String urls = "";
	
	boolean isPullRrefreshFlag;
	String nextUrl;
	private KJDB kjDb = null;
	
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
		mArticle_listview = (ListView)mView.findViewById(R.id.article_listview);
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
		mArticle_pullview = (PullToRefreshView)mView.findViewById(R.id.article_pullview);
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
			new Loadhtml(urls).execute("","","");
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
                 Element rollImg = content.getElementById("pic");
                 Element article = content.getElementById("article");
                 
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
                 Element nextPage = content.getElementById("pages");
                 Elements nextelement = nextPage.getElementsByTag("a");
                 isPullRrefreshFlag = false;
                 
                 Log.d("element", article.toString());
                 Elements elements = article.children();
                 for(Element linkss : elements)
                 {	 
                	 if(!linkss.hasAttr("class") || !"post".equals(linkss.attr("class"))){
                		 continue;
                	 }
                	 String title = "";
                 	 String imgUrl = "";
                 	 String summary = "";
                 	 String link = "";
                	 Elements titleImg = linkss.getElementsByTag("img");
                	 for(Element bean : titleImg){
                		 if(bean.hasAttr("alt")){	// 标题
                			 title = bean.attr("alt");
                		 }
                		 if(bean.hasAttr("src")){	// 图像地址
                			 imgUrl = bean.attr("src");
                		 }
                	 }
                	 
                	 Elements contentUrl = linkss.getElementsByClass("summary");
                	 for(Element bean:contentUrl){
                		 if(bean.hasAttr("class")){	// 内容摘要
                			 summary = bean.text();
                		 }
                		 Elements contentBean = bean.getElementsByTag("a");
                		 for(Element bean2:contentBean){
                			 if(bean2.hasAttr("href")){	// 跳转链接
                				 link = bean2.attr("href");
                			 }
                		 }
                	 }
                	 String time = "";
                	 ArrayList<String> tags = new ArrayList<String>();
                	 String tagUrl = "";
                	 Elements timeTag = linkss.getElementsByClass("postmeta");
                	 for(Element links: timeTag){
                		 Elements spans = links.getElementsByTag("span");
                		 for(Element bean:spans){
                			 if(bean.hasAttr("class")){
                				 if("left_author_span".equals(bean.attr("class"))){
                					 time = bean.text();
                					 continue;
                				 }
                				 if("left_tag_span".equals(bean.attr("class"))){
                					 Elements childen = bean.children();
                					 for(Element child:childen){
                						 if(child.hasAttr("href")){
                							 tagUrl = child.attr("href");
                							 tags.add(child.text());
                						 }
                					 }
                				 }
                			 }
                		 }
                		 
                		 
                	 }
                	 ArticleItemBean bean = new ArticleItemBean();
                	 bean.setTitle(title);
                	 bean.setDate(time);
                	 bean.setImgUrl(imgUrl);
                	 bean.setSummary(summary);
                	 bean.setUrl(link);
                	 bean.setTags(tags);
                	 bean.setMd5(MD5Utils.md5(link));
                	 ArticleItemBean selectBean = kjDb.findById(bean.getMd5(), ArticleItemBean.class);
                	 if(selectBean==null){
                		 mArticleList.add(bean);
                	 }
                 }
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
            mArticle_pullview.setVisibility(View.VISIBLE);
            mArticleAdapter.notifyDataSetChanged();
            mArticle_pullview.onFooterRefreshComplete();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if(mArticleList!=null 
            		&& mArticleList.size()==0){
            	setLoadingVisible(true);
            	mArticle_pullview.setVisibility(View.GONE);
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
		BaseApplication.globalContext.saveObject(mArticleList, MD5Utils.md5(urls));
		BaseApplication.globalContext.saveObject(images, "rollImgs");
	}
	
	
}

