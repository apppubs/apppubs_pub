package com.hingecloud.apppubs.pub.tools;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SpringContextHolder implements ApplicationContextAware, InitializingBean {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringContextHolder.context = context;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(context, "SpringContextHolder初始化失败");
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return (T) context.getBean(clazz);
	}

}
