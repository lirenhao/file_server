package com.yada.wechat.dao

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/**
  *
  * @author le.wu
  *         2018/8/22
  */
@Repository
class DownloadRepository(jdbcTemplate: JdbcTemplate) {
  /**
    * 查询文件信息表
    *
    * @param fileId 文件ID
    * @return
    */
  def selectFileInfo(fileId: String): String = {
    val sql =s"""select * from t_b_file_info t where t.FILEID='$fileId'""".stripMargin
    val rs = jdbcTemplate.queryForRowSet(sql)
    if (rs.next()) {
      rs.getString("FILEPATH")
    } else {
      null
    }
  }
}
