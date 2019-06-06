package com.yada.wechat.controller

import java.io.File

import com.typesafe.scalalogging.slf4j.LazyLogging
import com.yada.wechat.dao.DeleteRepository
import com.yada.wechat.service.CheckService
import com.yada.wechat.utils.KeyManagerUtil
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}

/**
  * 文件删除
  *
  * @author le.wu
  *         2018/8/29
  */
@Controller
@EnableAutoConfiguration
@RequestMapping(Array("/delete"))
class DeleteController(checkService: CheckService, deleteRepository: DeleteRepository) extends LazyLogging {
  /**
    * 接收请求
    *
    * @return 返回结果
    */
  @RequestMapping
  @ResponseBody
  def home(handleId: String): String = {
    if (handleId == null || "".equals(handleId)) {
      logger.info("@DELETE 删除文件，操作ID[{}]", handleId)
      return "FAILED"
    }
    var rhandleId = ""
    try {
      rhandleId = KeyManagerUtil.decrypt(handleId)
    } catch {
      case e: Exception =>
        logger.error("@DOWNLOAD 下载文件，操作ID解密异常", handleId, e)
        return "FAILED"
    }
    logger.info("@DELETE 删除文件，操作ID[{}]", rhandleId)
    val map = checkService.checkHandleId(rhandleId, "2")
    if (map == null) return "FAILED"
    val fileId = map.getOrElse("fileId", "")
    val path = deleteRepository.selectFileInfo(fileId)
    if (path == null) {
      logger.info("@DELETE 查询无此文件,操作ID[{}],文件ID[{}]", rhandleId, fileId)
      return "FAILED"
    }
    val file = new File(path)
    if (file.exists()) {
      if (deleteRepository.deleteFileInfo(fileId) > 0) {
        logger.info("@DELETE 文件删除成功，文件路径[{}],操作ID[{}],文件ID[{}]", path, rhandleId, fileId)
        if (!file.delete()) {
          logger.info("@DELETE 文件删除失败,文件路径[{}],操作ID[{}],文件ID[{}]", path, rhandleId, fileId)
        }
        return "SUCCESS"
      } else {
        logger.info("@DELETE 文件删除成功,文件信息表数据删除失败，文件路径[{}],操作ID[{}],文件ID[{}]", path, rhandleId, fileId)
        return "FAILED"
      }
      "FAILED"
    } else {
      logger.info("@DELETE 文件不存在,文件路径[{}],操作ID[{}],文件ID[{}]", path, rhandleId, fileId)
      "FAILED"
    }
  }
}
