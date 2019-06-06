package com.yada.wechat.dao

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/**
  * 文件删除Dao
  *
  * @author le.wu
  *         2018/8/29
  */
@Repository
class DeleteRepository(jdbcTemplate: JdbcTemplate) {

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

  /**
    * 删除文件信息表中数据
    *
    * @param fileId 文件ID
    * @return
    */
  def deleteFileInfo(fileId: String): Int = {
    val sql =s"""delete from t_b_file_info t where t.FILEID='$fileId'""".stripMargin
    jdbcTemplate.update(sql)
  }
}
