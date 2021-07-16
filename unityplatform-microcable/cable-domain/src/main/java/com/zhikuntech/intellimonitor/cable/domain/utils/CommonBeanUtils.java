package com.zhikuntech.intellimonitor.cable.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CommonBeanUtils<Dto, Do> {

	/**
	 * dot 转换为Do 工具类
	 */
	public static <Do> Do dtoToDo(Object dtoEntity, Class<Do> doClass) {
		// 判断dto是否为空!
		if (dtoEntity == null) {
			return null;
		}
		// 判断DoClass 是否为空
		if (doClass == null) {
			return null;
		}
		try {
			Do newInstance = doClass.newInstance();
			BeanUtils.copyProperties(dtoEntity, newInstance);
			// Dto转换Do
			return newInstance;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * do 转换为Dto 工具类
	 *
	 */
	public static <Dto> Dto doToDto(Object doEntity, Class<Dto> dtoClass) {
		// 判断dto是否为空!
		if (doEntity == null) {
			return null;
		}
		// 判断DoClass 是否为空
		if (dtoClass == null) {
			return null;
		}
		try {
			Dto newInstance = dtoClass.newInstance();
			BeanUtils.copyProperties(doEntity, newInstance);
			// Dto转换Do
			return newInstance;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * do 转换为Dto 工具类
	 */
	public static <Dto> List<Dto> doToDtoList(List<?> doEntity, Class<Dto> dtoClass) {
		// 判断dto是否为空!
		if (doEntity == null) {
			return null;
		}
		// 判断DoClass 是否为空
		if (dtoClass == null) {
			return null;
		}
		try {
			List<Dto> objects = new ArrayList<>();
			for (Object object : doEntity) {
				Dto newInstance = dtoClass.newInstance();
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
