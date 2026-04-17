/**
 * @author TianLong Liu
 * @date 2026-04-16 20:11:37
 * @description
 */

package org.will.demo.designpattern.singleton;

/**
 * 单列模式：懒汉式（线程不安全）
 * 优点：懒加载（用的时候才创建）
 * 缺点：多线程完全不安全，会创建多个实例。结论：生产绝对不能用。
 * @author TianLong Liu
 * @date 2026-04-16 20:11:37
 */
public class Singleton2 {
    private static Singleton2 instance;
    private Singleton2(){}

    public static Singleton2 getInstance(){
        if(instance == null){
            instance = new Singleton2();
        }
        return instance;
    }
}
