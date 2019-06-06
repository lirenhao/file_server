package com.yada.wechat.interceptor

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.google.common.util.concurrent.RateLimiter
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

/**
  *
  * @author le.wu
  *         2018/9/14
  */
class CurrentLimiterInterceptor(httpMethod: String, limitUrlAndTps: String, switchFlag: String, default: String, context: String) extends HandlerInterceptorAdapter with LazyLogging {
  //不同的URL初始化不同的累加器
  private val limiterMap: Map[String, RateLimiter] = initRateLimiter(limitUrlAndTps)
  //初始化url和url对应的tps值
  private val urlAndTpsMap: Map[String, String] = getUrlAndTps(limitUrlAndTps)
  //初始化要过滤的http请求方法
  private val httpMthod: Array[String] = initHttpMethod(httpMethod)


  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean = {
    val method = request.getMethod
    httpMthod.foreach {
      m =>
        if (method.equals(m)) {
          logger.info("非法请求方式，请求方式[{}]", method)
          return false
        }
    }
    val url = request.getRequestURI
    //只有限流开关为开启才会做限流控制 1-开
    if ("1" == switchFlag && !url.equals(context + "/limit")) { //是限制的URL
      if (urlAndTpsMap == null) {
        val limiter = limiterMap.getOrElse("default", null)
        return this.limiterProcess(request, response, handler, limiter)
      } else {
        urlAndTpsMap.keySet.foreach(
          key =>
            if (url.contains(key)) {
              if (limiterMap.getOrElse(key, null) == null) {
                val limiter = limiterMap.getOrElse("default", null)
                return this.limiterProcess(request, response, handler, limiter)
              } else {
                val limiter = limiterMap.getOrElse(key, null)
                return this.limiterProcess(request, response, handler, limiter)
              }
            }
        )
      }
      val limiter = limiterMap.getOrElse("default", null)
      this.limiterProcess(request, response, handler, limiter)
    }
    super.preHandle(request, response, handler)
  }

  /**
    * 限流器
    *
    * @param request  request
    * @param response response
    * @param handler  handler
    * @return 拦截结果
    * @throws Exception 异常
    */
  private def limiterProcess(request: HttpServletRequest, response: HttpServletResponse, handler: Any, limiter: RateLimiter): Boolean = {
    if (!limiter.tryAcquire) {
      logger.info("请求地址[{}]TPS已达到上限", request.getRequestURI)
      //达到限流后,向前台返回错误码
      val forwardUrl: String = "/limit"
      request.getRequestDispatcher(forwardUrl).forward(request, response)
      false
    } else {
      limiter.acquire
      super.preHandle(request, response, handler)
    }
  }

  override def postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView): Unit = {
    super.postHandle(request, response, handler, modelAndView)
  }

  override def afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception): Unit = {
    super.afterCompletion(request, response, handler, ex)
  }

  def getUrlAndTps(limitUrlAndTps: String): Map[String, String] = {
    var map: Map[String, String] = Map.empty
    if (limitUrlAndTps != null || "".equals(limitUrlAndTps)) {
      limitUrlAndTps.split("\\|").foreach(
        urlAndTps =>
          map += (
            urlAndTps.split("-")(0) -> urlAndTps.split("-")(1)
            )
      )
    }
    map
  }

  def initHttpMethod(httpMethod: String): Array[String] = {
    httpMethod.split("\\|")
  }

  def initRateLimiter(limitUrlAndTps: String): Map[String, RateLimiter] = {
    var map: Map[String, RateLimiter] = Map(
      "default" -> RateLimiter.create(default.toInt)
    )
    if (limitUrlAndTps != null || "".equals(limitUrlAndTps)) {
      limitUrlAndTps.split("\\|").foreach(
        urlAndTps =>
          map += (urlAndTps.split("-")(0) -> RateLimiter.create(urlAndTps.split("-")(1).toInt))
      )
    }
    map
  }
}
