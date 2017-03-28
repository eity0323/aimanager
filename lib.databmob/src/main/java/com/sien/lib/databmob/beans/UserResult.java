package com.sien.lib.databmob.beans;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 用户实体
 */
public class UserResult extends BmobUser {

	private static final long serialVersionUID = 1L;
	private Integer age;
	private Integer num;
	private Boolean sex;

	private List<String> hobby;		// 对应服务端Array类型：String类型的集合

	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public List<String> getHobby() {
		return hobby;
	}
	public void setHobby(List<String> hobby) {
		this.hobby = hobby;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return getUsername()+"\n"+getObjectId()+"\n"+age+"\n"+num+"\n"+getSessionToken()+"\n"+getEmailVerified();
	}
}
