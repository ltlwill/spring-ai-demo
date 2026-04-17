/**
 * @author TianLong Liu
 * @date 2026-04-16 20:15:46
 * @description
 */

package org.will.demo.designpattern.singleton;

/**
 * 单列模式：静态内部类（最优之一，推荐）
 * 原理：
 *
 *     外部类加载 → 内部类不加载
 *     调用 getInstance() → 才加载 Holder → 初始化实例
 *
 * 优点
 *
 *     懒加载
 *     线程安全（JVM 类加载机制保证）
 *     无锁、性能极高
 *     简洁优雅
 *
 * 缺点：无法传参
 * @author TianLong Liu
 * @date 2026-04-16 20:15:46
 */
public class Singleton5 {
    private Singleton5() {}
    private static class Inner{
        private static Singleton5 instance = new Singleton5();
    }

    public static Singleton5 getInstance() {
        return Inner.instance;
    }

}
