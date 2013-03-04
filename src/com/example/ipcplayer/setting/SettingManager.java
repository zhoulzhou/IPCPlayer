package com.example.ipcplayer.setting;


public interface SettingManager extends ComInterface{

	/**
	 * 设置腾讯微博access token
	 */
	public void setTencentAccessToken(String token);
	/**
	 * 设置腾讯微博openid
	 * @param openId
	 */
	public void setTencentOpenId(String openId);
	/**
	 * 设置open key
	 * @param openKey
	 */
	public void setTencentOpenKey(String openKey);
	/**
	 * 设置
	 */
	public void setAppForegroundTime(long peroid);		
	
	/**
	 *获取
	 */
	public long getAppForegroundTime();

	/**
	 * 获取腾讯微博access token
	 * @return token
	 */
	public String getTencentAccessToken();
	
	/**
	 * 获取open key
	 * @return
	 */
	public String getTencentOpenKey();
	
	/**
	 * 获取open id
	 * @return
	 */
	public String getTencentOpenId();
	
	/**
	 * 获取新浪微博access token
	 * @return token
	 */
	public String getSinaAccessToken();
	
	/**
	 * 获取新浪微博token过期时间
	 * @return
	 */
	public String getSinaExpireIn();
	
	/**
	 * 设置新浪微博access token
	 */
	public void setSinaAccessToken(String token);
	
	/**
	 * 设置新浪uid
	 * @param uid
	 */
	public void setSinaUid(String uid);
	
	/**
	 * 获取新浪uid
	 * @return
	 */
	public String getSinaUid();
	
	/**
	 * 设置新浪过期时间
	 * @param expire 时间字符串
	 */
	public void setSinaExpireIn(String expire);
}