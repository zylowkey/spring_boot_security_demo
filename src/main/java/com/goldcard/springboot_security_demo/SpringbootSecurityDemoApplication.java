package com.goldcard.springboot_security_demo;

import com.goldcard.springboot_security_demo.service.impl.UserDetailsServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@SpringBootApplication
@MapperScan(basePackages = "com.goldcard.springboot_security_demo.mapper")
@EnableCaching
// 传统Spring下需要配置注解 EnableWebSecurity 驱动SpringSecurity  spring boot自动驱动
//@EnableWebSecurity
public class SpringbootSecurityDemoApplication extends WebSecurityConfigurerAdapter {
    //Spring boot的自动装配机制会读取application.properties文件里的配置生成有关Redis的操作对象：
    //RedisConnectionFactory,RedisTemplate,StringRedisTemplete等常用对象
    //RedisTemplate默认使用JdkSerializationRedisSerializer进行序列化键值
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        initRedisTemplete();
    }

    // Redis连接工厂
    @Autowired
    private RedisConnectionFactory connectionFactory;
    // Redis消息监听器
    @Autowired
    private MessageListener redisMsgListener = null;

    // 任务池
    private ThreadPoolTaskScheduler taskScheduler = null;


    public static void main(String[] args) {
        SpringApplication.run(SpringbootSecurityDemoApplication.class, args);
    }

    /**
     * 创建任务池，运行线程等待处理Redis的消息
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler initTaskScheduler() {
        if (taskScheduler != null) {
            return taskScheduler;
        }
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        return taskScheduler;
    }

    /**
     * 定义Redis的监听容器
     *
     * @return 监听容器
     */
    @Bean
    public RedisMessageListenerContainer initRedisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // Redis连接工厂
        container.setConnectionFactory(connectionFactory);
        // 设置运行任务池
        container.setTaskExecutor(initTaskScheduler());
        // 定义监听渠道，名称为topic1
        Topic topic = new ChannelTopic("topic1");
        // 使用监听器监听Redis的消息
        container.addMessageListener(redisMsgListener, topic);
        return container;
    }

    //修改RedisTemplate的序列化器
    private void initRedisTemplete() {
        RedisSerializer redisSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
    }

    @Autowired
    private DataSource dataSource;

    @Value("${system.user.password.secret}")
    private String secret = null;

    // 使用用户名称查询密码
    String pwdQuery = " select user_name, pwd, available" + " from t_user where user_name = ?";
    // 使用用户名称查询角色信息
    String roleQuery = " select u.user_name, r.role_name " + " from t_user u, t_user_role ur, t_role r "
            + "where u.id = ur.user_id and r.id = ur.role_id" + " and u.user_name = ?";

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     *覆盖WebSecurityConfigurerAdapter用户详情方法
     * @param auth 用户签名管理器构造器
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //密码编码器
////        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        //采用密钥加密
//        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder(secret);
//        auth.jdbcAuthentication()
//                //密码编码器
//                .passwordEncoder(passwordEncoder)
//                //数据源
//                .dataSource(dataSource)
//                //查询用户，自动判断密码是否一致
//                .usersByUsernameQuery(pwdQuery)
//                //赋予权限
//                .authoritiesByUsernameQuery(roleQuery);
//        super.configure(auth);
//    }

    /**
     *覆盖WebSecurityConfigurerAdapter用户详情方法
     * @param auth 用户签名管理器构造器
     *  自定义认证服务，配置信息优先从缓存读取，读取不到再从数据库获取
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //采用密钥加密
        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder(secret);
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    /**
     * 限制认证用户的访问权限
     * @param http
     * @throws Exception
     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //限定签名后的权限
//        http.
//                /**
//                authorizeRequests：设置需要签名的请求，可以将不同的请求权限赋予不同的角色
//                antMatchers：配置请求的路径
//                hasAnyRole：指定角色 "ROLE_ADMIN" "ROLE_USER"，指定了这些路径只能这些角色访问
//                /admin/** 统配指定，这是分配了ROLE_ADMIN角色可以访问
//                 hasAnyRole方法会默认加上前缀ROLE_  将访问权限赋予多个角色
//                 hasRole 方法会默认加上前缀ROLE_  将访问权限赋予一个角色
//                 hasAuthority不会加上前缀
//                 hasAnyRole/hasAuthority 都表示对应的请求路径只有用户分配了对应的角色才能访问
//                 anyRequest 表示任意的没有限定的请求
//                 permitAll表示没有配置过权限限定的路径允许全部访问
//                 anonymous 允许匿名访问没有配置过的请求
//                 formLogin 启用Spring Security默认的登录页面
//                 httpBasic 启用HTTP的Basic请求输入用户名和密码
//                 */
//
//                /**
//                 * 对这里的配置，会采取先配置优先的原则
//                 * 第二段配置允许匿名访问，没有给出地址
//                 * 但是第一段加入了访问限制，基于先配置优先原则，Spring Security还是会采用第一段的访问限制
//                 */
//                /*#######第一段########*/
//                authorizeRequests()
//                // 限定"/user/welcome"请求赋予角色ROLE_USER或者ROLE_ADMIN
//                .antMatchers("/user/welcome", "/user/details").hasAnyRole("USER", "ADMIN")
//                // 限定"/admin/"下所有请求权限赋予角色ROLE_ADMIN
//                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//                // 其他路径允许签名后访问
//                .anyRequest().permitAll()
//                /*#######第二段########*/
//                /** and代表连接词 **/
//                // 对于没有配置权限的其他请求允许匿名访问
//                .and().anonymous()
//                /*第三段*/
//                // 使用Spring Security默认的登录页面
//                .and().formLogin()
//                // 启动HTTP基础验证
//                .and().httpBasic();
//
//                //采用正则表达式
////                http.authorizeRequests()
////                        .regexMatchers("/user/welcome", "/user/details").hasAnyRole("USER", "ADMIN")
////                        .regexMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
////                        .and().formLogin()
////                        .and().httpBasic();
//    }


    /**
     * 限制认证用户的访问权限 使用Spring 表达式
     * @param http
     * @throws Exception
     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                //使用Spring 表达式限定只有角色ROLE_USER或者ROLE_ADMIN
//                .antMatchers("/user/**").access("hasRole('USER') or hasRole('ADMIN')")
//                //设置访问权限给角色ROLE_ADMIN,要求是完整登录(非记住我登录)
//                .antMatchers("/admin/welcome").access("hasAuthority('ROLE_ADMIN') && isFullyAuthenticated()")
//                //限定 "/admin/welcome2" 访问权限给角色ROLE_ADMIN，允许不完整登录
//                .antMatchers("/admin/welcome2").access("hasAuthority('ROLE_ADMIN')")
//                //使用记住我功能
//                .and().rememberMe()
//                //使用Spring Security默认的登录页面
//                .and().formLogin()
//                //启动HTTP基础验证
//                .and().httpBasic();
//
//        //强制使用 https
//        http
//                //使用安全渠道，限定为https请求  requiresSecure 表示使用Https请求
//                .requiresChannel().antMatchers("/admin/**").requiresSecure()
//                //不使用https请求  requiresInsecure表示取消安全请求的机制，这样就可以使用普通的Http请求
//                .and().requiresChannel().antMatchers("/user/**").requiresInsecure()
//                //限定允许的访问角色
//                .and().authorizeRequests().antMatchers("/admin/**").hasAnyRole("ADMIN")
//                .antMatchers("/user/**").hasAnyRole("USER","ADMIN");
//
//        //跨站点请求伪造(Cross-Site Request Forgery,CSRF)
//        //关闭CSRF
//        http.csrf().disable();
//    }

    /**
     * 限制认证用户的访问权限  自定义
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 访问 /admin 下的请求需要管理员权限
                .authorizeRequests().antMatchers("/admin/**").access("hasRole('ADMIN')")
                // 启用remember me 功能，有效时间为一天(86400s),在浏览器中将使用Cookie以键  remember-me-key 进行保存，在保存前会以 MD5加密
                .and().rememberMe().tokenValiditySeconds(86400).key("remember-me-key")
                .and().httpBasic()
                //通过签名后，可以访问任何请求
                .and().authorizeRequests().antMatchers("/**").permitAll()
                //设置自定义的登录界面和默认的跳转界面  /ogin/plage 路径映射通过WebConfig配置映射
                .and().formLogin().loginPage("/ogin/plage")
                .defaultSuccessUrl("/admin/welcome1")
                //自定义登出界面和默认跳转路径
                .and().logout().logoutUrl("/logout/pge")
                .logoutSuccessUrl("/welcome");
    }
}
