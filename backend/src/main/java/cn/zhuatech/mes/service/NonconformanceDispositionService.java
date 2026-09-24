/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 对生产不合格品执行隔离、追溯、评审和处置放行治理。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class NonconformanceDispositionService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        if (request.dispositionQuantity().compareTo(request.reportedQuantity()) > 0) {
            blockers.add("处置数量不能超过不合格报告数量");
        }
        if (!request.lotQuarantined()) blockers.add("不合格批次尚未隔离");
        if (!request.containmentComplete()) blockers.add("遏制措施尚未完成");
        if (!request.traceScopeConfirmed()) blockers.add("受影响批次和在制品追溯范围未确认");
        if (request.severity() == Severity.CRITICAL && !request.materialReviewBoardApproved()) {
            blockers.add("严重不合格必须经过材料评审委员会批准");
        }
        switch (request.disposition()) {
            case USE_AS_IS -> {
                if (!request.materialReviewBoardApproved()) blockers.add("让步接收缺少材料评审批准");
                if (request.severity() != Severity.MINOR && !request.customerConcessionApproved()) {
                    blockers.add("重大或严重不合格让步接收缺少客户批准");
                }
            }
            case REWORK -> {
                if (!request.reworkInstructionReleased()) blockers.add("返工作业指导书尚未受控发布");
            }
            case SCRAP -> {
                if (!request.scrapAuthorized()) blockers.add("报废处置缺少授权");
            }
            case RETURN_TO_SUPPLIER -> {
                if (!request.supplierReturnAuthorized()) blockers.add("退供应商缺少退货授权或 RMA");
            }
        }
        if (!request.inventoryMovementReady()) blockers.add("处置后的库存移动或状态变更尚未准备");
        if (!request.electronicSignatureComplete()) blockers.add("质量和生产电子签名链不完整");
        if (!request.rootCauseRecorded()) actions.add("登记根因分析和纠正预防措施负责人");
        if (!request.auditEvidenceAttached()) actions.add("归档检验、隔离、评审、处置和签名证据");
        Decision decision = !blockers.isEmpty() ? Decision.BLOCKED
                : !actions.isEmpty() ? Decision.REVIEW : Decision.EXECUTE;
        String route = request.severity() == Severity.CRITICAL ? "质量经理→生产负责人→材料评审委员会"
                : request.disposition() == Disposition.USE_AS_IS ? "质量工程师→质量经理" : "质量工程师";
        return new Assessment(request.ncrNo(), request.lotNo(), request.disposition(), decision, route,
                List.copyOf(blockers), List.copyOf(actions));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String ncrNo, @NotBlank String lotNo,
                          @NotNull @DecimalMin("0.0001") BigDecimal reportedQuantity,
                          @NotNull @DecimalMin("0.0001") BigDecimal dispositionQuantity,
                          @NotNull Severity severity, @NotNull Disposition disposition,
                          boolean lotQuarantined, boolean containmentComplete,
                          boolean traceScopeConfirmed, boolean rootCauseRecorded,
                          boolean materialReviewBoardApproved, boolean customerConcessionApproved,
                          boolean reworkInstructionReleased, boolean scrapAuthorized,
                          boolean supplierReturnAuthorized, boolean inventoryMovementReady,
                          boolean electronicSignatureComplete, boolean auditEvidenceAttached) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Assessment(String ncrNo, String lotNo, Disposition disposition,
                             Decision decision, String approvalRoute,
                             List<String> blockers, List<String> actions) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Severity { MINOR, MAJOR, CRITICAL }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Disposition { USE_AS_IS, REWORK, SCRAP, RETURN_TO_SUPPLIER }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Decision { EXECUTE, REVIEW, BLOCKED }
}
