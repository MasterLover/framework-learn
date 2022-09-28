package com.lagou.rpc.provider.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)//接口和类
@Retention(RetentionPolicy.RUNTIME)//运行时
public @interface RpcService {

}
