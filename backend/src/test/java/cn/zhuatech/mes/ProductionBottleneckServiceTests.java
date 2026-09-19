/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes;

import cn.zhuatech.mes.service.ProductionBottleneckService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class ProductionBottleneckServiceTests {
    private final ProductionBottleneckService service = new ProductionBottleneckService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void detectsCriticalBottleneck() {
        var result = service.analyze(new ProductionBottleneckService.Request("WC-ASSY-01",
            new BigDecimal("60"), new BigDecimal("78"), 14, 75, 94));
        assertThat(result.level()).isEqualTo("CRITICAL");
        assertThat(result.cycleVarianceRate()).isEqualByComparingTo("0.3000");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void acceptsStableWorkCenter() {
        var result = service.analyze(new ProductionBottleneckService.Request("WC-PACK-01",
            new BigDecimal("45"), new BigDecimal("46"), 2, 5, 72));
        assertThat(result.level()).isEqualTo("NORMAL");
        assertThat(result.bottleneckScore()).isZero();
    }
}
