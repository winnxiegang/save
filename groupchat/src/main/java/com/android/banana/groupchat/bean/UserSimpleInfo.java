package com.android.banana.groupchat.bean;

/**
 * Created by qiaomu on 2017/11/4.
 */

public class UserSimpleInfo {
    /**
     * accountPasswordSet	boolean	账户密码是否设置
     * identifySetted	boolean	是否完成实名和身份证验证,true表示已经验证,false表示未验证
     * userLogoUrl	String	头像url
     * loginPasswordSet	boolean	登陆密码是否已设置
     * cell	String	手机
     * certType	CertTypeEnum	证件类型
     * 枚举字段如下：
     * PKCS12 => PKCS#12
     * X509 => X509
     * realName	String	真实姓名
     * cellValidate	boolean	手机是否验证
     * canLogin	boolean	是否可登陆
     * certNo	String	证件号码
     * userId	String	用户id
     * loginName	String	登陆名
     */

    public boolean accountPasswordSet;
    public boolean identifySetted;
    public String userLogoUrl;
    public String cell;
    public String certType;
    public String realName;
    public boolean cellValidate;
    public boolean canLogin;
    public String certNo;
    public String userId;
    public String loginName;


}
