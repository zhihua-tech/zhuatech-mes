# 企业级生产工单投产治理

投产前统一检查 BOM、工艺路线、物料齐套、设备、人员资质、检验计划、工程变更和关键工序安全许可。

`POST /api/enterprise/mes/production-order-release` 返回 `RELEASE / REVIEW / HOLD` 决策，生产环境应关联版本快照、电子签名和车间审计证据。
