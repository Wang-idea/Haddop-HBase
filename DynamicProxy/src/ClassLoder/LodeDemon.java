package ClassLoder;
/**
 *类加载器
 *      Class类：getClassLoader()
 *              获得该类的类加载器
 */
public class LodeDemon {

    public static void main(String[] args) {
        /**
         * 类加载器的父类加载器就是引导类加载器，则此方法将在这样的实现中返回 null。
         */

        ClassLoader classLoader = String.class.getClassLoader();
        System.out.println("classLoader = " + classLoader);

        ClassLoader classLoader1 = LodeDemon.class.getClassLoader();
        System.out.println("classLoader1 = " + classLoader1);
    }
}
