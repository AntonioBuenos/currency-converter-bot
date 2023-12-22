package by.smirnov.currencyconverterbot.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter

import javax.sql.DataSource

@Configuration
class PersistenceProvidersConfiguration {

    @Autowired
    @Bean(name = "entityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        def em = new LocalContainerEntityManagerFactoryBean()
        em.setDataSource(dataSource)
        em.setPackagesToScan("by.smirnov.currencyconverterbot.entity")

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter()
        em.setJpaVendorAdapter(vendorAdapter)
        em.setJpaProperties(getAdditionalProperties())

        return em
    }

    private static Properties getAdditionalProperties() {
        def properties = new Properties()

        properties."hibernate.show_sql" = "true"
        properties."hibernate.default_schema" = "currency_converter"
        properties."current_session_context_class" = "org.springframework.orm.hibernate5.SpringSessionContext"
        return properties
    }
}

