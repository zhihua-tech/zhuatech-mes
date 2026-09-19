/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;
import jakarta.validation.constraints.*;import org.springframework.stereotype.Service;import java.util.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class WorkOrderChangeGovernanceService{
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public Assessment assess(Request r){
  List<String> blockers=new ArrayList<>();List<String> actions=new ArrayList<>();
  if(!r.releasedBaselineExists())blockers.add("工单缺少已发布基线");
  if(!r.bomAndRoutingRevisionValid())blockers.add("BOM 或工艺路线版本无效");
  if(!r.wipImpactAssessed())blockers.add("尚未评估在制品影响");
  if(!r.materialTraceabilityPreserved())blockers.add("变更后无法保持物料批次追溯");
  if(!r.engineeringApproved())blockers.add("工程变更未获批准");
  if(!r.qualityApproved())blockers.add("质量影响未获批准");
  if(!r.capacityAndScheduleChecked())blockers.add("产能和排程影响未校验");
  if(r.safetyCritical()&&!r.ehsApproved())blockers.add("安全关键变更必须通过 EHS 审批");
  if(r.requesterId().equals(r.approverId()))blockers.add("变更申请人与最终审批人必须职责分离");
  if(!r.electronicSignatureComplete())blockers.add("电子签名链不完整");
  if(!r.effectiveTimeScheduled())actions.add("设置变更生效时间和受影响工位");
  if(!r.operatorNotificationPlanned())actions.add("生成班组培训与操作员通知任务");
  if(!r.rollbackPlanReady())actions.add("补充失败回滚及旧版本隔离方案");
  if(!r.auditEvidenceAttached())actions.add("归档变更单、影响评估和批准证据");
  RiskLevel risk=r.safetyCritical()||r.hasActiveWip()?RiskLevel.HIGH:RiskLevel.NORMAL;
  Decision decision=!blockers.isEmpty()?Decision.BLOCKED:!actions.isEmpty()?Decision.REVIEW:Decision.RELEASE_CHANGE;
  String route=risk==RiskLevel.HIGH?"工程→质量→生产→EHS/工厂负责人":"工程→质量→生产";
  return new Assessment(r.changeNo(),decision,risk,route,List.copyOf(blockers),List.copyOf(actions));
 }
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public record Request(@NotBlank String changeNo,@NotBlank String requesterId,@NotBlank String approverId,
  boolean releasedBaselineExists,boolean bomAndRoutingRevisionValid,boolean wipImpactAssessed,
  boolean materialTraceabilityPreserved,boolean engineeringApproved,boolean qualityApproved,
  boolean capacityAndScheduleChecked,boolean safetyCritical,boolean ehsApproved,boolean hasActiveWip,
  boolean electronicSignatureComplete,boolean effectiveTimeScheduled,boolean operatorNotificationPlanned,
  boolean rollbackPlanReady,boolean auditEvidenceAttached){}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public record Assessment(String changeNo,Decision decision,RiskLevel riskLevel,String approvalRoute,List<String> blockers,List<String> actions){}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public enum Decision{RELEASE_CHANGE,REVIEW,BLOCKED}/**
                                                     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                     */
public enum RiskLevel{NORMAL,HIGH}
}
