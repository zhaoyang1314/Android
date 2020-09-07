package com.profitles.biz;

public enum TrType {
	CST_ADJ, //成本调整
	CUM_RADJ, //累计收货调整
	CUM_RRES, //累计收货重置
	CUM_SADJ, //累计发货调整
	CUM_SRES, //累计发货重置
	CYC_CNT, //周期盘点调整
	CYC_ERR, //周期数错误
	CYC_RCNT, //周期重盘
	ISS_CHL, //改变库存细节
	RCT_CHL, //改变库存细节
	ISS_DO, //分销订单发货
	RCT_GIT, //分销订单发货
	ISS_GIT, //分销订单收货
	RCT_DO, //分销订单收货
	ISS_FAS, //最终装配单
	RCT_FAS, //最终装配单
	ISS_PRV, //采购退回供应商
	ISS_RV, //库存退回供应商
	ISS_SO, //客户订单货物发运
	ISS_TR, //库存转移
	RCT_TR, //库存转移
	ISS_UNP, //非计划发放/收到
	RCT_UNP, //非计划发放/收到
	ISS_WO, //加工单发放/收到
	RCT_WO, //加工单发放/收到
	ORD_PO, //采购单帐目
	ORD_SO, //客户订单帐目
	ORD_SEO, //材料单帐目
	RCT_PO, //采购单收货
	RCT_G_PO, //免检零件采购单收货 //16.1.23 Lucky
	RCT_RS, //库存退货
	RCT_SOR, //库存客户订单退回
	RJCT_WO, //加工单拒绝
	TAG_CNT, //实际库存更新
	WIP_ADJ, //在制品调整
	WO_CLOSE, //加工单结算
	ISS_PK, RCT_PK, ISS_BIN, //上架（库存减少）
	RCT_BIN
	//上架（库存增加）
}
