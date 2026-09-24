/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class NonconformanceDispositionServiceTest {
    private final NonconformanceDispositionService service = new NonconformanceDispositionService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void executesControlledReworkDisposition() {
        var result = service.assess(request(NonconformanceDispositionService.Severity.MAJOR,
                NonconformanceDispositionService.Disposition.REWORK, true, true, true));
        assertThat(result.decision()).isEqualTo(NonconformanceDispositionService.Decision.EXECUTE);
        assertThat(result.blockers()).isEmpty();
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void reviewsDispositionUntilRootCauseIsRecorded() {
        var result = service.assess(request(NonconformanceDispositionService.Severity.MINOR,
                NonconformanceDispositionService.Disposition.REWORK, false, true, true));
        assertThat(result.decision()).isEqualTo(NonconformanceDispositionService.Decision.REVIEW);
        assertThat(result.actions()).anyMatch(item -> item.contains("根因"));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void blocksCriticalConcessionWithoutRequiredApprovals() {
        var result = service.assess(request(NonconformanceDispositionService.Severity.CRITICAL,
                NonconformanceDispositionService.Disposition.USE_AS_IS, true, false, false));
        assertThat(result.decision()).isEqualTo(NonconformanceDispositionService.Decision.BLOCKED);
        assertThat(result.blockers()).anyMatch(item -> item.contains("材料评审"));
        assertThat(result.blockers()).anyMatch(item -> item.contains("客户批准"));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private NonconformanceDispositionService.Request request(
            NonconformanceDispositionService.Severity severity,
            NonconformanceDispositionService.Disposition disposition,
            boolean rootCause, boolean reviewApproved, boolean customerApproved) {
        return new NonconformanceDispositionService.Request("NCR-100", "LOT-100",
                new BigDecimal("10"), new BigDecimal("10"), severity, disposition,
                true, true, true, rootCause, reviewApproved, customerApproved,
                true, true, true, true, true, true);
    }
}
