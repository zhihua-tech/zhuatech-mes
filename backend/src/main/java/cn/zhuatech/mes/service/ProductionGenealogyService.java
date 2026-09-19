/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 在成品批次入账前校验工单、BOM 与投入物料批次谱系。 */
@Service
public class ProductionGenealogyService {
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        Set<String> identities = new HashSet<>();
        if (request.outputLotAlreadyExists()) blockers.add("成品批次编号已经存在");
        if (!request.bomRevisionValid()) blockers.add("BOM 版本无效或与工单不一致");
        if (!request.routingRevisionValid()) blockers.add("工艺路线版本无效或与工单不一致");
        if (!request.equipmentTraceCaptured()) blockers.add("缺少设备或产线追溯标识");
        if (!request.operatorTraceCaptured()) blockers.add("缺少操作员或班组追溯标识");
        if (!request.qualityReleaseComplete()) blockers.add("成品质量放行尚未完成");
        if (!request.electronicSignatureComplete()) blockers.add("批次入账电子签名链不完整");
        for (MaterialLot material : request.materialLots()) {
            String identity = material.materialCode() + ":" + material.lotNo();
            if (!identities.add(identity)) blockers.add("投入物料批次重复: " + identity);
            if (!material.qaReleased()) blockers.add(identity + " 未通过质量放行");
            if (material.expired()) blockers.add(identity + " 已过期");
            if (!material.issuedToWorkOrder()) blockers.add(identity + " 未发料到当前工单");
            if (!material.traceCodeCaptured()) blockers.add(identity + " 缺少内部追溯码");
            BigDecimal allowed = material.bomQuantity().multiply(BigDecimal.ONE.add(
                    material.allowedOverIssuePercent().divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP)));
            if (material.consumedQuantity().compareTo(allowed) > 0) blockers.add(identity + " 超出 BOM 允许耗用量");
            if (material.criticalMaterial() && !material.supplierLotCaptured()) {
                blockers.add(identity + " 关键物料缺少供应商批次");
            } else if (!material.supplierLotCaptured()) {
                actions.add(identity + " 建议补充供应商批次以增强反向追溯");
            }
        }
        if (!request.auditEvidenceAttached()) actions.add("归档报工、投料、检验和电子签名证据");
        if (!request.downstreamLabelReady()) actions.add("生成成品批次标签并同步仓储系统");
        Decision decision = !blockers.isEmpty() ? Decision.BLOCKED
                : !actions.isEmpty() ? Decision.REVIEW : Decision.RECORD;
        return new Assessment(request.genealogyId(), request.outputLotNo(), decision,
                request.materialLots().size(), List.copyOf(blockers), List.copyOf(actions));
    }

    public record Request(@NotBlank String genealogyId, @NotBlank String workOrderNo,
                          @NotBlank String outputLotNo,
                          @NotNull @DecimalMin("0.0001") BigDecimal producedQuantity,
                          boolean outputLotAlreadyExists, boolean bomRevisionValid,
                          boolean routingRevisionValid, boolean equipmentTraceCaptured,
                          boolean operatorTraceCaptured, boolean qualityReleaseComplete,
                          boolean electronicSignatureComplete, boolean auditEvidenceAttached,
                          boolean downstreamLabelReady,
                          @NotEmpty List<@Valid MaterialLot> materialLots) {}

    public record MaterialLot(@NotBlank String materialCode, @NotBlank String lotNo,
                              @NotNull @DecimalMin("0.0001") BigDecimal bomQuantity,
                              @NotNull @DecimalMin("0.0001") BigDecimal consumedQuantity,
                              @NotNull @DecimalMin("0.0") BigDecimal allowedOverIssuePercent,
                              boolean qaReleased, boolean expired, boolean issuedToWorkOrder,
                              boolean traceCodeCaptured, boolean criticalMaterial,
                              boolean supplierLotCaptured) {}

    public record Assessment(String genealogyId, String outputLotNo, Decision decision,
                             int inputLotCount, List<String> blockers, List<String> actions) {}

    public enum Decision { RECORD, REVIEW, BLOCKED }
}
