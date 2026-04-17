/**
 * @author TianLong Liu
 * @date 2026-04-16 20:17:23
 * @description
 */

package org.will.demo.designpattern.singleton;

/**
 * 单列模式： 枚举单例（Effective Java 作者推荐，最强）
 * 优点
 *
 *     最简单
 *     JVM 天然保证线程安全
 *     天然防止反射攻击
 *     天然防止序列化破坏
 *
 * 缺点：不是懒加载
 * 评价：最安全、最简洁的单例实现
 * @author TianLong Liu
 * @date 2026-04-16 20:17:23
 */
public enum Singleton6 {
    SINGLETON_6;

    public void doSomething() {
        System.out.println("Doing something...");
    }
}
