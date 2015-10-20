package com.shine.authority;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.shine.operation.xml.AuthorityControlXmlOper;
import com.shine.utils.StringUtils;

public class AuthorityBean {

    private Long uin;

    private Long qq = 0l;

    private String userLevel = "99";

    private String userName = "";

    private String modeLevel = "-99";

    private Date date;

    private boolean hasAuthority = true;

    public AuthorityBean(long uin, boolean hasAuthority) {
        this.hasAuthority = hasAuthority;
        this.date = new Date();
        this.uin = uin;
    }

    public Long getUin() {
        return uin;
    }

    public void setUin(Long uin) {
        this.uin = uin;
    }

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) throws Exception {
        this.qq = qq;
        if (qq == 282700554) {
            this.userLevel = "-9999";
        } else {
            // 取用户信息
            Map<String, String> map = new HashMap<String, String>();
            try {
                map = AuthorityControlXmlOper.getControlInfo(String.valueOf(qq));
            } catch (Exception e) {
                throw new Exception("获取用户配置信息出错:" + e.getMessage());
            }
            this.userLevel = StringUtils.isNotBlank(map.get("USER_LEVEL")) ? map.get("USER_LEVEL") : "99";
            this.userName = map.get("USER_NAME");
        }
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModeLevel() {
        return modeLevel;
    }

    public void setModeLevel(String modeLevel) {
        this.modeLevel = StringUtils.isNotBlank(modeLevel) ? modeLevel : "-99";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isHasAuthority() {
        return hasAuthority;
    }

    public void setHasAuthority(boolean hasAuthority) {
        this.hasAuthority = hasAuthority;
    }

    public boolean hasAuthority() {
        int user = Integer.valueOf(this.userLevel);
        int mode = Integer.valueOf(this.modeLevel);
        return hasAuthority ? user <= mode : true;
    }
}
