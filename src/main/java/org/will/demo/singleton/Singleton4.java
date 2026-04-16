/**
 * @author TianLong Liu
 * @date 2026-04-16 20:14:41
 * @description
 */

package org.will.demo.singleton;

/**
 * 双重检查锁 DCL（Double-Checked Locking）
 * 关键点：必须加 volatile（禁止指令重排）
 * 优点
 *
 *     线程安全
 *     懒加载
 *     高性能（只锁一次）
 *
 * 缺点
 *
 *     写法复杂
 *     仍可能被反射 / 序列化破坏
 * @author TianLong Liu
 * @date 2026-04-16 20:14:41
 */
public class Singleton4 {
    private static volatile Singleton4 instance;
    private Singleton4() {}

    private static Singleton4 getInstance() {
        if (instance == null) {
            synchronized (Singleton4.class) {
                if (instance == null) {
                    instance = new Singleton4();
                }
            }
        }
        return instance;
    }
}
