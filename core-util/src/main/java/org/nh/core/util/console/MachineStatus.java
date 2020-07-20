package org.nh.core.util.console;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 4/21/17.
 */
public class MachineStatus implements Serializable {

    /**
     * ip addr
     */
    private String ip;

    /**
     * 计算机用户CPU使用率(%)
     */
    private double cpuUserUsagePercent = 0.0;

    /**
     * 计算机系统CPU使用率(%)
     */
    private double cpuSysUsagePercent = 0.0;

    /**
     * 计算机内存容量(KB)
     */
    private double memoryTotal = 0.0;

    /**
     * 计算机已使用内存容量(KB)
     */
    private double memoryUsage = 0.0;

    /**
     * 计算机内存使用率(计算机已使用内存容量/计算机内存容量)
     */
    private double memoryUsagePercent = 0.0;

    /**
     * 复合使用率
     *
     * @return
     */
    private double comboUsagePercent = 0.0;

    /**
     * 硬盘使用率
     */
    private Map<String, Double> diskUsagePercent = new HashMap<>();

    public MachineStatus() {
    }

    public MachineStatus(String ip) {
        this.ip = ip;
    }

    public MachineStatus(String ip, double cpuUserUsagePercent, double cpuSysUsagePercent, double memoryTotal,
                         double memoryUsage, double memoryUsagePercent, double comboUsagePercent,
                         Map<String, Double> diskUsagePercent) {
        this.ip = ip;
        this.cpuUserUsagePercent = cpuUserUsagePercent;
        this.cpuSysUsagePercent = cpuSysUsagePercent;
        this.memoryTotal = memoryTotal;
        this.memoryUsage = memoryUsage;
        this.memoryUsagePercent = memoryUsagePercent;
        this.comboUsagePercent = comboUsagePercent;
        this.diskUsagePercent = diskUsagePercent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public double getCpuUserUsagePercent() {
        return cpuUserUsagePercent;
    }

    public void setCpuUserUsagePercent(double cpuUserUsagePercent) {
        this.cpuUserUsagePercent = cpuUserUsagePercent;
    }

    public double getCpuSysUsagePercent() {
        return cpuSysUsagePercent;
    }

    public void setCpuSysUsagePercent(double cpuSysUsagePercent) {
        this.cpuSysUsagePercent = cpuSysUsagePercent;
    }

    public double getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(double memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public double getMemoryUsagePercent() {
        return memoryUsagePercent;
    }

    public void setMemoryUsagePercent(double memoryUsagePercent) {
        this.memoryUsagePercent = memoryUsagePercent;
    }

    public double getComboUsagePercent() {
        return comboUsagePercent;
    }

    public void setComboUsagePercent(double comboUsagePercent) {
        this.comboUsagePercent = comboUsagePercent;
    }

    public Map<String, Double> getDiskUsagePercent() {
        return diskUsagePercent;
    }

    public void setDiskUsagePercent(Map<String, Double> diskUsagePercent) {
        this.diskUsagePercent = diskUsagePercent;
    }

    @Override
    public String toString() {
        return "MachineStatus{" +
                "ip='" + ip + '\'' +
                ", cpuUserUsagePercent=" + cpuUserUsagePercent +
                ", cpuSysUsagePercent=" + cpuSysUsagePercent +
                ", memoryTotal=" + memoryTotal +
                ", memoryUsage=" + memoryUsage +
                ", memoryUsagePercent=" + memoryUsagePercent +
                ", comboUsagePercent=" + comboUsagePercent +
                ", diskUsagePercent=" + diskUsagePercent +
                '}';
    }
}

