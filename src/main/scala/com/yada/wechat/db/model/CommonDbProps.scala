package com.yada.wechat.db.model

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
  * 数据库加载配置参数类
  *
  * @author le.wu
  *         2018-04-02
  */
@Component
class CommonDbProps {
  // 端口
  @Value("${file.server.port}")
  var port: String = _
  // 上下文根
  @Value("${file.server.context}")
  var context: String = _
  //授权码超时时间
  @Value("${handleId.time}")
  var handleIdTime: String = _
  //限流开关
  @Value("${interceptor.tps.switchFlag}")
  var switchFlag: String = _
  //限流url
  @Value("${interceptor.tps.limitUrlAndTps}")
  var limitUrlAndTps: String = _
  @Value("${interceptor.tps.defaultTps}")
  var defaultTps: String = _
  @Value("${interceptor.tps.path}")
  var path: String = _
  //图片加载失败返回图的本地存放路径
  @Value("${failedImage.path}")
  var failedImagePath: String = _
  @Value("${httpMethod.filter}")
  var httpMethod: String = _


}

