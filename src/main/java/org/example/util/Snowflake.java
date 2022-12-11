package org.example.util;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 雪花算法
 * @author mr.ahai
 */
public class Snowflake implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 起始时间戳
     */
    private final long START_TIMESTAMP;
    //数据标识占用位数
    private final long WORKERID_BIT = 5L;
    /**
     * 数据中心占用位数
     */
    private final long DATACENTER_BIT = 5L;
    /**
     * 序列号占用的位数
     */
    private final long SEQUENCE_BIT = 12L;
    /**
     * 最大支持机器节点数 0~31
     */
    private final long MAX_WORKERID = -1L ^ (-1L << WORKERID_BIT);
    /**
     * 最大支持数据中心节点数 0~31
     */
    private final long MAX_DATACENTER = -1L ^ (-1L << DATACENTER_BIT);
    /**
     * 最大支持序列号 12位 0~4095
     */
    private final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    private final long WORKERID_LEFT_SHIFT = SEQUENCE_BIT;
    private final long DATACENTER_LEFT_SHIFT = SEQUENCE_BIT + WORKERID_BIT;
    private final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BIT + WORKERID_BIT + DATACENTER_BIT;

    private final long workerId;
    private final long datacenterId;
    private long sequence = 0L;

    /**
     * 上一次的时间戳
     */
    private long lastTimestamp = -1L;

    public Snowflake(long workerId, long datacenterId) {
        this(null,workerId,datacenterId);
    }

    public Snowflake(LocalDateTime localDateTime, long workerId, long datacenterId) {
        if (localDateTime == null) {
            //2021-10-23 22:41:08 北京时间
            this.START_TIMESTAMP = 1635000080L;
        } else {
            this.START_TIMESTAMP = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        }
        if (workerId > MAX_WORKERID || workerId < 0) {
            throw new IllegalArgumentException("workerId can't be greater than MAX_WORKERID or less than 0");
        }
        if (datacenterId > MAX_DATACENTER || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER or less than 0");
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获取下一个ID
     * @return long:id
     */
    public synchronized long nextId() {
        long currentTimestamp = getCurrentTimestamp();
        if (currentTimestamp < lastTimestamp) {
            throw new IllegalArgumentException("Clock moved backwards. Refusing to generate id.");
        }
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //毫秒内序列溢出就取新的时间戳
            if (sequence == 0L) {
                currentTimestamp = getNextTimestamp(lastTimestamp);
            }
        } else {
            //不同毫秒内，序列号置为0L
            sequence = 0L;
        }

        //更新上次生成ID的时间截
        lastTimestamp = currentTimestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return  //时间戳部分
                ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT)
                //数据中心部分
                | (datacenterId << DATACENTER_LEFT_SHIFT)
                //机器标识部分
                | (workerId << WORKERID_LEFT_SHIFT)
                //序列号部分
                | sequence;

    }

    /**
     * 获取字符串类型的下一个ID
     * @return String:id
     */
    public String nextStringId() {
        return Long.toString(nextId());
    }

    /**
     * 获取当前系统时间戳
     * @return 时间戳
     */
    private long getCurrentTimestamp() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上一次生成ID的时间戳
     * @return 下一个时间戳
     */
    private long getNextTimestamp(long lastTimestamp) {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }

    /**
     * 根据ID获取工作机器ID
     * @param id 生成的雪花ID
     * @return 工作机器标识
     */
    public long getWorkerId(long id) {
        return id >> WORKERID_LEFT_SHIFT & ~(-1L << WORKERID_BIT);
    }

    /**
     * 根据ID获取数据中心ID
     * @param id 生成的雪花ID
     * @return 数据中心ID
     */
    public long getDataCenterId(long id) {
        return id >> DATACENTER_LEFT_SHIFT & ~(-1L << DATACENTER_BIT);
    }

}