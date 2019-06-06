package com.yada.wechat.controller

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PostMapping, RequestMapping, ResponseBody}

/**
  * 限流controller
  *
  * @author le.wu
  *         2018/9/18
  */
@Controller
@EnableAutoConfiguration
@RequestMapping(Array("/limit"))
class LimitController extends LazyLogging {

  @PostMapping
  @ResponseBody
  def home(): String = {
    "LIMIT"
  }
}
