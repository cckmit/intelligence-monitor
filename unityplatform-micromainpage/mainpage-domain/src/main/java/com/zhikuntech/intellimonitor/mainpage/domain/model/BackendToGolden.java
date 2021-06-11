package com.zhikuntech.intellimonitor.mainpage.domain.model;

import lombok.Data;

/**
 * @author 代志豪
 * 2021/6/9 18:27
 */
@Data
public class BackendToGolden {

    private int id;

    private int backendId;

    private int goldenId;

    private int number;

    private String description;

}
