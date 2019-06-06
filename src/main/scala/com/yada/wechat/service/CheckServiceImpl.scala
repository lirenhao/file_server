package com.yada.wechat.service

import com.typesafe.scalalogging.slf4j.LazyLogging
import com.yada.wechat.dao.CheckRepository
import com.yada.wechat.db.model.CommonDbProps
import com.yada.wechat.utils.CompareTimeUtil
import org.springframework.stereotype.Service

/**
  *
  * @author le.wu
  *         2018/8/9
  */
@Service
class CheckServiceImpl(checkRepository: CheckRepository, commonDbProps: CommonDbProps) extends CheckService with LazyLogging {
  /**
    * 验证操作ID是否有效
    *
    * @param handleId 操作ID
    * @return
    */
  override def checkHandleId(handleId: String, requestType: String): Map[String, String] = {
    if (handleId == null || "".equals(handleId)) {
      logger.info("操作ID为空，请求类型[{}],0-上传，1-下载，2-删除", requestType)
      return null
    }
    val map = checkRepository.selectByHandleId(handleId, requestType)
    if (map == null) {
      logger.info("操作ID[{}]，查询预处理表不存在数据,请求类型[{}],0-上传，1-下载，2-删除", handleId, requestType)
      return null
    }
    val fileId = map.getOrElse("fileId", "")
    val path = map.getOrElse("path", "")
    val fileName = map.getOrElse("fileName", "")
    val time = map.getOrElse("time", "")
    val date = map.getOrElse("date", "")
    if (time == null || "".equals(time)) {
      logger.info("操作ID[{}],查询预处理表插入时间为空，请求类型[{}],0-上传，1-下载，2-删除", handleId, requestType)
      return null
    }
    if (CompareTimeUtil.compareTime(date + time, commonDbProps.handleIdTime.toInt)) {
      logger.info("操作已超时，操作ID[{}],请求类型[{}],0-上传，1-下载，2-删除", handleId, requestType)
      return null
    }
    if ("0".equals(requestType)) {
      if (path == null || "".equals(path) || fileName == null || "".equals(fileName)) {
        logger.info("操作ID[{}],查询预处理表文件保存路径或者文件名为空，请求类型[{}],0-上传，1-下载，2-删除", handleId, requestType)
        return null
      }
    } else if ("1".equals(requestType) || "2".equals(requestType)) {
      if (fileId == null || "".equals(fileId)) {
        logger.info("操作ID[{}],查询预处理表文件ID为空，请求类型[{}],0-上传，1-下载，2-删除", handleId, requestType)
        return null
      }
    } else {
      logger.info("请求类型[{}]不存在,0-上传，1-下载，2-删除", handleId, requestType)
      return null
    }
    map
  }
}
