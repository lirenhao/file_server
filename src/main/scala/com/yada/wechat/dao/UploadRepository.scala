package com.yada.wechat.dao

import java.text.SimpleDateFormat
import java.util.{Calendar, Date, UUID}

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/**
  *
  * @author le.wu
  *         2018/8/10
  */
@Repository
class UploadRepository(jdbcTemplate: JdbcTemplate) {
  /**
    * 插入数据
    *
    * @param filePath 文件保存路径
    * @return
    */
  def insert(filePath: String): String = {
    val cal = Calendar.getInstance
    val date = new SimpleDateFormat("yyyyMMdd").format(cal.getTime)
    val time = new SimpleDateFormat("HHmmss").format(cal.getTime)
    val uuid: String = UUID.randomUUID().toString.replace("-", "")
    val sql =
      s""" insert into T_B_FILE_INFO(FILEID,FILEPATH,IN_DATE,IN_TIME)
         | values
         |  ('$uuid','$filePath','$date','$time')""".stripMargin
    if (jdbcTemplate.update(sql) > 0) {
      uuid
    } else {
      null
    }
  }
}
