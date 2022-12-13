//package pl.lodz.p.it.ssbd2022.ssbd01.config;
//
//import javax.annotation.sql.DataSourceDefinition;
//import javax.annotation.sql.DataSourceDefinitions;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
//import javax.ejb.Stateful;
//import javax.ejb.Stateless;
//import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Scope;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.sql.Connection;
//
//// Ta pula połączeń jest na potrzeby tworzenia struktur przy wdrażaniu aplikacji
//@DataSourceDefinitions({
//        @DataSourceDefinition(
//                name = "java:/app/jdbc/ssbd01admin",
//                className = "com.mysql.cj.jdbc.MysqlDataSource",
//                user = "ssbd01admin",
//                password = "admin",
////                url = "jdbc:mysql://localhost:3306/ssbd01",
//                url = "jdbc:mysql://mysql:3306/ssbd01",
//                transactional = true,
//                isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
//                initialPoolSize = 1,
//                minPoolSize = 0,
//                maxPoolSize = 1,
//                maxIdleTime = 10
//        ),
//
//// Ta pula połączeń jest na potrzeby implementacji uwierzytelniania w aplikacji
//        @DataSourceDefinition(
//                name = "java:/app/jdbc/ssbd01auth",
//                className = "com.mysql.cj.jdbc.Driver",
//                user = "ssbd01auth",
////                url = "jdbc:mysql://localhost:3306/ssbd01",
//                url = "jdbc:mysql://mysql:3306/ssbd01",
//                transactional = true,
//                isolationLevel = Connection.TRANSACTION_READ_COMMITTED
//        ),
//
//// Ta pula połączeń jest na potrzeby operacji realizowanych przez moduł obsługi konta w aplikacji
//        @DataSourceDefinition(
//                name = "java:/app/jdbc/ssbd01mok",
//                className = "com.mysql.cj.jdbc.MysqlConnectionPoolDataSource",
//                user = "ssbd01mok",
//                password = "mok",
////                url = "jdbc:mysql://localhost:3306/ssbd01",
//                url = "jdbc:mysql://mysql:3306/ssbd01",
//                transactional = true,
//                isolationLevel = Connection.TRANSACTION_READ_COMMITTED
//        ),
//
//// Ta pula połączeń jest na potrzeby operacji realizowanych przez moduł ogłoszeń w aplikacji
//        @DataSourceDefinition(
//                name = "java:/app/jdbc/ssbd01mo",
//                className = "com.mysql.cj.jdbc.MysqlDataSource",
//                user = "ssbd01mo",
//                password = "mo",
////                url = "jdbc:mysql://localhost:3306/ssbd01",
//                url = "jdbc:mysql://mysql:3306/ssbd01",
//                transactional = true,
//                isolationLevel = Connection.TRANSACTION_READ_COMMITTED
//        ),
//
//// Ta pula połączeń jest na potrzeby operacji realizowanych przez moduł zgłaszającego w aplikacji
//        @DataSourceDefinition(
//                name = "java:/app/jdbc/ssbd01mz",
//                className = "com.mysql.cj.jdbc.MysqlDataSource",
//                user = "ssbd01mz",
//                password = "mz",
////                url = "jdbc:mysql://localhost:3306/ssbd01",
//                url = "jdbc:mysql://mysql:3306/ssbd01",
//                transactional = true,
//                isolationLevel = Connection.TRANSACTION_READ_COMMITTED
//        ),
//
//// Ta pula połączeń jest na potrzeby operacji realizowanych przez moduł wynajmującego w aplikacji
//        @DataSourceDefinition(
//                name = "java:/app/jdbc/ssbd01mw",
//                className = "com.mysql.cj.jdbc.MysqlDataSource",
//                user = "ssbd01mw",
//                password = "mw",
////                url = "jdbc:mysql://localhost:3306/ssbd01",
//                url = "jdbc:mysql://mysql:3306/ssbd01",
//                transactional = true,
//                isolationLevel = Connection.TRANSACTION_READ_COMMITTED
//        )
//})
//@Stateless
//public class JDBCConfiguration {
//    @PersistenceContext(unitName = "ssbd01adminPU")
//    private EntityManager entityManager;
//}
