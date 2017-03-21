package com.sien.lib.databmob.beans;

import org.greenrobot.greendao.annotation.*;

/**
 * 固定目标分类（非自动创建项）
 */
@Entity
public class AimTypeVO extends CPBaseVO{

    public static final int STATUS_UNDO = 0;//未做状态
    public static final int STATUS_DONE = 1;//完成状态

    public static final int PERIOD_DAY = 1;//1天
    public static final int PERIOD_WEEK = 7;//1周
    public static final int PERIOD_MONTH = 30;//1月
    public static final int PERIOD_SEASON = 90;//1季
    public static final int PERIOD_HALF_YEAR = 180;//半年
    public static final int PERIOD_YEAR = 365;//1年
    public static final int PERIOD_HALF_MONTH = 15;//半月

    public static final int PRIORITY_ONE = 1;//1级
    public static final int PRIORITY_TWO = 2;//2级
    public static final int PRIORITY_THREE = 3;//3级
    public static final int PRIORITY_FOUR = 4;//4级
    public static final int PRIORITY_FIVE = 5;//5级

    @Id(autoincrement = true)
    private Long id;
    private Integer targetPeriod;//预期完成周期
    private Boolean planProject;//是否为计划工程
    private String cover;//封面图片
	
    private String typeName;//类型名称
    private Boolean customed;//是否为自定义
    private Boolean recyclable;//是否循环创建
    private Integer period;//实际完成周期 1，7，10，15，30，90，180，360天
    private java.util.Date startTime;//开始时间(创建时间)
    private java.util.Date endTime;//结束时间
    private java.util.Date modifyTime;//修改时间
    private Integer finishStatus;//完成状态 0为完成 1已完成
    private Integer priority;//优先等级 1 优先级最高，5优先级最低
    private String desc;//类型描述
    private Integer finishPercent;//完成百分比

    private Boolean active;//是否为激活状态(激活状态可自动创建，非激活状态不可自动创建)
    private String firstExtra;//备用字段1 （用于记录自动创建的父目标分类id;仅自动创建分类有该字段，固定分类为空）
    private String secondExtra;//备用字段2 (用于开启倒计时)

    @Generated
    public AimTypeVO() {
    }

    public AimTypeVO(Long id) {
        this.id = id;
    }


    @Generated
    public AimTypeVO(Long id, String typeName, String desc, Boolean customed, Boolean recyclable, Integer priority, Integer period, Integer targetPeriod, Integer finishStatus, Integer finishPercent, java.util.Date startTime, java.util.Date endTime, java.util.Date modifyTime, Boolean active, String firstExtra, String secondExtra, Boolean planProject, String cover) {
        this.id = id;
        this.typeName = typeName;
        this.desc = desc;
        this.customed = customed;
        this.recyclable = recyclable;
        this.priority = priority;
        this.period = period;
        this.targetPeriod = targetPeriod;
        this.finishStatus = finishStatus;
        this.finishPercent = finishPercent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.modifyTime = modifyTime;
        this.active = active;
        this.firstExtra = firstExtra;
        this.secondExtra = secondExtra;
        this.planProject = planProject;
        this.cover = cover;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getCustomed() {
        return customed;
    }

    public void setCustomed(Boolean customed) {
        this.customed = customed;
    }

    public Boolean getRecyclable() {
        return recyclable;
    }

    public void setRecyclable(Boolean recyclable) {
        this.recyclable = recyclable;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getTargetPeriod() {
        return targetPeriod;
    }

    public void setTargetPeriod(Integer targetPeriod) {
        this.targetPeriod = targetPeriod;
    }

    public Integer getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(Integer finishStatus) {
        this.finishStatus = finishStatus;
    }

    public Integer getFinishPercent() {
        return finishPercent;
    }

    public void setFinishPercent(Integer finishPercent) {
        this.finishPercent = finishPercent;
    }

    public java.util.Date getStartTime() {
        return startTime;
    }

    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }

    public java.util.Date getEndTime() {
        return endTime;
    }

    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }

    public java.util.Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(java.util.Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getFirstExtra() {
        return firstExtra;
    }

    public void setFirstExtra(String firstExtra) {
        this.firstExtra = firstExtra;
    }

    public String getSecondExtra() {
        return secondExtra;
    }

    public void setSecondExtra(String secondExtra) {
        this.secondExtra = secondExtra;
    }

    public Boolean getPlanProject() {
        return planProject;
    }

    public void setPlanProject(Boolean planProject) {
        this.planProject = planProject;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * 校验是否开启倒计时
     * @return
     */
    public boolean checkCountdown(){
        if (secondExtra == null){
            return false;
        }

        if ("true".equals(secondExtra)){
            return true;
        }

        return false;
    }

}
