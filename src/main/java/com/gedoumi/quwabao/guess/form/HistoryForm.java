package com.gedoumi.quwabao.guess.form;

import lombok.Data;

/**
 * 历史记录接口接参
 * @author Minced
 */
@Data
public class HistoryForm {

    /**
     * 当前页，默认值：1
     */
    private Integer page = 1;

    /**
     * 每页数据量，默认值：5
     */
    private Integer size = 5;

}
