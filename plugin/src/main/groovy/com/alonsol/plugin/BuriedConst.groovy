package com.alonsol.plugin


/**
 * 埋点常量
 * TYPE_INSET_BEFORE 插入方法前
 * TYPE_INSET_AFTER 插入方法后
 * TYPE_REPLACE 替换方法
 */
class BuriedConst {

    /**
     * 掺入方法体前面
     */
    public static final int TYPE_INSET_BEFORE = 1

    /**
     * 插入方法提后面
     */
    public static final int TYPE_INSET_AFTER = 2

    /**
     * 替换方法提
     */
    public static final int TYPE_REPLACE = 3


    /**
     * 修复代码
     */
    public static final int TYPE_FIX = 4

    /**
     * hook onClickListener or onLongClickListener
     */
    public static final int TYPE_HOOK = 5
}