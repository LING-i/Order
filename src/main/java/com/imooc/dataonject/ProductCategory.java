package com.imooc.dataonject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 类目表
 */

@Entity
@DynamicUpdate //修改的时候动态更新时间
@Data   //get、set、toString
public class ProductCategory {

    /*类目ID*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增
    private Integer categoryId;

    /** 类目名字. */
    private String categoryName;

    /** 类目编号. */
    private Integer categoryType;

    private Date updateTime;

    private Date CreateTime;

    public ProductCategory(){

    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
