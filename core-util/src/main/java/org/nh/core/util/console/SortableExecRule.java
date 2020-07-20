package org.nh.core.util.console;

import com.gome.gscm.diamond.Diamond;
import com.gome.gscm.diamond.DiamondException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by root on 3/12/18.
 */

interface IData{
    double getData(MachineStatus m);
    double getDelta();
    double getMax();
    String getDiamondKey();
}

class Data<T>{
    T data;
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

public abstract class SortableExecRule implements IExecRule{

    private static final Logger logger = Logger.getLogger(SortableExecRule.class);

    public boolean sort(Iterable<MachineStatus> machineStatusList, IData data, Data<Boolean> isSame, List<RankBean> sortData){
        for (MachineStatus m : machineStatusList) {
            sortData.add(new RankBean(m.getIp(), BigDecimal.valueOf(data.getData(m)).setScale(2, RoundingMode.UP).doubleValue()));
        }
        Collections.sort(sortData, new Comparator<RankBean>() {
            @Override
            public int compare(RankBean o1, RankBean o2) {
                return (int) (o1.getPercent() - o2.getPercent());
            }
        });
        double firstVal = 0.0;
        boolean same = true;
        int i = 0;
        double dela = data.getDelta();
        try {
            String val = Diamond.getString(data.getDiamondKey());
            if(val != null && !"".equals(val))
                dela = Double.parseDouble(val);
        } catch (DiamondException e) {
            logger.error(e.getMessage(), e);
        }
        for (MachineStatus b : machineStatusList) {
            if (i == 0) {
                firstVal = data.getData(b);
            } else if (BigDecimal.valueOf(data.getData(b)).equals(BigDecimal.valueOf(firstVal))
                    || (Math.abs(data.getData(b) - firstVal) < dela)) {
                same = true;
            } else {
                same = false;
                break;
            }
            i++;
        }
        MachineStatus machineStatus = machineStatusList.iterator().next();
        isSame.setData(same);
        return !(same && data.getData(machineStatus) > data.getMax());
    }


}
