package com.sien.lib.datapp.beans;

import org.greenrobot.greendao.annotation.*;

/**
 * 目标项实体（自动创建项）
 */
@Entity
public class AimRecordVO  extends CPBaseVO{

    @Id(autoincrement = true)
    private Long id;
    private String aimName;
    private String desc;
    private Long aimTypeId;
    private java.util.Date startTime;
    private java.util.Date endTime;
    private java.util.Date modifyTime;
    private Integer finishStatus;
    private Integer finishPercent;
    private Integer priority;
    private String cover;

    @Generated
    public AimRecordVO() {
    }

    public AimRecordVO(Long id) {
        this.id = id;
    }

    @Generated
    public AimRecordVO(Long id, String aimName, String desc, Long aimTypeId, java.util.Date startTime, java.util.Date endTime, java.util.Date modifyTime, Integer finishStatus, Integer finishPercent, Integer priority, String cover) {
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
