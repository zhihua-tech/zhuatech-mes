/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class ProductionGenealogyServiceTest {
    private final ProductionGenealogyService service = new ProductionGenealogyService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void recordsCompleteProductionGenealogy() {
        var result = service.assess(request(material("RM-1", "LOT-1", "10", true, true), true, true));
        assertThat(result.decision()).isEqualTo(ProductionGenealogyService.Decision.RECORD);
        assertThat(result.inputLotCount()).isEqualTo(1);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void reviewsNonCriticalMissingSupplierTraceAndClosureTasks() {
        var result = service.assess(request(material("RM-1", "LOT-1", "10", false, false), false, false));
        assertThat(result.decision()).isEqualTo(ProductionGenealogyService.Decision.REVIEW);
        assertThat(result.actions()).hasSize(3);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void blocksExpiredUnreleasedAndOverIssuedCriticalMaterial() {
        var material = new ProductionGenealogyService.MaterialLot("RM-1", "LOT-1",
                new BigDecimal("10"), new BigDecimal("12"), new BigDecimal("5"),
                false, true, true, true, true, false);
        var result = service.assess(request(material, true, true));
        assertThat(result.decision()).isEqualTo(ProductionGenealogyService.Decision.BLOCKED);
        assertThat(result.blockers()).hasSize(4);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void blocksDuplicateInputLotIdentity() {
        var material = material("RM-1", "LOT-1", "10", true, true);
        var input = request(material, true, true);
        var duplicated = new ProductionGenealogyService.Request(input.genealogyId(), input.workOrderNo(),
                input.outputLotNo(), input.producedQuantity(), false, true, true, true, true,
                true, true, true, true, List.of(material, material));
        var result = service.assess(duplicated);
        assertThat(result.decision()).isEqualTo(ProductionGenealogyService.Decision.BLOCKED);
        assertThat(result.blockers()).anyMatch(item -> item.contains("投入物料批次重复"));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private ProductionGenealogyService.Request request(ProductionGenealogyService.MaterialLot material,
                                                        boolean evidence, boolean label) {
        return new ProductionGenealogyService.Request("GEN-1", "WO-1", "FG-LOT-1",
                new BigDecimal("8"), false, true, true, true, true, true, true,
                evidence, label, List.of(material));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private ProductionGenealogyService.MaterialLot material(String code, String lot, String consumed,
                                                             boolean critical, boolean supplierLot) {
        return new ProductionGenealogyService.MaterialLot(code, lot, new BigDecimal("10"),
                new BigDecimal(consumed), new BigDecimal("5"), true, false, true, true,
                critical, supplierLot);
    }
}
