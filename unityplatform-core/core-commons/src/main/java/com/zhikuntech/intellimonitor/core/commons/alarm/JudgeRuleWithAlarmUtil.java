package com.zhikuntech.intellimonitor.core.commons.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author 杨锦程
 * @Date 2021/7/8 11:46
 * @Description 警告判断工具类
 * @Version 1.0
 */
@Slf4j
public class JudgeRuleWithAlarmUtil {
    private static final String LEFTCLOSEDINTERVAL = "[";
    private static final String RIGHTCLOSEDINTERVAL = "]";
    private static final String LEFTOPENINTERVAL = "(";
    private static final String RIGHTOPENINTERVAL = ")";
    private static final char ANDSET = '∪';
    private static final char INTERSET = '∩';
    private static final String MINUSINFINITY = "-∞";
    private static final String ADDINFINITY = "+∞";

    /**
     * 正常区间
     */
    private static String normalInterval = "";
    /**
     * 一级预警区间
     */
    private static String oneLevelWarningInterval = "";
    /**
     * 二级预警区间
     */
    private static String twoLevelWarningInterval = "";
    /**
     * 告警区间
     */
    private static String alarmInterval = "";

    /**
     * 一二级预警字符串形式: 40,60
     * 告警字符串形式: (-∞,30)U(80,+∞)
     *
     * @param num
     * @param alarmRule
     * @return
     */
    public static AlarmResultDTO process(BigDecimal num, AlarmRuleDTO alarmRule) {
        String alarmRange = alarmRule.getAlarmRange();
        String preWarningRangeLevelOne = alarmRule.getPreWarningRangeLevelOne();
        String preWarningRangeLevelTwe = alarmRule.getPreWarningRangeLevelTwe();
        //判断: 有二级预警一定有一级预警;有一级预警不一定有二级预警
        if(StringUtils.isEmpty(alarmRange)){
            log.error("警告值不能为空!");
            return null;
        }
        if(StringUtils.isNotEmpty(preWarningRangeLevelTwe)){
            if(StringUtils.isEmpty(preWarningRangeLevelOne)){
                log.error("有二级预警值一定要有一级预警值!");
                return null;
            }
        }

        String[] alarmRangeSetSplit = null;
        //如果交集连接
        if (alarmRange.indexOf(INTERSET) > -1) {
            alarmRangeSetSplit = alarmRange.split(String.valueOf(INTERSET));
        }
        //如果并集连接
        else if (alarmRange.indexOf(ANDSET) > -1) {
            alarmRangeSetSplit = alarmRange.split(String.valueOf(ANDSET));

            String[] alarmRangeLeftSplit = alarmRangeSetSplit[0].split(",");
            String[] alarmRangeRightSplit = alarmRangeSetSplit[1].split(",");

            String[] preWarningRangeLevelOnSplit = preWarningRangeLevelOne.split(",");
            String[] preWarningRangeLevelTweSplit = preWarningRangeLevelTwe.split(",");

            //正常区间
            normalInterval = LEFTOPENINTERVAL + preWarningRangeLevelOne + RIGHTOPENINTERVAL;
            //一级预警
            //(23,60]∪[70,100)
            oneLevelWarningInterval = LEFTOPENINTERVAL + new BigDecimal(preWarningRangeLevelTweSplit[0]) + "," +
                    new BigDecimal(preWarningRangeLevelOnSplit[0]) + RIGHTCLOSEDINTERVAL +
                    ANDSET +
                    LEFTCLOSEDINTERVAL + new BigDecimal(preWarningRangeLevelOnSplit[1]) + "," +
                    new BigDecimal(preWarningRangeLevelTweSplit[1]) + RIGHTOPENINTERVAL;
            //二级预警
            twoLevelWarningInterval = LEFTCLOSEDINTERVAL + new BigDecimal(alarmRangeLeftSplit[1].substring(0, alarmRangeLeftSplit[1].length() - 1)) + "," +
                    new BigDecimal(preWarningRangeLevelTweSplit[0]) + RIGHTCLOSEDINTERVAL +
                    ANDSET +
                    LEFTCLOSEDINTERVAL + new BigDecimal(preWarningRangeLevelTweSplit[1]) + "," +
                    new BigDecimal(alarmRangeRightSplit[0].substring(1)) + RIGHTCLOSEDINTERVAL;
            //警告
            alarmInterval = alarmRange;
        } else {
//            alarmInterval = LEFTCLOSEDINTERVAL + alarmRange + RIGHTCLOSEDINTERVAL;
            alarmInterval = alarmRange.charAt(0) + alarmRange.substring(1,alarmRange.length() - 1) + alarmRange.charAt(alarmRange.length() - 1);
            twoLevelWarningInterval = LEFTCLOSEDINTERVAL + preWarningRangeLevelTwe + RIGHTCLOSEDINTERVAL;
            oneLevelWarningInterval = LEFTCLOSEDINTERVAL + preWarningRangeLevelOne + RIGHTCLOSEDINTERVAL;
        }

        List<MeasuringPoint[]> alarmMeasuringPointList = parseOperator(alarmInterval, null);
        assert alarmMeasuringPointList != null;
        Set<MeasuringPoint[]> alarmMeasuringPointSet = new HashSet<>(alarmMeasuringPointList);
        boolean alarmJudge = judgeSingle(alarmMeasuringPointSet, num);
        if (alarmJudge) {
            return AlarmResultDTO.builder().produce(1).level(1).build();
        }

        List<MeasuringPoint[]> twoLevelWarningMeasuringPointList = parseOperator(twoLevelWarningInterval, null);
        assert twoLevelWarningMeasuringPointList != null;
        Set<MeasuringPoint[]> twoLevelWarningMeasuringPointSet = new HashSet<>(twoLevelWarningMeasuringPointList);
        boolean twoLevelWarningJudge = judgeSingle(twoLevelWarningMeasuringPointSet, num);
        if (twoLevelWarningJudge) {
            return AlarmResultDTO.builder().produce(1).level(3).build();
        }

        List<MeasuringPoint[]> oneLevelWarninMmeasuringPointList = parseOperator(oneLevelWarningInterval, null);
        assert oneLevelWarninMmeasuringPointList != null;
        Set<MeasuringPoint[]> oneLevelWarninMmeasuringPointSet = new HashSet<>(oneLevelWarninMmeasuringPointList);
        boolean oneLevelWarninJudge = judgeSingle(oneLevelWarninMmeasuringPointSet, num);
        if (oneLevelWarninJudge) {
            return AlarmResultDTO.builder().produce(1).level(2).build();
        }

        return AlarmResultDTO.builder().produce(0).level(0).build();
    }

