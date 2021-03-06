package cn.xflat.core.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class SpringContextHolder {

	private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

	private static JsonObject config;

	static ApplicationContext applicationContext;

	/** restrict initialization to a single worker */
	private static final ReentrantLock initializationLock = new ReentrantLock();

	private static Vertx vertx;

	/**
	 * Set an instance of vertx that will be added to the resulting
	 * ApplicationContext so that beans within spring can autowire {@link Vertx}
	 * and the {@link org.vertx.java.core.eventbus.EventBus}
	 *
	 * @param vertx
	 *            the vertx instance to add to the application context.
	 */
	public static void setVertx(Vertx vertx) {
		SpringContextHolder.vertx = vertx;
	}

	public static void createApplicationContext(JsonObject config) {
		try {
			initializationLock.lock();
			if (applicationContext == null) {
				SpringContextHolder.config = config;
				logger.debug("Staring to create the ApplicationContext");
				String configType = config.getString("configType");
				if (configType == null) {
					throw new IllegalArgumentException("configType is a mandatory configuration that must be set");
				}
				if (ConfigType.XML.getValue().equals(configType)) {
					createXMLBasedApplicationContext();
				} else if (ConfigType.JAVA_CONFIG.getValue().equals(configType)) {
					createJavaConfigBasedApplicationContext();
				} else {
					throw new IllegalArgumentException("illegal configTye: " + configType + " must be xml or class");
				}

			} else {
				logger.debug("App context already created");
			}
		} finally {
			initializationLock.unlock();
		}
	}

	private static void createXMLBasedApplicationContext() {
		JsonArray jsonArrayOfXmlFiles = config.getJsonArray("configFiles");
		if (jsonArrayOfXmlFiles == null) {
			throw new IllegalArgumentException(
					"xml based context requires configFiles configuration property to be set");
		}
		String[] xmlFiles = new String[jsonArrayOfXmlFiles.size()];
		try {
			// You are really going to make me do this, because JsonArray
			// doesn't have a nice method to convert the type
			for (int i = 0; i < jsonArrayOfXmlFiles.size(); i++) {
				xmlFiles[i] = jsonArrayOfXmlFiles.getString(i);
			}
			if (xmlFiles[0] == null) {
				throw new IllegalArgumentException(
						"xml based context requires configFiles configuration property to be set with an array of Strings");
			}

			logger.debug("Creating an ApplicationContext with xml configuration");
			if (vertx != null) {
				GenericApplicationContext vertxInjecting = new GenericApplicationContext();
				ConfigurableListableBeanFactory beanFactory = vertxInjecting.getBeanFactory();
				beanFactory.registerSingleton("vertx", vertx);
				beanFactory.registerSingleton("eventBus", vertx.eventBus());
				vertxInjecting.refresh();

				applicationContext = new ClassPathXmlApplicationContext(xmlFiles, vertxInjecting);
			} else {
				applicationContext = new ClassPathXmlApplicationContext(xmlFiles);
			}

			logger.info("Application Context has been created");
		} catch (ClassCastException notStringsException) {
			throw new IllegalArgumentException(
					"xml based context requires configFiles configuration property to be set with an array of String type only");
		} catch (Throwable e) {
			logger.info("Error creating Spring Application Context", e);
		}
	}

	private static void createJavaConfigBasedApplicationContext() {
		JsonArray jsonArrayOfClassStrings = config.getJsonArray("configClasses");
		if (jsonArrayOfClassStrings == null) {
			throw new IllegalArgumentException(
					"java config based context requires configClasses configuration property to be set");
		}
		String[] classes = new String[jsonArrayOfClassStrings.size()];
		try {
			// You are really going to make me do this, because JsonArray
			// doesn't have a nice method to convert the type
			for (int i = 0; i < jsonArrayOfClassStrings.size(); i++) {
				classes[i] = jsonArrayOfClassStrings.getString(i);
			}
		} catch (ClassCastException notStringsException) {
			throw new IllegalArgumentException(
					"java config based context requires configFiles configuration property to be set with an array of String type only");
		}
		if (classes.length > 0) {
			List<Class> clazzes = new ArrayList<>();
			for (String stringClass : classes) {
				try {
					Class clazz = Class.forName(stringClass);
					clazzes.add(clazz);
				} catch (ClassNotFoundException cnfe) {
					throw new IllegalArgumentException("Invalid class: " + stringClass
							+ ". This must be the fully qualified class of a Spring @Configuration class");
				}
			}
			logger.debug("Creating an ApplicationContext with Java Config classes configuration");
			try {
				applicationContext = new AnnotationConfigApplicationContext(clazzes.toArray(new Class[] {}));
			} catch (Throwable e) {
				logger.info("Error creating Spring Application Context", e);
			}
			logger.info("Application Context has been created");
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
