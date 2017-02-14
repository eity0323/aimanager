package com.sien.lib.datapp.beans;

import org.greenrobot.greendao.annotation.*;

/**
 * 目标分类（自动创建项）
 */
@Entity
public class AimObjectVO  extends CPBaseVO{

    @Id(autoincrement = true)
    private Long id;
    private String typeName;
    private String desc;
    private Boolean customed;
    private Boolean recyclable;
    private Integer priority;
    private Integer period;
    private Integer targetPeriod;
    private Integer finishStatus;
    private Integer finishPercent;
    private java.util.Date startTime;
    private java.util.Date endTime;
    private java.util.Date modifyTime;
    private Boolean planProject;
    private String cover;

    @Generated
    public AimObjectVO() {
    }

    public AimObjectVO(Long id) {
        this.id = id;
    }

    @Generated
    public AimObjectVO(Long id, String typeName, String desc, Boolean customed, Boolean recyclable, Integer priority, Integer period, Integer targetPeriod, Integer finishStatus, Integer finishPercent, java.util.Date startTime, java.util.Date endTime, java.util.Date modifyTime, Boolean planProject, String cover) {
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

}
