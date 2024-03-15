package com.chuan.jaegertest.jsdk;

/**
 * 常量采样器 {@link io.jaegertracing.internal.samplers.ConstSampler}
 * 0:不采样 非0:全部采样
 * <p>
 * 概率采样器 {@link io.jaegertracing.internal.samplers.ProbabilisticSampler}
 * 设置0~1的概率作为采样率百分比
 * 默认千分之一 概率准确率取决于标识符的离散程度
 * <p>
 * 速率采样器 {@link io.jaegertracing.internal.samplers.RateLimitingSampler}
 * 限制每秒采样的次数,使用漏桶算法,限流
 * <p>
 * 默认采样器 {@link io.jaegertracing.internal.samplers.RemoteControlledSampler}
 * 更像是一个代理, 每隔60s查询服务的采样率配置
 * 配置的采样策略可以细粒度到每个服务,每个操作,且有默认采样策略,可配置的实际采样实现就是上述描述的三种
 * 改配置文件由jaeger-collector读取 故最终是在收集组件处决定是否采样,
 * 但需要代理组件提供接口并转发至收集组件 ,收集组件不直接提供查询接口 代理组件端口:5778
 * `jaeger-collector --sampling.strategies-file=strategy.json`
 *
 * @author chuanjiang
 */
public class SamplerDemo {

}
