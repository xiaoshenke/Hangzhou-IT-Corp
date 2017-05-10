package wuxian.me.spidersdk.control;

/**
 * Created by wuxian on 10/5/2017.
 * <p>
 * 使用url存入RedisJobQueue的方式,pull的时候加一层解析的方式来实现分布式JobQueue
 * <p>
 * <p>
 * 核心问题：解决分布式spiderSdk如何解析某个pattern的url
 * <p>
 * 方案一：使用Master/Agent方式,Agent将自己收集到的url pattern上报给Master,
 * 然后Master下发给各个Agent
 * 优势：业务无感知
 * 劣势：实现困难
 * <p>
 * 方案二：使用业务方将自己注册到sdk(urlPattern,urlParsing)的方式
 * 优势：实现简单
 * 劣势：业务方需要将自己注册到sdk,业务方能强烈感知到sdk的存在,
 * 且业务方的实现需要规范(比如构造函数必须无参)
 * <p>
 * Todo:
 */
public class RedisJobQueue implements IQueue {
}
