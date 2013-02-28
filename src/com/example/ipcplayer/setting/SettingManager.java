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
	
}