package com.yada.wechat.controller

import java.io.{File, FileInputStream}

import com.typesafe.scalalogging.slf4j.LazyLogging
import com.yada.wechat.dao.DownloadRepository
import com.yada.wechat.db.model.CommonDbProps
import com.yada.wechat.service.CheckService
import com.yada.wechat.utils.KeyManagerUtil
import javax.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}

/**
  * 文件下载
  *
  * @author le.wu
  *         2018/8/10
  */
@Controller
@EnableAutoConfiguration
@RequestMapping(Array("/download"))
class DownloadController(commonDbProps: CommonDbProps, downloadRepository: DownloadRepository, checkService: CheckService) extends LazyLogging {
  /**
    * 接收请求
    *
    * @return 返回结果
    */
  @RequestMapping
  @ResponseBody
  def home(handleId: String, response: HttpServletResponse): Unit = {
    if (handleId == null || "".equals(handleId)) {
      writeFile(response, commonDbProps.failedImagePath)
      logger.error("@DOWNLOAD 下载文件，操作ID[{}]", handleId)
    }
    var rhandleId = ""
    try {
      rhandleId = KeyManagerUtil.decrypt(handleId)
    } catch {
      case e: Exception =>
        logger.error("@DOWNLOAD 下载文件，操作ID解密异常", handleId, e)
    }
    logger.info("@DOWNLOAD 下载文件，操作ID[{}]", rhandleId)
    val map = checkService.checkHandleId(rhandleId, "1")
    if (map == null) {
      writeFile(response, commonDbProps.failedImagePath)
    }
    val fileId = map.getOrElse("fileId", "")
    val path = downloadRepository.selectFileInfo(fileId)
    val file = new File(path)
    if (path == null || !file.exists()) {
      writeFile(response, commonDbProps.failedImagePath)
      logger.info("@DOWNLOAD 查询无此文件,操作ID[{}],文件ID[{}]", rhandleId, fileId)
    }
    writeFile(response, path)
    logger.info("@DOWNLOAD 下载文件完成，操作ID[{}]，文件ID[{}]，文件路径[{}]", rhandleId, fileId, path)
  }

  /**
    * 返回指定目录的
    *
    * @param path 文件路径
    */
  def writeFile(response: HttpServletResponse, path: String): Unit = {
    val file = new File(path)
    if (file.exists()) {
      val in = new FileInputStream(file)
      val os = response.getOutputStream
      val b = new Array[Byte](1024)
      while (in.read(b) != -1) {
        os.write(b)
      }
      in.close()
      os.flush()
      os.close()
    } else {
      logger.info("@DOWNLOAD 文件不存在，文件路径[{}]", path)
    }
  }
}
