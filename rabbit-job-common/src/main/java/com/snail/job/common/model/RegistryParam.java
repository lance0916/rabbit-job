package com.snail.job.common.model;

/**
 * @author WuQinglong
 */
public class RegistryParam {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 执行器的地址
     */
    private String address;

    public RegistryParam(String appName, String address) {
        this.appName = appName;
        this.address = address;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "RegistryParam{" +
                "appName='" + appName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
