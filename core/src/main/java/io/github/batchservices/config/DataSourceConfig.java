package io.github.batchservices.config;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.support.DataFieldMaxValueIncrementerFactory;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.SqlServerMaxValueIncrementer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "entityManagerFactory", 
		transactionManagerRef = "transactionManager",
		basePackages = { "io.github.batchservices.repository.global" })
public class DataSourceConfig {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

	@Autowired
	private DataSourceProperties properties;

    @Autowired
    private MBeanServer mBeanServer;

	public Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
		properties.setProperty("hibernate.show_sql", "false");
		properties.setProperty("hibernate.id.new_generator_mappings", "false");
		return properties;
	}

    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    @ConfigurationProperties(prefix = "spring.datasource.tomcat")
    public PoolProperties tomcatSqlServerPoolProperties() {
        return new PoolProperties();
    }

    @Value("${spring.datasource.tomcat.jmx-enabled}")
    boolean isJmxEnabled;

	private DataSource createPool(org.apache.tomcat.jdbc.pool.DataSource dataSource, String serverName, String domainName) throws Exception {

        dataSource.createPool();

        if(isJmxEnabled) {
            Hashtable<String, String> nameProperties = new Hashtable<>();
            nameProperties.put("name", serverName + "-" + domainName + "-DataSource");
            dataSource.preRegister(mBeanServer, new ObjectName("metrics", nameProperties));
        }

        return dataSource;
    }

	/**
	 * Default DataSource
	 */
	@ConfigurationProperties(
	prefix = "spring.datasource"
	)
    private DataSource defaultDataSource() throws Exception {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource(tomcatSqlServerPoolProperties());
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(decryptPassword(properties.getPassword()));
        return createPool(dataSource, properties.getUsername(), properties.getName());
    }

	@Bean(name="repoDataSource")
	public DataSource repoDataSource() throws Exception {
        return defaultDataSource();
	}

    public String decryptPassword(String password) {

        String pwd;
        try {
            pwd = password;
        } catch (SecurityException e) {
            logger.error("Encountered Security Exception" + e.getMessage());
            return password;
        }
        return pwd;
    }
	
	/***
	 *	nHome Transaction Manager	
	 *	Use this for any Spring-Data-JPA DB interactions on "nHome".
	 *
	 */
	@Bean (name = "transactionManager")
	@Primary
	public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory emf){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	/***
	 * Use this entity Manager for "nHome" related JPA ORM activities.
	 *  * Note that whenever Spring sees any class that has below property definition...
	 *  		"@PersistenceContext
    			 private EntityManager globalEntityManager;" 
	 * Then Spring injects an "EntityManager" using "entityManagerFactory" defined in this bean.
	 */
	@PersistenceContext(unitName = "globalEntity")
	@Bean (name="entityManagerFactory")
	public EntityManagerFactory  entityManagerFactory(@Qualifier("repoDataSource")DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.ncr.di.bch.nachafiletransformer.domain");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(hibernateProperties());
		em.afterPropertiesSet();

		return em.getObject();
	}

	/***
	 * Use this JdbcTemplate for Global (nHome) table operations.
	 */
	@Bean
	public JdbcTemplate jdbcTemplate(@Qualifier("repoDataSource")DataSource dataSource)
	{
		return new JdbcTemplate(dataSource);
	}

	/*******************************
	 * Sticky Data Source Configuration for Spring Batch Job Repository - START 
	 *******************************/ 

	@Bean(name="jobRepository")
	public JobRepository getJobRepository(@Qualifier("repoDataSource") DataSource repoDataSource, @Qualifier("transactionManager") PlatformTransactionManager transactionManager) throws Exception {

		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(repoDataSource);
		factory.setTransactionManager(transactionManager);
		factory.setValidateTransactionState(true);

		factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");	// This is to eliminate dead-lock exception.
		//factory.setIncrementerFactory(customIncrementerFactory(null));
		//return factory.getJobRepository();
		try {
			factory.afterPropertiesSet();
			return factory.getObject();
		} catch (Exception e) {
			System.out.println("JobRepository bean could not be initialized");
			throw new BatchConfigurationException(e);
		}
	}

	@Bean
	public BatchConfigurer configurer(@Qualifier("repoDataSource") DataSource repoDataSource){
		return new DefaultBatchConfigurer(repoDataSource);
	}

	// Note Sravan V: Test without this bean and remove if not necessary.
	@Bean
	  public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
	    SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
	    simpleJobLauncher.setJobRepository(jobRepository);
	    return simpleJobLauncher;
	  }

	// Note Sravan V: Test without this bean and remove if not necessary.
	private DataFieldMaxValueIncrementerFactory customIncrementerFactory(@Qualifier("repoDataSource") DataSource repoDataSource) {
	    return new CustomDataFieldMaxValueIncrementerFactory(repoDataSource);
	  }

	  private class CustomDataFieldMaxValueIncrementerFactory extends DefaultDataFieldMaxValueIncrementerFactory {

	    CustomDataFieldMaxValueIncrementerFactory(DataSource dataSource) {
	      super(dataSource);
	    }

	    @Override
	    public DataFieldMaxValueIncrementer getIncrementer(String incrementerType, String incrementerName) {
	      DataFieldMaxValueIncrementer incrementer = super.getIncrementer(incrementerType, incrementerName);
	      if (incrementer instanceof SqlServerMaxValueIncrementer) {
	        ((SqlServerMaxValueIncrementer) incrementer).setCacheSize(20);
	      }
	      return incrementer;
	    }
	  }

	/*******************************
	 * Sticky Data Source Configuration for Spring Batch Job Repository - END 
	 *******************************/

}
