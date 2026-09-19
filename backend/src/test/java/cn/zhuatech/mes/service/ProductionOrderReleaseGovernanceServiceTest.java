/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class ProductionOrderReleaseGovernanceServiceTest {
    private final ProductionOrderReleaseGovernanceService service = new ProductionOrderReleaseGovernanceService();
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void releasesReadyProductionOrder() {
        var result = service.assess(new ProductionOrderReleaseGovernanceService.Request("MO-001", true, true, true, true, true, true, true, true, true));
        assertThat(result.decision()).isEqualTo(ProductionOrderReleaseGovernanceService.Decision.RELEASE);
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void holdsUnsafeProductionOrder() {
        var result = service.assess(new ProductionOrderReleaseGovernanceService.Request("MO-002", false, false, false, false, false, false, false, true, false));
        assertThat(result.decision()).isEqualTo(ProductionOrderReleaseGovernanceService.Decision.HOLD);
        assertThat(result.blockers()).hasSize(6);
        assertThat(result.actions()).hasSize(2);
    }
}
