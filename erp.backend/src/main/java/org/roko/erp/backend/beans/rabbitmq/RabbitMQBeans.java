package org.roko.erp.backend.beans.rabbitmq;

import org.roko.erp.backend.services.AsyncSalesCreditMemoPostService;
import org.roko.erp.backend.services.AsyncSalesOrderPostService;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQBeans {

    @Value("${app.rabbitmq.hostname}")
    private String hostname;

    @Value("${app.rabbitmq.port}")
    private String port;

    @Value("${app.rabbitmq.virtualhost}")
    private String virtualhost;

    @Value("${app.rabbitmq.username}")
    private String username;

    @Value("${app.rabbitmq.password}")
    private String password;

    @Value("${app.rabbitmq.queue.post.sales.order}")
    private String postSalesOrderQueueName;

    @Value("${app.rabbitmq.queue.post.sales.creditmemo}")
    private String postSalesCreditMemoQueueName;

    @Value("${app.rabbitmq.queue.post.purchase.order}")
    private String postPurchaseOrderQueueName;

    @Value("${app.rabbitmq.queue.post.purchase.creditmemo}")
    private String postPurchaseCreditMemoQueueName;

    @Value("${app.rabbitmq.queue.post.generaljournalbatch}")
    private String postGeneralJournalBatchQueueName;

    @Autowired
    private AsyncSalesOrderPostService salesOrderPostService;

    @Autowired
    private AsyncSalesCreditMemoPostService salesCreditMemoPostService;

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(hostname);
        connectionFactory.setPort(Integer.valueOf(port));
        connectionFactory.setVirtualHost(virtualhost);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public SimpleMessageListenerContainer salesOrderPostListener(
            ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(postSalesOrderQueueName);
        container.setMessageListener(
                new MessageListenerAdapter(new SalesOrderPostReceiver(salesOrderPostService), "receiveMessage"));
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer salesCreditMemoPostListener(
            ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(postSalesCreditMemoQueueName);
        container.setMessageListener(new MessageListenerAdapter(new SalesCreditMemoPostReceiver(salesCreditMemoPostService), "receiveMessage"));
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer purchaseOrderPostListener(
            ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(postPurchaseOrderQueueName);
        container.setMessageListener(new MessageListenerAdapter(new PurchaseOrderPostReceiver(), "receiveMessage"));
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer purchaseCreditMemoPostListener(
            ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(postPurchaseCreditMemoQueueName);
        container
                .setMessageListener(new MessageListenerAdapter(new PurchaseCreditMemoPostReceiver(), "receiveMessage"));
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer generalJournalBatchPostListener(
            ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(postGeneralJournalBatchQueueName);
        container.setMessageListener(
                new MessageListenerAdapter(new GeneralJournalBatchPostReceiver(), "receiveMessage"));
        return container;
    }

}
