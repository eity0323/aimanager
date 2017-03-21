package com.sien.lib.databmob.beans;

/**
 * @author sien
 * @date 2017/3/6
 * @descript com.sien.lib.datapp.beans
 */
public enum CoverIcon {
    ICON_DAY("icon_day_en"),ICON_WEEK("icon_week_en"),ICON_MONTH("icon_month_en"),ICON_SEASON("icon_season_en"),ICON_HALFYEAR("icon_halfyear_en"),ICON_YEAR("icon_year_en"),ICON_OTHER("icon_other_en");

    private String mResource;

    private CoverIcon(String resource){
        this.mResource = resource;
    }

    public String getValue(){
        return mResource;
    }
}
