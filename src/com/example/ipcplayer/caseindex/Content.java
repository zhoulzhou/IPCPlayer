package com.example.ipcplayer.caseindex;

import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.StringUtil;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Content {
	String letter;
	String content;
	private String name;  
    private Object other = "test";  
    private String pinyin;
    
	public Object getOther() {
		return other;
	}

	public void setOther(Object other) {
		this.other = other;
	}

	public String getPinyin() {
		return getPingYin(name);
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Content(String letter, String content){
		this.letter = letter;
		this.name = content;
		this.pinyin = getPinyin();
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getName(){
		return name;
	}
    public String getPingYin(String inputString) {  
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        format.setVCharType(HanyuPinyinVCharType.WITH_V);  

        if(StringUtil.isEmpty(inputString)){
        	LogUtil.d(" inputString is null  ");
        	return null;
        }
        char[] input = inputString.trim().toCharArray();  
        String output = "";  

        try {  
            for (int i = 0; i < input.length; i++) {  
                if (java.lang.Character.toString(input[i]).  
                matches("[\\u4E00-\\u9FA5]+")) {  
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i],format);  
                    output += temp[0];  
                } else  
                    output += java.lang.Character.toString(  
                    input[i]);  
            }  
        } catch (BadHanyuPinyinOutputFormatCombination e) {  
            e.printStackTrace();  
        }  
        return output;  
    }  
}