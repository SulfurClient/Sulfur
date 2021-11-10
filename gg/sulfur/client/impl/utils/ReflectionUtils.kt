package gg.sulfur.client.impl.utils

import org.reflections.Reflections

/**
 * @author Kansio
 * @created 1:25 AM
 * @project Client
 */

object ReflectUtils {

    @JvmStatic
    fun <T : Any> getReflects(packagePath: String, clazz: Class<T>): List<Class<out T>> {
        return Reflections(packagePath)
            .getSubTypesOf(clazz)
            .filter { clazz.getDeclaredAnnotation(NotUsable::class.java) == null }
    }


    @JvmStatic
    fun <T : Any> getReflectsFlight(packagePath: String, clazz: Class<T>): List<Class<out T>> {
        return Reflections(packagePath)
            .getSubTypesOf(clazz)
            .filter { clazz.getDeclaredAnnotation(NotUsable::class.java) == null }
    }

}

annotation class NotUsable