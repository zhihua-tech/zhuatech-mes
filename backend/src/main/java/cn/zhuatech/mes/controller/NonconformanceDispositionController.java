/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.controller;

import cn.zhuatech.mes.common.ApiResponse;
import cn.zhuatech.mes.service.NonconformanceDispositionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/enterprise/mes")
public class NonconformanceDispositionController {
    private final NonconformanceDispositionService service;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public NonconformanceDispositionController(NonconformanceDispositionService service) {
        this.service = service;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/nonconformance-disposition")
    public ApiResponse<NonconformanceDispositionService.Assessment> assess(
            @Valid @RequestBody NonconformanceDispositionService.Request request) {
        return ApiResponse.ok("不合格品处置评估完成", service.assess(request));
    }
}
