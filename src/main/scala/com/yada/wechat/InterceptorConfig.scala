package com.yada.wechat

import com.yada.wechat.db.model.CommonDbProps
import com.yada.wechat.interceptor.CurrentLimiterInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.{InterceptorRegistry, WebMvcConfigurerAdapter}

/**
  *
  * @author le.wu
  *         2018/9/18
  */
@Configuration
class InterceptorConfig(commonDbProps: CommonDbProps) extends WebMvcConfigurerAdapter {
  override def addInterceptors(registry: InterceptorRegistry): Unit = {
    registry.addInterceptor(new CurrentLimiterInterceptor(commonDbProps.httpMethod, commonDbProps.limitUrlAndTps, commonDbProps.switchFlag, commonDbProps.defaultTps, commonDbProps.context)).addPathPatterns(commonDbProps.path)
    super.addInterceptors(registry)
  }
}
