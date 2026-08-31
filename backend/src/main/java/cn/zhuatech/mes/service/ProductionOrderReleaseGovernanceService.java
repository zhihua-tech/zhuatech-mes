/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductionOrderReleaseGovernanceService {
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        if (!request.bomVersionApproved()) blockers.add("BOM 版本未批准");
        if (!request.routingApproved()) blockers.add("工艺路线未批准");
        if (!request.materialAvailable()) blockers.add("关键物料未齐套");
        if (!request.equipmentQualified()) blockers.add("设备点检或校准不合格");
        if (!request.operatorsQualified()) blockers.add("操作人员资质不满足工序要求");
        if (!request.inspectionPlanReady()) actions.add("配置首检、巡检与放行规则");
        if (!request.changeOrdersClosed()) actions.add("关闭影响本工单的工程变更");
        if (request.criticalProcess() && !request.safetyPermitApproved()) blockers.add("关键工序安全许可未批准");

        Decision decision = !blockers.isEmpty() ? Decision.HOLD
                : !actions.isEmpty() ? Decision.REVIEW : Decision.RELEASE;
        return new Assessment(request.productionOrderNo(), decision,
                List.copyOf(blockers), List.copyOf(actions));
    }
    public record Request(@NotBlank String productionOrderNo, boolean bomVersionApproved,
                          boolean routingApproved, boolean materialAvailable,
                          boolean equipmentQualified, boolean operatorsQualified,
                          boolean inspectionPlanReady, boolean changeOrdersClosed,
                          boolean criticalProcess, boolean safetyPermitApproved) {}
    public record Assessment(String productionOrderNo, Decision decision,
                             List<String> blockers, List<String> actions) {}
    public enum Decision { RELEASE, REVIEW, HOLD }
}
