package com.zhikuntech.intellimonitor.cable.domain.utils;

public class CableIdCalc {
    /**
     * 告警类型、海缆编号和海缆第几个点 转成海缆总编号 工具类
     *        A海缆温度点位8804-9803 14803-14842
     *      * B海缆温度点位9804-9932(1-129) 9933-10802(131-1000) 14843-14882(1001-1040)
     *      * C海缆温度点位10803-11802 14883-14922
     *      * A海缆应力点位11803-12802 14923-15484
     *      * B海缆应力点位12803-13802 15485-16046
     *      * C海缆应力点位13803-14802 16047-16608
     */

    public static int numToId(int type, int id, int num){
        int result=0;
        if (type==1){
            switch (id){
                case 1:
                    if (num<=1000){
                        result = num + 8803;
                    }else {
                        result = num +13802;
                    }
                    break;
                case 2:
                    if (num<=129){
                        result = num + 9803;
                    }else if(num>=131 && num<=1000){
                        result = num + 9802;
                    }else {
                        result = num + 13842;
                    }
                    break;
                case 3:
                    if (num <= 1000) {
                        result = num + 10802;
                    }else {
                        result = num + 13882;
                    }
                    break;
            }
        }else {
            switch (id){
                case 1 :
                    if (num<=1000){
                        result = num + 11802;
                    }else {
                        result = num + 13922;
                    }
                break;
                case 2 :
                    if (num<=1000){
                        result = num + 12802;
                    }else {
                        result = num + 14484;
                    }
                break;
                case 3 :
                    if (num<=1000){
                        result = num + 13802;
                    }else {
                        result = num + 15046;
                    }
                break;
            }
        }
        return result;
    }
}
