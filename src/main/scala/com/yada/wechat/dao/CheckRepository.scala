package com.yada.wechat.dao

import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate

import scala.collection.mutable

/**
  * 权限认证Dao
  *
  * @author le.wu
  *         2018/8/9
  */
@Repository
class CheckRepository(jdbcTemplate: JdbcTemplate) {
  /**
    * 根据操作ID查询文件预处理表
    *
    * @param handleId    操作ID
    * @param requestType 请求类型
    * @return
    */
  def selectByHandleId(handleId: String, requestType: String): Map[String, String] = {
    val sql1 =s"""select * from t_b_file_proc t where t.HANDLEID='$handleId' and t.status='$requestType'""".stripMargin
    val sql2 =s"""delete t_b_file_proc t where t.HANDLEID='$handleId' and t.status='$requestType'""".stripMargin
    val rs = jdbcTemplate.queryForRowSet(sql1)
    if (rs.next()) {
      val map = Map(
        "fileId" -> rs.getString("FILEID"),
        "path" -> rs.getString("PATH"),
        "fileName" -> rs.getString("FILENAME"),
        "date" -> rs.getString("IN_DATE"),
        "time" -> rs.getString("IN_TIME")
      )
      //如果查询到则删除此条数据
      jdbcTemplate.execute(sql2)
      map
    } else {
      null
    }
  }
}
