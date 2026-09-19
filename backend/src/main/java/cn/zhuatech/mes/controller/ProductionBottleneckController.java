/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.controller;

import cn.zhuatech.mes.common.ApiResponse;
import cn.zhuatech.mes.service.ProductionBottleneckService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/mes/insights")
public class ProductionBottleneckController {
    private final ProductionBottleneckService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ProductionBottleneckController(ProductionBottleneckService service) { this.service = service; }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/bottleneck")
    public ApiResponse<ProductionBottleneckService.Result> analyze(
        @Valid @RequestBody ProductionBottleneckService.Request request) {
        return ApiResponse.ok(service.analyze(request));
    }
}
