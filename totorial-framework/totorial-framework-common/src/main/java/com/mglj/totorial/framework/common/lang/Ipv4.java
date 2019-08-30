package com.mglj.totorial.framework.common.lang;

import com.yhdx.baseframework.common.util.RegexUtils;

/**
 * Ipv4地址，用整形存储表示
 *
 * Created by zsp on 2018/8/24.
 */
public final class Ipv4 {

    private int value;

    private Ipv4() {

    }

    /**
     * 构建一个ipv4
     *
     * @param string    例如：255.255.255.0
     */
    public Ipv4(String string) {
        if (!RegexUtils.checkIpAddress(string)) {
            throw new IllegalArgumentException("[" + string + "]不是有效的ip地址");
        }
        String[] array = string.split("\\.");
        combine(Integer.parseInt(array[0]),
                Integer.parseInt(array[1]),
                Integer.parseInt(array[2]),
                Integer.parseInt(array[3]));

    }

    /**
     * 构建一个ipv4
     *
     * @param segment1
     * @param segment2
     * @param segment3
     * @param segment4
     */
    public Ipv4(int segment1, int segment2, int segment3, int segment4) {
        if(segment1 < 0 || segment2 < 0 || segment3 < 0 || segment4 < 0) {
            throw new IllegalArgumentException("正确的ipv4地址格式为：[0-255].[0-255].[0-255].[0-255]");
        }
        if(segment1 > 255 || segment2 > 255 || segment3 > 255 || segment4 > 255) {
            throw new IllegalArgumentException("正确的ipv4地址格式为：[0-255].[0-255].[0-255].[0-255]");
        }
        combine(segment1, segment2, segment3, segment4);
    }

    /**
     * 获取ipv4的整形值
     *
     * @return
     */
    public final int getValue() {
        return value;
    }

    @Override
    public final String toString() {
        int segment4 = value & 255;
        int segment3 = value >> 8 & 255;
        int segment2 = value >> 16 & 255;
        int segment1 = value >> 24 & 255;
        return segment1 + "." + segment2 + "." + segment3 + "." + segment4;
    }

    /**
     * 分析整数值，尝试转换为ipv4对象。
     *
     * @param value
     * @return
     */
    public final static Ipv4 parse(int value) {
        int segment4 = value & 255;
        int segment3 = value >> 8 & 255;
        int segment2 = value >> 16 & 255;
        int segment1 = value >> 24 & 255;
        return new Ipv4(segment1, segment2, segment3, segment4);
    }

    private void combine(int segment1, int segment2, int segment3, int segment4) {
        value = segment1 << 24 | segment2 << 16 | segment3 << 8 | segment4;
    }

}
