package com.yada.wechat.service

/**
  * 权限认证
  *
  * @author le.wu
  *         2018/8/9
  */
trait CheckService {
  /**
    * 验证操作ID是否有效
    *
    * @param handleId 操作ID
    * @return
    */
  def checkHandleId(handleId: String, requestType: String): Map[String, String]
}
