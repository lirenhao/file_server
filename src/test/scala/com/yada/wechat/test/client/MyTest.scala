package com.yada.wechat.test.client

import java.text.SimpleDateFormat
import java.util.Date

/**
  *
  * @author le.wu
  *         2018/8/9
  */
object MyTest extends App {

  val sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
  val date: String = sdf.format(new Date())
  println(date)
}
