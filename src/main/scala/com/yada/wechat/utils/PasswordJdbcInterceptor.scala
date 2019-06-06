package com.yada.wechat.utils

import org.apache.tomcat.jdbc.pool.{ConnectionPool, JdbcInterceptor, PooledConnection}


/**
  * Created by ltl on 2017/12/15.
  *
  * 连接线程池时解密密码
  */
class PasswordJdbcInterceptor extends JdbcInterceptor {

  override def reset(parent: ConnectionPool, con: PooledConnection): Unit = {
  }

  override def poolStarted(pool: ConnectionPool): Unit = {
    val poolProperties = pool.getPoolProperties
    val password = KeyManagerUtil.decrypt(poolProperties.getPassword)
    poolProperties.setPassword(password)
  }
}