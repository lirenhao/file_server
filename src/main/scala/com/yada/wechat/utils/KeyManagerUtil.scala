package com.yada.wechat.utils

/**
  * Created by pcs on 2017/11/20.
  * Desc is KeyManagerUtil(不能spring注入的类可以直接使用这个类,要与keyManager同步)
  */
object KeyManagerUtil {

  // val serviceEnter = new YadaServiceEnter()

  /**
    * 解密信息
    *
    * @param data 解密前数据
    * @return 解密后数据
    */
  def decrypt(data: String): String = {
    // serviceEnter.getService.decryptInfo(data)
    data
  }

  /**
    * 加密信息
    *
    * @param data 加密前数据
    * @return 加密后数据
    */
  def encrypt(data: String): String = {
    // serviceEnter.getService.encryptInfo(data)
    data
  }

  /**
    * 获取key明文
    *
    * @param key 密文
    * @return 获取key明文
    */
  def getPlainKey(key: String): String = {
    // serviceEnter.getService.getPlainKey(key)
    ""
  }
}