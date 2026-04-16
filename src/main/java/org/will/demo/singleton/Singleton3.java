/**
 * @author TianLong Liu
 * @date 2026-04-16 20:13:16
 * @description
 */

package org.will.demo.singleton;

/**
 * 单列模式：懒汉式（加 synchronized，线程安全但低效）
 * 优点：线程安全、懒加载
 * 缺点：整个方法加锁，并发性能极差
 * 结论：不推荐高并发场景。
 * @author TianLong Liu
 * @date 2026-04-16 20:13:16
 */
public class Singleton3 {
    private static Singleton3 instance;
    private Singleton3() {}
    private static synchronized Singleton3 getInstance() {
        if (instance == null) {
            instance = new Singleton3();
        }
        return instance;
    }
}
