package com.android.residemenu.lt_lib.utils.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * 
 * @author leslie
 * 
 * @version $Id: CombineUtils.java, v 0.1 2014年6月5日 上午11:25:59 leslie Exp $
 */
public final class CombineUtils {

	/**
	 * 根据elementList，计算长度为size的组合值，<br>
	 * 其中索引danIndexSet中指定的在elementList中的元素必须出现， 出现的允许次数，最小为minDan，最大为maxDan<br>
	 * 对符合要求的每个组合，调用visitor进行访问
	 * 
	 * @param <T>
	 * @param elementList
	 * @param size
	 * @param danIndexSet
	 * @param minDan
	 * @param maxDan
	 * @param visitor
	 */
	public static <T> void combineVisit(final List<T> elementList, int size, final Set<Integer> danIndexSet,
			final int minDan, final int maxDan, final CombineEachVisitor<T> visitor) {
		if (danIndexSet == null && (minDan > 0 || maxDan > 0) || minDan > maxDan) {
             throw new IllegalArgumentException("不正确的胆范围,minDan=" + minDan + ",maxDan=" + maxDan);
		}
		combineVisit(elementList, size, new CombineEachVisitor<T>() {
			public void visit(List<T> singleCombineList) {
				// 无胆
				if (danIndexSet == null || danIndexSet.size() == 0) {
					visitor.visit(singleCombineList);
				}
				// 有胆，识别胆范围
				else {
					int mactchCount = 0;
					for (Integer index : danIndexSet) {
						if (singleCombineList.contains(elementList.get(index))) {
							mactchCount++;
							continue;
						}
					}
					if (mactchCount >= minDan && mactchCount <= maxDan) {
						visitor.visit(singleCombineList);
					}
				}
			}
		});
	}

	/**
	 * 根据elementList，计算长度为size的组合值，<br>
	 * 调用visitor进行访问
	 * 
	 * @param elementList
	 * @param size
	 * @param visitor
	 */
	public static <T> void combineVisit(List<T> elementList, int size, CombineEachVisitor<T> visitor) {
		CombinationGenerator generator = new CombinationGenerator(elementList.size(), size);
		while (generator.hasMore()) {
			int[] indices = generator.getNext();
			List<T> simpleComboineList = new ArrayList<T>();
			for (int i = 0; i < indices.length; i++) {
				simpleComboineList.add(elementList.get(indices[i]));
			}
			visitor.visit(simpleComboineList);
		}
	}

	/**
	 * 返回二维数组排列总排列数
	 * 
	 * @param elements
	 * @param size
	 * @return
	 */
	public static <T> int getTwoDimensionCombineTotalAmount(List<List<T>> elements, int size) {
		return getTwoDimensionCombineTotalAmount(elements, size, null, -1, -1);
	}

	/**
	 * 返回二维数组排列总排列数
	 * 
	 * @param elements
	 *            二维元素列表
	 * @param size
	 *            取size个
	 * @param danIndexSet
	 *            胆indexi集合
	 * @param minDan
	 *            最小胆个数
	 * @param maxDan
	 *            最大但个数
	 * @return
	 */
	public static <T> int getTwoDimensionCombineTotalAmount(List<List<T>> elements, int size,
			final Set<Integer> danIndexSet, final int minDan, final int maxDan) {
		final AtomicInteger i = new AtomicInteger();
		twoDimensionCombineVisit(elements, size, danIndexSet, minDan, maxDan, new CombineEachVisitor<T>() {
			public void visit(List<T> singleCombineList) {
				i.incrementAndGet();
			}
		});
		return i.intValue();
	}

	/**
	 * 二维数组组合访问 <br>
	 * 其中索引danIndexSet中指定的在elementList中的元素必须出现， 出现的允许次数，最小为minDan，最大为maxDan<br>
	 * 对符合要求的每个组合，调用visitor进行访问
	 * 
	 * @param <T>
	 * @param elements
	 * @param size
	 * @param danIndexSet
	 * @param minDan
	 * @param maxDan
	 * @param visitor
	 */
	public static <T> void twoDimensionCombineVisit(List<List<T>> elements, int size,
			final Set<Integer> danIndexSet, final int minDan, final int maxDan,
			final CombineEachVisitor<T> visitor) {
		    combineVisit(elements, size, danIndexSet, minDan, maxDan, new CombineEachVisitor<List<T>>() {
			public void visit(List<List<T>> singleCombineList) {
				int max = 1;
				for (List<T> single : singleCombineList) {
					max *= single.size();
				}
			 
				for (int i = 0; i < max; i++) {
					List<T> singleList = new ArrayList<T>();
					int temp = 1;
					for (List<T> single : singleCombineList) { 
						
						temp *= single.size();
						singleList.add(single.get(i / (max / temp) % single.size()));
					    
					}
				    visitor.visit(singleList); 
				}
			
			}

		});
	}

