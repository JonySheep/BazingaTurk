package com.bazingaturk.util.enums;

public enum TaskTypeEnum {
    CLASSIFICATION("分类任务",1),
    DETECTION("检测任务",2),
    SEGMENTATION("分割任务",3),

    ;

    private String name;
    private int index;

    TaskTypeEnum(String name, int i) {
        this.name = name;
        this.index = i;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

}
