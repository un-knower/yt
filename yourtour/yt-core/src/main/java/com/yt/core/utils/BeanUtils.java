package com.yt.core.utils;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import com.alibaba.fastjson.JSON;

public class BeanUtils {
	private static final String SETTER_PREFIX = "set";

	/**
	 * 对象合并
	 * @param source
	 * @param target
	 * @throws Exception
	 */
	public static void merge(Object source, Object target) throws Exception{
		merge(source, target, false);
	}

	/**
	 * 对象合并
	 * @param source
	 * @param target
	 * @param deep
	 * @throws Exception
	 */
	public static void merge(Object source, Object target, boolean deep) throws Exception{
		if(source == null || target == null) return;

		if(! source.getClass().equals(target.getClass())) return;

		//基于内省机制获取bean的属性
		BeanInfo targetBeanInfo = Introspector.getBeanInfo(target.getClass());

		//根据getter来判断有哪些属性
		PropertyDescriptor[] targetProps = targetBeanInfo.getPropertyDescriptors();
		for (PropertyDescriptor targetProp : targetProps) {
			//获取source与target相同name的属性
			PropertyDescriptor sourceProp = getPropertyDescriptor(source.getClass(), targetProp.getName());

			Method targetWriteMethod = targetProp.getWriteMethod();
			if (targetWriteMethod == null) {
				//如果不存在标准的setter，可能存在public xxx(非void) setXXX方法（譬如thrift生成的setter）
				MethodDescriptor methodDesc = getMethodDescriptor(target.getClass(), SETTER_PREFIX
						+ targetProp.getName().substring(0, 1).toUpperCase()
						+ targetProp.getName().substring(1));
				if (methodDesc != null) {
					targetWriteMethod = methodDesc.getMethod();
				}
			}

			//如果target具有setter方法，并且source具有getter方法
			if (targetWriteMethod != null && sourceProp != null && sourceProp.getReadMethod() != null) {
				Object value = getValue(sourceProp.getReadMethod(), source, new Object[0]);
				if (value != null) {
					//判断getter与setter的参数类型是否匹配，否则容易报IllegalArgumentException: argument type mismatch
					if (value.getClass() == targetWriteMethod.getParameterTypes()[0]) {
						setValue(targetWriteMethod, target, new Object[]{value});
					} else if (value.getClass().isPrimitive()
							|| targetWriteMethod.getParameterTypes()[0].isPrimitive()) {
						setValue(targetWriteMethod, target, new Object[]{value});
					}
				}
			}
		}
	}

	private static Object getValue(Method method, Object obj, Object[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
			method.setAccessible(true);
		}

		return method.invoke(obj, args);
	}

	private static void setValue(Method method, Object obj, Object[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
			method.setAccessible(true);
		}

		method.invoke(obj, args);
	}

	private static MethodDescriptor getMethodDescriptor(Class<?> clz, String methodName) throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(clz);

		if (beanInfo != null) {
			MethodDescriptor[] methods = beanInfo.getMethodDescriptors();
			for (MethodDescriptor method : methods) {
				if (method.getName().equals(methodName)) {
					return method;
				}
			}
		}

		return null;
	}

	private static PropertyDescriptor getPropertyDescriptor(Class<?> clz, String propertyName)
	throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(clz);

		if (beanInfo != null) {
			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor prop : props) {
				if (prop.getName().equals(propertyName)) {
					return prop;
				}
			}
		}

		return null;
	}

	/**
	 * 将JSON数据转换成对象
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static Object deserialize(String json, Class<?> clazz) throws Exception {
		return JSON.parseObject(json, clazz);
	}
}
