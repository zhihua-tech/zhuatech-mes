/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.controller;
import cn.zhuatech.mes.common.ApiResponse;
import cn.zhuatech.mes.service.ProductionOrderReleaseGovernanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/enterprise/mes")
public class ProductionOrderReleaseGovernanceController {
    private final ProductionOrderReleaseGovernanceService service;
    public ProductionOrderReleaseGovernanceController(ProductionOrderReleaseGovernanceService service) { this.service = service; }
    @PostMapping("/production-order-release")
    public ApiResponse<ProductionOrderReleaseGovernanceService.Assessment> assess(@Valid @RequestBody ProductionOrderReleaseGovernanceService.Request request) {
        return ApiResponse.ok("生产工单投产评估完成", service.assess(request));
    }
}
