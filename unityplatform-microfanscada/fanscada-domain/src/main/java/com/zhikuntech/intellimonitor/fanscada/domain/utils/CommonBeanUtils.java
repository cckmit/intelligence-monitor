package com.zhikuntech.intellimonitor.fanscada.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CommonBeanUtils {

	/**
	 * dto 转换为Do 工具类
	 */
	public static <T> T dtoTransfer(Object sourceEntity, Class<T> targetClass) {
		// 判断dto是否为空!
		if (sourceEntity == null) {
			return null;
		}
		// 判断DoClass 是否为空
		if (targetClass == null) {
			return null;
		}
		try {
			T newInstance = targetClass.newInstance();
			BeanUtils.copyProperties(sourceEntity, newInstance);
			// Dto转换Do
			return newInstance;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * do 转换为Dto 工具类
	 */
	public static <T> List<T> dtoListTransfer(List<?> sourceEntityList, Class<T> targetClass) {
		// 判断dto是否为空!
		if (sourceEntityList == null) {
			return null;
		}
		// 判断DoClass 是否为空
		if (targetClass == null) {
			return null;
		}
		try {
			List<T> objects = new ArrayList<>();
			for (Object object : sourceEntityList) {
				T newInstance = targetClass.newInstance();
				BeanUtils.copyProperties(object, newInstance);
				objects.add(newInstance);
			}
			// Dto转换Do
			return objects;
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> T convertValue(Object bean, Class<T> clazz){
		try{
			ObjectMapper mapper = new ObjectMapper();
			return mapper.convertValue(bean, clazz);
		}catch(Exception e){
			return null;
		}
	}






}
