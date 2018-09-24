package com.hingecloud.apppubs.pub.model;

import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
/**
 * <p>
 *  实体类
 * </p>
 *
 * @author 张稳
 * @since 2018-09-24 
 */
@TableName("dic")
public class TDic implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 0:app代码版本；
     */
    private Integer type;
    private String value;
    private String reserve1;
    private String reserve2;
    private String reserve3;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    @Override
    public String toString() {
        return "TDic{" +
            ", id=" + id +
            ", type=" + type +
            ", value=" + value +
            ", reserve1=" + reserve1 +
            ", reserve2=" + reserve2 +
            ", reserve3=" + reserve3 +
            "}";
    }
}
