/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;
import org.junit.jupiter.api.Test;import static org.assertj.core.api.Assertions.assertThat;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class WorkOrderChangeGovernanceServiceTest{
 private final WorkOrderChangeGovernanceService service=new WorkOrderChangeGovernanceService();
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private WorkOrderChangeGovernanceService.Request request(boolean effective,boolean notify,boolean rollback,boolean evidence){return new WorkOrderChangeGovernanceService.Request("CHG-1","eng","director",true,true,true,true,true,true,true,false,true,false,true,effective,notify,rollback,evidence);}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void releasesControlledChange(){assertThat(service.assess(request(true,true,true,true)).decision()).isEqualTo(WorkOrderChangeGovernanceService.Decision.RELEASE_CHANGE);}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void reviewsIncompleteDeploymentPlan(){var a=service.assess(request(false,false,false,false));assertThat(a.decision()).isEqualTo(WorkOrderChangeGovernanceService.Decision.REVIEW);assertThat(a.actions()).hasSize(4);}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void blocksUnsafeActiveWipChange(){var r=new WorkOrderChangeGovernanceService.Request("CHG-2","same","same",false,false,false,false,false,false,false,true,false,true,false,true,true,true,true);var a=service.assess(r);assertThat(a.decision()).isEqualTo(WorkOrderChangeGovernanceService.Decision.BLOCKED);assertThat(a.riskLevel()).isEqualTo(WorkOrderChangeGovernanceService.RiskLevel.HIGH);assertThat(a.blockers()).hasSizeGreaterThanOrEqualTo(9);}
}
