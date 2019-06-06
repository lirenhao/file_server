package com.yada.wechat.controller

import java.io.{File, FileOutputStream}

import com.typesafe.scalalogging.slf4j.LazyLogging
import com.yada.wechat.dao.UploadRepository
import com.yada.wechat.db.model.CommonDbProps
import com.yada.wechat.service.CheckService
import com.yada.wechat.utils.KeyManagerUtil
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile

/**
  * 文件上传controller
  *
  */
@Controller
@EnableAutoConfiguration
@RequestMapping(Array("/upload"))
class UploadController(commonDbProps: CommonDbProps, uploadRepository: UploadRepository, checkService: CheckService) extends LazyLogging {


  /**
    * 接收post请求
    *
    * @return 返回结果
    */
  @PostMapping
  @ResponseBody
  def home(@RequestParam(value = "file", required = false) file: MultipartFile, handleId: String): String = {
    logger.info("@UPLOAD 文件开始上传，操作ID[{}]", handleId)
    if (file.isEmpty) {
      logger.info("@UPLOAD 上传文件为[{}]", file)
      return "FAILED"
    }
    if (handleId == null || "".equals(handleId)) {
      logger.info("@UPLOAD 操作ID[{}]", handleId)
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
    val map = checkService.checkHandleId(rhandleId, "0")
    if (map == null) return "FAILED"
    var path = map.getOrElse("path", "")
    val fileName = map.getOrElse("fileName", "")
    if (!path.endsWith("/")) {
      path = path + "/"
    }
    val filePath = new File(path)
    if (!filePath.exists()) {
      filePath.mkdirs()
    }
    val fileAllPath = new File(path + fileName)
    try {
      if (fileAllPath.exists()) {
        fileAllPath.delete()
      }
      val in = file.getInputStream
      val os = new FileOutputStream(path + fileName)
      val b = new Array[Byte](1024)
      while (in.read(b) != -1) {
        os.write(b)
      }
      in.close()
      os.flush()
      os.close()
    } catch {
      case e: Exception =>
        logger.error("@UPLOAD 上传文件出现异常[{}]", e)
        return "FAILED"
    }
    val fileId = uploadRepository.insert(path + fileName)
    if (fileId != null) {
      logger.info("@UPLOAD 文件上传成功，操作ID[{}],文件ID[{}],文件路径[{}]", rhandleId, fileId, path + fileName)
      KeyManagerUtil.encrypt(fileId)
    } else {
      logger.info("@UPLOAD 文件上传完成，插入数据库失败，操作ID[{}],文件ID[{}],文件路径[{}]", rhandleId, fileId, filePath + fileName)
      "FAILED"
    }
  }
}
