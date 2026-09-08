/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.controller;
import cn.zhuatech.mes.common.ApiResponse;import cn.zhuatech.mes.service.WorkOrderChangeGovernanceService;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/enterprise/mes")public class WorkOrderChangeGovernanceController{
 private final WorkOrderChangeGovernanceService service;public WorkOrderChangeGovernanceController(WorkOrderChangeGovernanceService service){this.service=service;}
 @PostMapping("/work-order-change")public ApiResponse<WorkOrderChangeGovernanceService.Assessment> assess(@Valid @RequestBody WorkOrderChangeGovernanceService.Request request){return ApiResponse.ok("生产工单变更评估完成",service.assess(request));}
}
