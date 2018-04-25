package com.android.residemenu.lt_lib.enumdata;

/**
 * 号码类型枚举类
 *
 * @author Left
 *
 * @version $Id: K3AwardNumShapeEnum.java, v 0.1 2015年6月11日 上午11:53:01 Left Exp $
 */
public enum K3AwardNumShapeEnum implements EnumBase {

    TH3(new NMK3ShapeEnum[]{NMK3ShapeEnum.THT3,NMK3ShapeEnum.THD3},"三同号"),

    TH2(new NMK3ShapeEnum[]{NMK3ShapeEnum.THD2,NMK3ShapeEnum.THF2},"二同号"),

    BTH3(new NMK3ShapeEnum[]{NMK3ShapeEnum.BTH3},"三不同号"), // 123,124,125,126,134,135,136,145,146,156,234,235,236,245,246,256,345,346,356,456

    BTH2(new NMK3ShapeEnum[]{NMK3ShapeEnum.BTH2},"二不同号"), // 12,13,14,15,16,23,24,25,26,34,35,36,45,46,56

    LH3(new NMK3ShapeEnum[]{NMK3ShapeEnum.LH3},"三连号"), // 123,234,345,456

    ;
    private String message;

    private NMK3ShapeEnum[] containValues;

    private K3AwardNumShapeEnum(NMK3ShapeEnum[] containValues,String message){
        this.message=message;
        this.containValues = containValues;
    }

    public NMK3ShapeEnum[] getContainValues(){
        return containValues;
    }

    public String message() {
        // TODO Auto-generated method stub
        return message;
    }

    public Number value() {
        // TODO Auto-generated method stub
        return null;
    }

}
