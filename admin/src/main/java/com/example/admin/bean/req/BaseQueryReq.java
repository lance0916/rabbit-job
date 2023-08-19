package com.example.admin.bean.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author WuQinglong created on 2021/12/13 07:54
 */
@Getter
@Setter
@ToString
public abstract class BaseQueryReq {

    /**
     * 当前页码
     */
    @NotNull
    @Positive
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    @NotNull
    @Positive
    private Integer pageSize = 10;

}
