package com.human.dalligo.vo;

import lombok.Data;

@Data
public class GoodsVO {
    private int goodsId;        // goods_id
    private String goodsName;   // goods_name
    private String goodsBrand;  // goods_brand
    private int goodsPrice;     // goods_price
    private int goodsQuantity;  // goods_quantity
    private String goodsTag;    // goods_tag
    private String goodsImage;  // goods_image
    private String goodsInfo;   // goods_info
    private int topRank;
}