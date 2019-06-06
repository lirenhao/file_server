package com.yada.wechat

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching

/**
  * 程序入口
  *
  * @author le.wu
  *         2018/8/9
  */
object Application extends App {

  SpringApplication.run(classOf[Application])
}

@SpringBootApplication
@EnableCaching
class Application() {}