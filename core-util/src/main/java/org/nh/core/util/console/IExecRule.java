package org.nh.core.util.console;
import java.util.Collection;

/**
 * Created by root on 4/21/17.
 */
public interface IExecRule {
    /**
     * 根据规则得到一个ip
     *
     * @param machineStatusList 待选择列表
     * @return
     */
    RuleResult pick(Collection<MachineStatus> machineStatusList);


}
