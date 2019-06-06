package com.yada.wechat.utils

import java.text.SimpleDateFormat
import java.util.Date

/**
  * 某一时间与当前时间比较
  *
  * @author le.wu
  *         2018/8/23
  */

object CompareTimeUtil {
  /**
    * 比较当前时间与某一时间相关某分钟
    *
    * @param time1  时间，格式（yyyyMMddHHmmss）
    * @param second 相隔时间秒数
    * @return
    */
  def compareTime(time1: String, second: Int): Boolean = {
    val sdf = new SimpleDateFormat("yyyyMMddHHmmss")
    val date1 = sdf.parse(time1)
    val date2 = new Date
    (date2.getTime - date1.getTime) / 1000 > second
  }
}
