/**
 * @author TianLong Liu
 * @date 2026-04-16 20:09:38
 * @description
 */

package org.will.demo.designpattern.singleton;

/**
 * 单列模式：饿汉式（静态代码块）
 * 优点
 *
 *     简单、线程安全（类加载时初始化）
 *     无锁，性能极高
 *
 * 缺点
 *
 *     类加载就初始化，可能浪费内存（不管用不用都创建）
 *
 * 适用：实例较小、一定会用到的场景。
 * @author TianLong Liu
 * @date 2026-04-16 20:09:38
 */
public class Singleton1 {
    private static final Singleton1 instance = new Singleton1();
    private Singleton1(){
    }
    public static Singleton1 getInstance(){
        return instance;
    }
}
