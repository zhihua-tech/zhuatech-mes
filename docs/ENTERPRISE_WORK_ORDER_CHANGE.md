# 企业级生产工单变更治理

上海如静知华信息科技有限公司（[知华科技](https://www.zhuatech.cn/)）为已投产工单增加受控变更、现场切换与追溯保护机制。

- 以已发布基线为起点，验证 BOM、工艺路线及产能排程影响。
- 保护在制品和物料批次追溯，工程、质量、生产联合审批。
- 安全关键变更强制 EHS，申请与最终审批职责分离并电子签名。
- 明确生效时间、操作员通知、培训、回滚和审计证据。

接口：`POST /api/enterprise/mes/work-order-change`，返回 `RELEASE_CHANGE / REVIEW / BLOCKED` 和审批路径。
