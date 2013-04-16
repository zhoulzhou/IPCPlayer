
/**
 * Copyright (c) 2010 Baidu Inc.
 * 
 * @copyright	2010 Baidu Inc.
 * @version
 * Lyric :lyric decoding info for show lyric info.
 */
package com.example.ipcplayer.object;

import java.util.ArrayList;


public class LyricDecodingInfo {
	
	private int mTotalNum=0;
    public ArrayList<String> mSentencesList = new ArrayList<String>();
    public ArrayList<Long> mFromTimesList = new ArrayList<Long>();
    public ArrayList<Long> mToTimesList = new ArrayList<Long>();
    private int mCurrentIndex=-1;

    private OnLyricResetListener mListener;
    
    public void setOnLryicResetListener(OnLyricResetListener listener){
    	mListener = listener;
    }
    
	public LyricDecodingInfo()
	{
		reset();
	}
	
	public void  reset()
	{
		mSentencesList.clear();
		mFromTimesList.clear();
		mToTimesList.clear();
		
		mTotalNum=0;
		if(mListener != null){
			mListener.onReset();
		}
	}
	
	public boolean setInfo(String[]sens,long[] froms,long[] tos)
	{
		reset();
		
		if((sens==null)||(froms==null)||(tos==null))
			return false;
		
		if(sens.length==0)
			return false;
	
		//Log.d("LyricDecodingInfo","++++setInfo,length:"+sens.length);
		
		int len1=sens.length;
		int len2=froms.length;
		int len3=tos.length;
		
		if((len1!=len2)||(len1!=len3)||(len2!=len3))
		   return false;

		//Log.d("LyricDecodingInfo","++++setInfo,222222222:"+sens.length);

		for(int i=0;i<len1;i++)
		{
			mSentencesList.add(sens[i]);
		}
		for(int j=0;j<len1;j++)
		{
			mFromTimesList.add(froms[j]);
		}
		for(int k=0;k<len1;k++)
		{
			mToTimesList.add(tos[k]);
		}
		mTotalNum=len1;
		return true;
		
	}
	
    /**
     * 得到当前句子的索引
     * @return 当前句子的索引
     */
    public int getCurIndex()
    {
   	 
   	  return mCurrentIndex;
    }
    
    public boolean isLast()
    {
   	 if(mSentencesList.size()==0) return true;
   	 
   	 if((mCurrentIndex+1)>=mSentencesList.size())return true;
   	 
   	 return false;
   	 
    }
    
    public boolean isFirst()
    {
   	 if(mSentencesList.size()==0) return true;
   	 
   	 if(mCurrentIndex<=0)return true;
   	 
   	 return false;
   	 
    }
    
    /**
     * 得到这批歌词里面,最长的那一句索引
     * @return 最长的字串的索引
     */
    public int getMaxSentence() {
    
	 if(mSentencesList.size()==0) return 0;
	
        String temp="";
        String max=mSentencesList.get(0);
        int i=0;
        int maxIndex=i;
        for (String sen : mSentencesList) {
            if (temp.length() > max.length()) {
                max = sen;
                maxIndex=i;
            }
       	 i++;
        }
        return i;
    }
    
    /**
     * 得到这批歌词里面某行的内容
     * @return 某行歌词
     */
    public String getSentence(int index) {

   	 if(index<0)return "";
   	 
   	 if(index>=mSentencesList.size())
   		 return mSentencesList.get(mSentencesList.size()-1);
   	 
   	 return mSentencesList.get(index);
    }
    
    /**
     * 获得指定歌词的子句播放时间
     * 
     * <p>默认认为每个单词的时间是平均的</p>
     * @param index 用来指定哪句歌词 
     * @param len 子句长度
     * @return 
     */
    public long getChildSentenceDuration(int index, int len){
    	return getCellDurationTime(index) * len;
    }
    
    public long getCellDurationTime(int index){
    	if(index < 0 || index > mSentencesList.size()){
    		return -1;
    	}
    	
    	String sentence = mSentencesList.get(index);
    	
    	long cellDuration = getSentenceDuration(index) / sentence.length();
    	
    	return cellDuration;
    }
    
    /**
     * 获得指定歌词的持续时间
     * @param index
     * @return
     */
    public long getSentenceDuration(int index){
    	if(index < 0){
    		return -1;
    	}
    	long duration = mToTimesList.get(index) - mFromTimesList.get(index);
    	
    	return duration;
    	
    }
    
    /**
     * 获得指定歌词的起始时间
     * @param index
     * @return
     */
    public long getSentenceStartTime(int index){
    	if(index < 0){
    		return -1;
    	}
    	return mFromTimesList.get(index);
    }
    
    /**
     * 获得指定歌词的结束时间
     * @param index
     * @return
     */
    public long getSentenceEndTime(int index){
    	if(index < 0){
    		return -1;
    	}
    	return mToTimesList.get(index);
    }
    
    public int getLines()
    {
       return mTotalNum;
    }
    
    public int seekTime(long time)
    {
    	if(mTotalNum<=0)return -1;
    	
    	int seekIndex=-1;
    	long from=0;
    	long to=0;
    	for(int i=0;i<mTotalNum;i++)
    	{
    		from=mFromTimesList.get(i);
    		to=mToTimesList.get(i);
    		if((i==0)&&(time<from))
    		{
    			seekIndex=0;
    			break;
    		}
    		if((i==(mTotalNum-1))&&(time>to))
    		{
    			seekIndex=mTotalNum-1;
    			break;
    		}
    		if((time>=from)&&(time<to))
    		{
    			seekIndex=i;
    			break;
    		}
    	}
    	return seekIndex;
    }
	
    public interface OnLyricResetListener {
    	public void onReset();
    }
}
