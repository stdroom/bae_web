package com.sepcialfocus.android.ui.widget;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.mike.aframe.MKLog;
import com.mike.aframe.bitmap.KJBitmap;
import com.sepcialfocus.android.R;
import com.sepcialfocus.android.bean.RollImageBean;
import com.sepcialfocus.android.configs.URLs;
import com.sepcialfocus.android.ui.article.MainFragment;

public class ImageAdapter extends BaseAdapter{  
    private Context context;  
    private MainFragment index;
    
    KJBitmap kjBitmap;
    int width;
    int height;
   
   public static ArrayList<RollImageBean> images;
   
   static class ViewHolder {
	   ImageView imageView;
   }
    
    public ImageAdapter(ArrayList<RollImageBean> images,Context context,
    		MainFragment index,int width,int height) {  
    	this.images = images;
    	this.index = index;
        this.context = context;  
        kjBitmap = KJBitmap.create();
        KJBitmap.config.width = width;
        KJBitmap.config.height = height;
        this.width = width;
        this.height = height;
    }  
  
    public int getCount() {  
        return Integer.MAX_VALUE;  
    }  
  
    public Object getItem(int position) {  
        return images.get(position%images.size());
    }  
   
    public long getItemId(int position) {  
        return position;  
    }  
  
    public View getView(int position, View convertView, ViewGroup parent) {  
    	ViewHolder holder = null;
        if(convertView==null){  
            convertView = LayoutInflater.from(context).inflate(R.layout.item,null); //实例化convertView  
            Gallery.LayoutParams params = new Gallery.LayoutParams(
            		Gallery.LayoutParams.MATCH_PARENT,Gallery.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(params);
            holder = new ViewHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.gallery_image);
            convertView.setTag(holder);
        }  
        holder = (ViewHolder)convertView.getTag();
        String url = images.get(position % images.size()).getImgUrl();
        kjBitmap.display(holder.imageView,URLs.HOST+url,width,height);

        if(index!=null){
        	((MainFragment)index).changePointView(position % images.size());
        } else {
        	MKLog.d("ImageAdapter", "IndexFragment  is null !!");
        }
        /* imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					//((String) imageView).setSpan(new URLSpan("http://www.36939.net/"), 13, 15,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);    


			}
		});*/
        return convertView;  
        
    }  
}  