    /**
     * 超级组合,listStr是一个集合列表,这个列表中 的每个元素又都是1个集合, <br>
     * 从listStr的元素集合中各取1个,组成成长度为r的集合,返回组合清单
     *
     * @param <T>
     * @param listStr
     * @param r
     * @return
     */
    public static <T> List<List<T>> superCombine(List<List<T>> listStr, int r) {

        return tqq(listStr, r);
    }
    /**
     * 一个集合A,其中集合中每个元素又是1个集合, <Br>
     * 从结合A的每个元素中,取出一个子元素,组成长度为r的集合 <br>
     * 输出所有的这些组合
     *
     * @param
     * @param
     * @return
     */

    private static <T> List<List<T>> tqq(List<List<T>> coms, int len) {
        List<List<T>> rs = new ArrayList<List<T>>();
        if (coms.size() == len) {
            return qims(coms);
        } else if (len == 1) {// 从coms中遍历所有元素
            return cims(coms);
        } else {
            List<List<T>> comsss = new ArrayList<List<T>>();
            for (int i = 1; i < coms.size(); i++) {
                comsss.add(coms.get(i));
            }
            for (T tt : coms.get(0)) {
                rs.addAll(syscom(tt, tqq(comsss, len - 1)));
            }
            rs.addAll(tqq(comsss, len));

            return rs;
        }

    }
    private static <T> List<List<T>> cims(List<List<T>> coms) {
        List<List<T>> cos = new ArrayList<List<T>>();
        for (List<T> s : coms) {

            for (T ss : s) {
                List<T> list = new ArrayList<T>();

                list.add(ss);
                cos.add(list);
            }

        }
        return cos;
    }
    private static <T> List<List<T>> qims(List<List<T>> coms) {

        List<List<T>> cos = new ArrayList<List<T>>();

        if (coms.size() == 1) {

            for (T str : coms.get(0)) {

                List<T> list = new ArrayList<T>();
                list.add(str);
                cos.add(list);

            }

            return cos;
        } else {
            for (T ss : coms.get(0)) {
                List<List<T>> cc = new ArrayList<List<T>>();

                for (int i = 1; i < coms.size(); i++) {
                    cc.add(coms.get(i));
                }

                for (List<T> listTemp : qims(cc)) {
                    List<T> list = new ArrayList<T>();
                    list.add(ss);
                    for (T str : listTemp) {
                        list.add(str);

                    }
                    cos.add(list);
                }

            }
            return cos;
        }

    }
    private static <T> List<List<T>> syscom(T a, List<List<T>> com) {
        List<List<T>> liss = new ArrayList<List<T>>();
        /***
         * 将二维List拆分成一维
         */
        for (List<T> list : com) {
            List<T> tempList = new ArrayList<T>();
            tempList.add(a);
            for (T LetteryResult : list) {

                tempList.add(LetteryResult);
            }
            liss.add(tempList);
        }

        return liss;

    }
    public static int calculateNum(List<Integer> list, int r) {
        // 组装计算数组
        int[] calculateArr = new int[list.size()];

        int i = 0;
        for (Integer sum : list) {
            calculateArr[i] = sum;
            i++;
        }

        return cols(calculateArr, r);
    }
    private static int cols(int[] ints, int len) {
        if (len == ints.length) {
            return coll(ints);
        } else if (len == 0) {
            return 1;
        } else {
            int[] temps = new int[ints.length - 1];
            System.arraycopy(ints, 1, temps, 0, temps.length);
            return ints[0] * cols(temps, len - 1) + cols(temps, len);
        }
    }
    /**
     * 计算从ints中各取一个数字的方法
     *
     * @param ints
     * @return
     */
    private static int coll(int[] ints) {
        int result = 1;
        for (int i : ints) {
            result *= i;
        }
        return result;
    }


}
