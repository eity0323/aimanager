package com.sien.lib.databmob.beans;

import org.greenrobot.greendao.annotation.*;

/**
 * 目标项实体（非自动创建项）
 */
@Entity
public class AimItemVO  extends CPBaseVO{
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
    public static final int PRIORITY_TWO = 1;//2级
    public static final int PRIORITY_THREE = 1;//3级
    public static final int PRIORITY_FOUR = 1;//4级
    public static final int PRIORITY_FIVE = 1;//5级

    @Id(autoincrement = true)
    private Long id;
    private Long aimTypeId;//目标类型id
    private String cover;//封面图片
	
	private String aimName;//目标名称
    private java.util.Date startTime;//开始时间
    private java.util.Date endTime;//结束时间
    private java.util.Date modifyTime;//修改时间
    private Integer finishStatus;//完成状态
    private Integer finishPercent;//完成百分比
    private Integer priority;//优先等级
    private String desc;//类型描述

    @Generated
    public AimItemVO() {
    }

    public AimItemVO(Long id) {
        this.id = id;
    }

    @Generated
    public AimItemVO(Long id, String aimName, String desc, Long aimTypeId, java.util.Date startTime, java.util.Date endTime, java.util.Date modifyTime, Integer finishStatus, Integer finishPercent, Integer priority, String cover) {
        this.id = id;
        this.aimName = aimName;
        this.desc = desc;
        this.aimTypeId = aimTypeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.modifyTime = modifyTime;
        this.finishStatus = finishStatus;
        this.finishPercent = finishPercent;
        this.priority = priority;
        this.cover = cover;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAimName() {
        return aimName;
    }

    public void setAimName(String aimName) {
        this.aimName = aimName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getAimTypeId() {
        return aimTypeId;
    }

    public void setAimTypeId(Long aimTypeId) {
        this.aimTypeId = aimTypeId;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

}
