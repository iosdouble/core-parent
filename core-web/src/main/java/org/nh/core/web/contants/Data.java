package org.nh.core.web.contants;

/**
 * @Classname Data
 * @Description TODO 数据相关常量
 * @Date 2020/1/15 3:13 PM
 * @Created by nihui
 */
public class Data {
    public static class Status{
        public static final int ENABLED=1;//生效
        public static final int DISABLED=0;//失效
    }

    public static class StatusV2{
        public static final int ING=0;//进行中
        public static final int YES=1;//生效
        public static final int NO=2;//生效
    }

    public static class Delete{
        public static final int YES=1;//生效
        public static final int NO=0;//失效
    }

    public static final class Env{
        public static final int UNKNOWN=0;
        public static final int PROD=1;
        public static final int UAT=2;
        public static final int ALPHA=3;
    }
}