    public static AlarmResultDTO process(String num, AlarmRuleDTO alarmRule) {
        BigDecimal decimal = new BigDecimal(num);
        return process(decimal, alarmRule);
    }

    /**
     * 具体判断
     * 经过解析后得到List<MeasuringPoint[]>形式的区间,判断传入的值是否在结果区间范围内
     * @param set 区间
     * @param pvalue 需要判断的值
     * @return
     */
    private static boolean judgeSingle(Set<MeasuringPoint[]> set, BigDecimal pvalue) {
        for (MeasuringPoint[] measuringPoint : set) {
            boolean containLeft = measuringPoint[0].contain;
            if (containLeft) {
                //判断左边的值(包含)
                //大于等于
                if (pvalue.compareTo(measuringPoint[0].value) <= -1) {
                    continue;
                }
                boolean containRight = measuringPoint[1].contain;
                //判断右边的值
                boolean judgeRightResult = judgeRight(measuringPoint[1].value, pvalue, containRight);
                if (judgeRightResult) {
                    return true;
                }
            } else {
                //判断左边的值(不包含)
                //大于
                if (pvalue.compareTo(measuringPoint[0].value) == 1) {
                    boolean containRight = measuringPoint[1].contain;
                    //判断右边的值
                    boolean jugdeRightResult = judgeRight(measuringPoint[1].value, pvalue, containRight);
                    if (jugdeRightResult) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //判断右边的值
    private static boolean judgeRight(BigDecimal rvalue, BigDecimal pvalue, boolean contain) {
        boolean contain_right = contain;
        //判断右边的值
        if (contain_right) {
            //小于等于
            if (pvalue.compareTo(rvalue) < 1) {
                return true;
            } else {
                return false;
            }
        } else {
            //判断右边的值
            //小于
            if (pvalue.compareTo(rvalue) == -1) {
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * 解析告警运算符
     * 运算符由[ ] ( ) ∪ ∩ ∞ ，组成
     * (-∞,30)U(80,+∞)∩(50,100]
     * 解析过程:
     *      (主要过程：单个区间解析成MeasuringPoint[]形式,数组有2个元素,分别表示区间的左右端点。)
     *      初次解析时,先把前2个区间解析成MeasuringPoint[]形式,再根据中间的∪或者∩符号对两个区间进行计算,得到List<MeasuringPoint[]>形式的结果
     *      后面的计算过程是拿到之前计算的List<MeasuringPoint[]>形式的结果和后面的MeasuringPoint[]数组进行计算,
     *      一直递归计算下去,直到把整个字符串解析计算完成,返回最终结果。判断传入的值是否在结果区间范围内
     * @param operator               运算字符串
     * @param lastMeasuringPointList 上一次计算后的结果集
     */
    private static List<MeasuringPoint[]> parseOperator(String operator, List<MeasuringPoint[]> lastMeasuringPointList) {
        List<MeasuringPoint[]> firstMeasuringPointList = new ArrayList<>();
        List<MeasuringPoint[]> secondMeasuringPointList = new ArrayList<>();

        //判断字符串是否为空，为空表示判断完成，退出递归，直接返回解析后的结果集
        if (StringUtils.isEmpty(operator)) {
            return lastMeasuringPointList;
        }
        //判断：如果上次结果集有值，operator有值，那么operator肯定是以交集或并集符号开头的
        //∩(50,100]
        //∩(50,100] U (40,+∞]
        else if (!StringUtils.isEmpty(operator) && !ObjectUtils.isEmpty(lastMeasuringPointList)) {
            //第一个字符是交并集运算符
            char operateCharAt = operator.charAt(0);
            firstMeasuringPointList = lastMeasuringPointList;
            //截取第一个字符后面的部分作为新的运算字符串
            String newOperator = operator.substring(1);

            //第一个交集符号的位置
            int andSetCharAt = newOperator.indexOf(ANDSET);
            //第一个并集符号的位置
            int interSetCharAt = newOperator.indexOf(INTERSET);
            //没有并集和交集符号
            if (andSetCharAt < 0 && interSetCharAt < 0) {
                //整个运算符字符串就是一整个区间
                MeasuringPoint[] measuringPoint = parseInterval(newOperator);
                //运算符计算
                secondMeasuringPointList.add(measuringPoint);
                List<MeasuringPoint[]> measuringPoints = new ArrayList<>();
                if (operateCharAt == ANDSET) {
                    measuringPoints = calculateAddSet(firstMeasuringPointList, measuringPoint);
                } else if (operateCharAt == INTERSET) {
                    MeasuringPoint[] newMeasuringPoints = calculateInterSet(firstMeasuringPointList, measuringPoint);
                    measuringPoints.add(newMeasuringPoints);
                }
                //返回
                return parseOperator("", measuringPoints);
            } else {
                //集合运算符号
                char setType = 0;
                //第一个集合符号
                int startIndex = 0;
                //计算两个区间
                List<MeasuringPoint[]> newMeasuringPointList = new ArrayList<>();
                //如果包含了交集或者并集符号
                //如果确保第一个运算符是交集符号，那并集符号必须是不存在或者位置大于交集的位置
                if (andSetCharAt > 0 && (interSetCharAt < 0 || interSetCharAt > andSetCharAt)) {
                    //这样就得找到后面的左右开闭区间
                    //在交集符号位置后面一个位置就是第一个左开或闭符号位置
                    startIndex = andSetCharAt + 1;
                    //设置运算符号
                    setType = ANDSET;
                    MeasuringPoint[] measuringPoint = calculateInterSet(firstMeasuringPointList, secondMeasuringPointList.get(0));
                    newMeasuringPointList.add(measuringPoint);
                }
                //如果确保第一个运算符是并集符号，那交集符号必须是不存在或者大于并集的位置
                else if (interSetCharAt > 0 && (andSetCharAt < 0 || andSetCharAt > interSetCharAt)) {
                    //这样就得找到后面的左右开闭区间
                    //在交集符号位置后面一个位置就是第一个左开或闭符号位置
                    startIndex = interSetCharAt + 1;
                    //设置运算符号
                    setType = INTERSET;
                    List<MeasuringPoint[]> measuringPoints = calculateAddSet(firstMeasuringPointList, secondMeasuringPointList.get(0));
                    newMeasuringPointList.addAll(measuringPoints);
                }

                //第一个区间模型
                String firstOperator = operator.substring(0, startIndex);
                MeasuringPoint[] firstMeasuringPoint = parseInterval(firstOperator);
                firstMeasuringPointList = lastMeasuringPointList;
                secondMeasuringPointList.add(firstMeasuringPoint);

                //截取第一个区间模型后面的字符串部分作为剩余的运算字符串
                String lastOperator = newOperator.substring(startIndex);
                //截取剩余的字符串传递到下一次递归
                return parseOperator(lastOperator, newMeasuringPointList);
            }
        }
        //如果上次结果集没值,operator有值，表示是第一次递归
        //(50,100]
        //(50,100] U (40,+∞] ∩ (10,80)
        else if (!StringUtils.isEmpty(operator) && ObjectUtils.isEmpty(lastMeasuringPointList)) {
            //第一个交集符号的位置
            int andSetCharAt = operator.indexOf(ANDSET);
            //第一个并集符号的位置
            int interSetCharAt = operator.indexOf(INTERSET);
            //没有并集和交集符号
            //(50,100]
            if (andSetCharAt < 0 && interSetCharAt < 0) {
                List<MeasuringPoint[]> measuringPointList = new ArrayList<>();
                //整个运算符字符串就是一整个区间
                MeasuringPoint[] measuringPoint = parseInterval(operator);
                measuringPointList.add(measuringPoint);
                //此种情况下不用计算，直接返回
                return parseOperator("", measuringPointList);
            }
            //(50,100] U (40,+∞] ∩ (10,80)
            else {
                //集合运算符号
                char setType = 0;
                //第1个区间计算符号位置
                int firstStartIndex = 0;
                //如果包含了交集或者并集符号
                //如果确保第一个运算符是交集符号，那并集符号必须是不存在或者位置大于交集的位置
                if (andSetCharAt > 0 && (interSetCharAt < 0 || interSetCharAt > andSetCharAt)) {
                    //这样就得找到后面的左右开闭区间
                    //在交集符号位置后面一个位置就是第一个左开或闭符号位置
                    firstStartIndex = andSetCharAt;
                    //设置运算符号
                    setType = ANDSET;
                }
                //如果确保第一个运算符是并集符号，那交集符号必须是不存在或者大于并集的位置
                else if (interSetCharAt > 0 && (andSetCharAt < 0 || andSetCharAt > interSetCharAt)) {
                    //这样就得找到后面的左右开闭区间
                    //在交集符号位置后面一个位置就是第一个左开或闭符号位置
                    firstStartIndex = interSetCharAt;
                    //设置运算符号
                    setType = INTERSET;
                }

                //第一个区间模型
                String firstOperator = operator.substring(0, firstStartIndex);
                MeasuringPoint[] firstMeasuringPoint = parseInterval(firstOperator);
                firstMeasuringPointList.add(firstMeasuringPoint);

                //截取交集符号之后的部分作为新的操作字符串
                String subOperator = operator.substring(firstStartIndex + 1, operator.length());
                //接下来确定后面的右开或闭符号位置
                //确定方法:判断后面还有没有并集或交集符号，如果有判断出最靠近的那个符号的位置，则这个位置减1就是右开或闭符号;
                //          如果没有，那么最后一个符号就是右开或闭符号
                int nextAndSetCharAt = subOperator.indexOf(ANDSET);
                int nextInterSetCharAt = subOperator.indexOf(INTERSET);

                //第2个区间计算符号位置
                int secondStartIndex = 0;
                //最后截取的开始位置
                int remainStartIndex = 0;
                //第二个区间模型字符串
                String nextOperator = "";

                //第二个交集符号的位置
                int secondAndSetCharAt = subOperator.indexOf(ANDSET);
                //第二个并集符号的位置
                int secondInterSetCharAt = subOperator.indexOf(INTERSET);

                if (secondAndSetCharAt < 0 && secondInterSetCharAt < 0) {
                    //如果已经是最后一段区间了，剩余部分就是第二个区间模型字符串
                    nextOperator = subOperator.substring(0, subOperator.length());
                    //最后截取的开始位置是长度
                    remainStartIndex = nextOperator.length();
                } else if (secondAndSetCharAt > 0 && (secondInterSetCharAt < 0 || secondInterSetCharAt > secondAndSetCharAt)) {
                    nextOperator = subOperator.substring(0, nextAndSetCharAt);
                    secondStartIndex = secondAndSetCharAt;
                    remainStartIndex = secondAndSetCharAt;
                } else if (secondInterSetCharAt > 0 && (secondAndSetCharAt < 0 || secondAndSetCharAt > secondInterSetCharAt)) {
                    nextOperator = subOperator.substring(0, nextInterSetCharAt);
                    secondStartIndex = secondAndSetCharAt;
                    remainStartIndex = secondInterSetCharAt;
                }
                //添加第二个区间模型
                MeasuringPoint[] nextMeasuringPoint = parseInterval(nextOperator);
                secondMeasuringPointList.add(nextMeasuringPoint);

                List<MeasuringPoint[]> newMeasuringPointList = new ArrayList<>();
                //计算得到新的区间
                if (setType == INTERSET) {
                    MeasuringPoint[] measuringPoint = calculateInterSetSingle(firstMeasuringPoint, nextMeasuringPoint);
                    if (null != measuringPoint) {
                        newMeasuringPointList.add(measuringPoint);
                    }
                } else if (setType == ANDSET) {
                    List<MeasuringPoint[]> measuringPoints = calculateAddSet(firstMeasuringPointList, nextMeasuringPoint);
                    newMeasuringPointList.addAll(measuringPoints);
                }

                //截取剩余的字符串传递到下一次递归
                String remainOpretor = subOperator.substring(remainStartIndex, subOperator.length());
                return parseOperator(remainOpretor, newMeasuringPointList);
            }
        } else {
            log.error("不符合逻辑的分支>>>>");
            return null;
        }
    }


    /**
     * 计算并集
     * 第一个参数传入已有的集合区间，第二个参数传入待计算的区间
     * 计算过程：
     * 遍历firstMeasuringPointList，每个元素都和secondMeasuringPoint尝试进行合并计算交集
     *
     * @param firstMeasuringPointList
     * @param secondMeasuringPoint
     * @return
     */
    private static List<MeasuringPoint[]> calculateAddSet(List<MeasuringPoint[]> firstMeasuringPointList,
                                                          MeasuringPoint[] secondMeasuringPoint) {
        List<MeasuringPoint[]> mergeMeasuringPointList = new ArrayList<>();
        for (MeasuringPoint[] measuringPoint : firstMeasuringPointList) {
            List<MeasuringPoint[]> newMeasuringPoint = calculateAddSetSingle(measuringPoint, secondMeasuringPoint);
            //合并成功,将合并后的新区间替换掉secondMeasuringPoint，下次循环使用新区间和firstMeasuringPointList剩余元素合并计算
            if (newMeasuringPoint.size() == 1) {
                secondMeasuringPoint = newMeasuringPoint.get(0);
            }
            //合并失败，加入新的区间
            else if (newMeasuringPoint.size() == 2) {
                mergeMeasuringPointList.addAll(newMeasuringPoint);
            }
        }
        return mergeMeasuringPointList;
    }

    /**
     * 2个区间单独进行并集计算
     *
     * @param firstMeasuringPoint
     * @param secondMeasuringPoint
     * @return
     */
    private static List<MeasuringPoint[]> calculateAddSetSingle(MeasuringPoint[] firstMeasuringPoint, MeasuringPoint[] secondMeasuringPoint) {
        //最终结果
        List<MeasuringPoint[]> mergeMeasuringPointList = new ArrayList<>();

        BigDecimal compFirstValue0 = firstMeasuringPoint[0].getValue();
        BigDecimal compSecondValue0 = secondMeasuringPoint[0].getValue();

        MeasuringPoint[] leftMeasuringPoint = null;
        MeasuringPoint[] rightMeasuringPoint = null;

        //先确定哪个区间是左边区间，右边区间
        // <=
        if (compFirstValue0.compareTo(compSecondValue0) < 1) {
            leftMeasuringPoint = firstMeasuringPoint;
            rightMeasuringPoint = secondMeasuringPoint;
        } else {
            leftMeasuringPoint = secondMeasuringPoint;
            rightMeasuringPoint = firstMeasuringPoint;
        }

        //[20,30] (25,50)
        if (leftMeasuringPoint[0].getValue().compareTo(rightMeasuringPoint[0].getValue()) < 1 &&
                leftMeasuringPoint[1].getValue().compareTo(rightMeasuringPoint[0].getValue()) == 1 &&
                leftMeasuringPoint[1].getValue().compareTo(rightMeasuringPoint[1].getValue()) == -1) {
            MeasuringPoint[] measuringPoint1 = new MeasuringPoint[2];
            measuringPoint1[0] = leftMeasuringPoint[0];
            measuringPoint1[1] = rightMeasuringPoint[1];
            mergeMeasuringPointList.add(measuringPoint1);
        } else if (leftMeasuringPoint[1].getValue().compareTo(rightMeasuringPoint[0].getValue()) == -1) {
            mergeMeasuringPointList.add(leftMeasuringPoint);
            mergeMeasuringPointList.add(rightMeasuringPoint);
        }
        return mergeMeasuringPointList;
    }

    /**
     * 计算交集
     * (10,30) (20,50)
     * (10,30) (40,60)
     * 计算过程:
     * 遍历firstMeasuringPointList，每个元素分别和secondMeasuringPoint进行计算
     *
     * @param firstMeasuringPointList
     * @param secondMeasuringPoint
     * @return
     */
    private static MeasuringPoint[] calculateInterSet(List<MeasuringPoint[]> firstMeasuringPointList,
                                                      MeasuringPoint[] secondMeasuringPoint) {
        //最终结果
        MeasuringPoint[] mergeMeasuringPoint = null;

        for (MeasuringPoint[] measuringPoint : firstMeasuringPointList) {
            MeasuringPoint[] newMeasuringPoint = calculateInterSetSingle(measuringPoint, secondMeasuringPoint);
            if (null == newMeasuringPoint) {
                //如果计算结果为null，跳过下面的赋值
                continue;
            } else {
                secondMeasuringPoint = newMeasuringPoint;
                mergeMeasuringPoint = newMeasuringPoint;
            }
        }
        return mergeMeasuringPoint;
    }

    /**
     * 2个区间单独进行交集计算
     * (-∞,30)∪(80,+∞)∩(50,100)
     *
     * @param firstMeasuringPoint
     * @param secondMeasuringPoint
     * @return
     */
    private static MeasuringPoint[] calculateInterSetSingle(MeasuringPoint[] firstMeasuringPoint, MeasuringPoint[] secondMeasuringPoint) {
        //最终结果
        MeasuringPoint[] mergeMeasuringPoint = null;

        BigDecimal compFirstValue0 = firstMeasuringPoint[0].getValue();
        BigDecimal compSecondValue0 = secondMeasuringPoint[0].getValue();

        MeasuringPoint[] leftMeasuringPoint = null;
        MeasuringPoint[] rightMeasuringPoint = null;

        //先确定哪个区间是左边区间，右边区间
        // <=
        if (compFirstValue0.compareTo(compSecondValue0) < 1) {
            leftMeasuringPoint = firstMeasuringPoint;
            rightMeasuringPoint = secondMeasuringPoint;
        } else {
            leftMeasuringPoint = secondMeasuringPoint;
            rightMeasuringPoint = firstMeasuringPoint;
        }

        //[20,30] (25,50)
        if (leftMeasuringPoint[0].getValue().compareTo(rightMeasuringPoint[0].getValue()) < 1 &&
                leftMeasuringPoint[1].getValue().compareTo(rightMeasuringPoint[0].getValue()) == 1 &&
                leftMeasuringPoint[1].getValue().compareTo(rightMeasuringPoint[1].getValue()) == -1) {
            mergeMeasuringPoint = new MeasuringPoint[2];
            MeasuringPoint measuringPoint0 = new MeasuringPoint(rightMeasuringPoint[0].getValue(), rightMeasuringPoint[0].isContain());
            MeasuringPoint measuringPoint1 = new MeasuringPoint(leftMeasuringPoint[1].getValue(), leftMeasuringPoint[1].isContain());
            mergeMeasuringPoint[0] = measuringPoint0;
            mergeMeasuringPoint[1] = measuringPoint1;
        }
        return mergeMeasuringPoint;
    }

    /**
     * 解析单个区间，例如(5,10]或(-∞,7]
     *
     * @param interval
     * @return 返回左右点数组
     */
    private static MeasuringPoint[] parseInterval(String interval) {
        //使用逗号分割成两部分
        String[] splitStr = interval.split(",");
        //左边需要判断是否是-∞，右边需要判断是否是+∞；如果是用最值Double.MIN_VALUE或Double.MAX_VALUE表示,如果不是强转换成数字
        String leftSubStr = splitStr[0].substring(1, splitStr[0].length());
        //左边区间符号
        String leftIntervalSymbol = splitStr[0].substring(0, 1);
        String rightSubStr = splitStr[1].substring(0, splitStr[1].length() - 1);
        //右边区间符号
        String rightIntervalSymbol = splitStr[1].substring(splitStr[1].length() - 1);
        //左边数
        BigDecimal leftNumber = null;
        //右边数
        BigDecimal rightNumber = null;
        if (leftSubStr.equals(MINUSINFINITY)) {
            leftNumber = BigDecimal.valueOf(Double.MIN_VALUE);
        } else {
            leftNumber = new BigDecimal(leftSubStr);
        }
        if (rightSubStr.equals(ADDINFINITY)) {
            rightNumber = BigDecimal.valueOf(Double.MAX_VALUE);
        } else {
            rightNumber = new BigDecimal(rightSubStr);
        }
        //解析到模型中
        MeasuringPoint[] measuringPoints = new MeasuringPoint[2];
        BigDecimal measuringPointValue0 = leftNumber;
        boolean contain0 = false;
        if (leftIntervalSymbol.equals(LEFTCLOSEDINTERVAL)) {
            contain0 = true;
        } else if (leftIntervalSymbol.equals(LEFTOPENINTERVAL)) {
            contain0 = false;
        }
        MeasuringPoint measuringPoint0 = new MeasuringPoint(measuringPointValue0, contain0);
        measuringPoints[0] = measuringPoint0;

        BigDecimal measuringPointValue1 = rightNumber;
        boolean contain1 = false;
        if (rightIntervalSymbol.equals(RIGHTCLOSEDINTERVAL)) {
            contain1 = true;
        } else if (rightIntervalSymbol.equals(RIGHTOPENINTERVAL)) {
            contain1 = false;
        }
        MeasuringPoint measuringPoint1 = new MeasuringPoint(measuringPointValue1, contain1);
        measuringPoints[1] = measuringPoint1;
        return measuringPoints;
    }

    /**
     * 区间的端点模型
     */
    @Data
    @AllArgsConstructor
    @ToString
    private static class MeasuringPoint {
        private BigDecimal value;
        private boolean contain;
    }
}
