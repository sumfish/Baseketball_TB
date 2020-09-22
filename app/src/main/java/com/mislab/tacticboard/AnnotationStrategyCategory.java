package com.mislab.tacticboard;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AnnotationStrategyCategory {
    public static final int THREE_POINTS = 0;// 三分球
    public static final int SCREEN_3_POINTS = 1;// 中距離掩護外切三分
    public static final int SCREEN_JUMP_SHOT = 2;// 中距離掩護外切跳投
    public static final int JUMP_SHOT = 3;// 中距離跳投
    public static final int CUT = 4;// 切入
    public static final int HAND_OFF = 5;// 手遞手傳球
    public static final int POST_UP = 6;// 防守背框單打
    public static final int POST_UP_LOW = 7;// 背框單打(低位)
    public static final int POST_UP_HIGH = 8;// 背框單打(高位)
    public static final int ISO = 9;// 單打
    public static final int PICK_AND_ROW = 10;// 擋切戰術
    public static final int PICK_AND_ROW2 = 11;// 擋切戰術選項
    public static final int OTHERS = 12;// 其他

    public AnnotationStrategyCategory(@StrategyCategory int category){
        System.out.println("Category : "+ category);
    }

    @IntDef({THREE_POINTS, SCREEN_3_POINTS, SCREEN_JUMP_SHOT, JUMP_SHOT, CUT, HAND_OFF, POST_UP, POST_UP_LOW,
            POST_UP_HIGH, ISO, PICK_AND_ROW, PICK_AND_ROW2, OTHERS})
    @Retention(RetentionPolicy.SOURCE)

    public @interface StrategyCategory{

    }
}
