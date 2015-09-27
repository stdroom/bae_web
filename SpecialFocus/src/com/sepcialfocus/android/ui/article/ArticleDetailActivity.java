/**
 * 工程名: MainActivity
 * 文件名: ArticleDetailActivity.java
 * 包名: com.sepcialfocus.android.ui.article
 * 日期: 2015-9-4下午8:10:44
 * Copyright (c) 2015, 北京小马过河教育科技有限公司 All Rights Reserved.
 * http://www.xiaoma.com/
 * Mail: leixun@xiaoma.cn
 * QQ: 378640336
 *
*/

package com.sepcialfocus.android.ui.article;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mike.aframe.utils.Regexp;
import com.sepcialfocus.android.BaseFragmentActivity;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.ArticleItemBean;
import com.sepcialfocus.android.configs.AppConstant;
import com.sepcialfocus.android.configs.URLs;

/**
 * 类名: ArticleDetailActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2015-9-4 下午8:10:44 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class ArticleDetailActivity extends BaseFragmentActivity
	implements View.OnClickListener{
	
	
	private String urls = "";
	private ArticleItemBean mArticleBean;
	private TextView mArticleTitleTv;
	private TextView mArticlePostmetaTv;
	private TextView mArticleContentTv;
	private String mArticleContentStr = "";
	private String mArticlePostmetaStr = "";
	ImageView mBackImg;
	private LinearLayout mContentLL;
	private WebView mWebView;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_article_detail);
		mArticleBean = (ArticleItemBean)getIntent().getSerializableExtra("key");
		initView();
		mArticleTitleTv.setText(mArticleBean.getTitle());
		urls = URLs.HOST+mArticleBean.getUrl();
		setLoadingVisible(true);
		mContentLL.setVisibility(View.GONE);
		new Loadhtml(urls).execute("","","");
	}
	
	protected void initView(){
		super.initView();
		mBackImg = (ImageView)findViewById(R.id.bottom_back);
		mBackImg.setOnClickListener(this);
		mContentLL = (LinearLayout)findViewById(R.id.content_ll);
		mArticleTitleTv = (TextView)findViewById(R.id.article_title);
		mArticlePostmetaTv = (TextView)findViewById(R.id.article_postmeta);
		mArticleContentTv = (TextView)findViewById(R.id.article_content);
		mWebView = (WebView)findViewById(R.id.article_web);
		mWebView.setBackgroundColor(0);
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
                CharSequence charSequence = null;
            	doc = Jsoup.connect(urls).timeout(5000).get();
                 Document content = Jsoup.parse(doc.toString());
                 mArticleContentStr = parseArticleContent(content);
                 mArticlePostmetaStr = parsePostMeta(content);
                 return mArticleContentStr;
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
            setLoadingVisible(false);
            mContentLL.setVisibility(View.VISIBLE);
//            Log.d("doc", doc.toString().trim());
            mArticleContentTv.setText(result);
            mArticlePostmetaTv.setText(Html.fromHtml(mArticlePostmetaStr));
            mWebView.getSettings().setJavaScriptEnabled(false);  
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.setBackgroundColor(0);
            mWebView.loadData(mArticleContentStr, "text/html; charset=UTF-8", "utf-8");
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        
    }
	
	private String parsePostMeta(Document content){
		Element article = content.getElementById("text");
		Elements articles = content.getElementsByClass("postmeta");
		articles.select("script").remove();
        if(article!=null){
	        return articles.toString();
        }else{
        	Elements contents = content.getElementsByClass("info");
        	contents.select("script").remove();
        	return contents.toString();
        }
	}
	
	/*
	 * 解析内容
	 */
	private String parseArticleContent(Document content){
		//　去掉广告
		content.getElementById("hr336").remove();
		// 批量处理img标签 链接地址、宽高设置
		Elements pngs = content.select("img[src]");  
        for (Element element : pngs) {  
            String imgUrl = element.attr("src");
            String imgWidth = element.attr("style").trim();
            int width = Regexp.getStringWidth(imgWidth);
            int height = Regexp.getStringHeight(imgWidth);
            if(width > AppConstant.WEBVIEW_WIDTH && height > 0){
            	height = height*AppConstant.WEBVIEW_WIDTH/width;
            	width = AppConstant.WEBVIEW_WIDTH;
            }
            if(width>0 && height>0){
            	element.attr("style", "width:"+width+"px; height:"+height+"px;");
            }
            if (imgUrl.trim().startsWith("/")) {  
                imgUrl =URLs.HOST + imgUrl;  
                element.attr("src", imgUrl);
            }  
        }  
        
        Element article = content.getElementById("text");
        if(article!=null){
	        return article.toString();
        }else{
        	Elements contents = content.getElementsByClass("content");
        	return contents.toString();
        }
	}

	@Override
	public void onClick(View arg0) {
		
		switch(arg0.getId()){
		case R.id.bottom_back: 
			finish();
			break;
		}
	}
	
}

