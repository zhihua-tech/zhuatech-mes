/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
class ProductionOrderReleaseGovernanceServiceTest {
    private final ProductionOrderReleaseGovernanceService service = new ProductionOrderReleaseGovernanceService();
    @Test void releasesReadyProductionOrder() {
        var result = service.assess(new ProductionOrderReleaseGovernanceService.Request("MO-001", true, true, true, true, true, true, true, true, true));
        assertThat(result.decision()).isEqualTo(ProductionOrderReleaseGovernanceService.Decision.RELEASE);
    }
    @Test void holdsUnsafeProductionOrder() {
        var result = service.assess(new ProductionOrderReleaseGovernanceService.Request("MO-002", false, false, false, false, false, false, false, true, false));
        assertThat(result.decision()).isEqualTo(ProductionOrderReleaseGovernanceService.Decision.HOLD);
        assertThat(result.blockers()).hasSize(6);
        assertThat(result.actions()).hasSize(2);
    }
}
