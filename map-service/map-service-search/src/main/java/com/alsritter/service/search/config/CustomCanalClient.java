package com.alsritter.service.search.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.alsritter.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 配置 Canal 客户端
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Component
public class CustomCanalClient implements InitializingBean {
    private static final int BATCH_SIZE = 1000;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建链接
        // 这里填写 canal 所配置的服务器 ip,端口号,destination (在 canal.properties 文件里)以及服务器账号密码
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("127.0.0.1", 11111), "example", "", "");

        try {
            //打开连接
            connector.connect();
            //订阅数据库表,全部表
            connector.subscribe(".*..*");
            //回滚到未进行 ack 的地方，下次 fetch 的时候，可以从最后一个没有 ack 的地方开始拿
            connector.rollback();

            while (true) {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(BATCH_SIZE);
                //获取批量ID
                long batchId = message.getId();
                //获取批量的数量
                int size = message.getEntries().size();
                //如果没有数据
                if (batchId == -1 || size == 0) {
                    try {
                        //线程休眠2秒
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    //如果有数据,处理数据
                    printEntry(message.getEntries());
                }
                //进行 batch id 的确认。确认之后，小于等于此 batchId 的 Message 都会被确认。
                connector.ack(batchId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.disconnect();
        }
    }


    /**
     * 打印 canal server 解析 binlog 获得的实体类信息
     */
    private static void printEntry(List<Entry> entries) {
        for (Entry entry : entries) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                //开启/关闭事务的实体类型，跳过
                continue;
            }

            // RowChange 对象，包含了一行数据变化的所有特征
            //比如 isDdl 是否是 ddl 变更操作 sql 具体的 ddl sql beforeColumns afterColumns 变更前后的数据字段等等
            RowChange rowChange;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new BusinessException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }

            //获取操作类型：insert/update/delete 类型
            EventType eventType = rowChange.getEventType();
            //打印 Header 信息
            System.out.printf("================》; binlog[%s:%s] , name[%s,%s] , eventType : %s%n",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType);

            //判断是否是 DDL 语句
            if (rowChange.getIsDdl()) {
                System.out.println("================》;isDdl: true,sql:" + rowChange.getSql());
            }

            //获取 RowChange 对象里的每一行数据，打印出来
            for (RowData rowData : rowChange.getRowDatasList()) {
                //如果是删除语句
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                    //如果是新增语句
                } else if (eventType == EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                    //如果是更新的语句
                } else {
                    //变更前的数据
                    System.out.println("------->; before");
                    printColumn(rowData.getBeforeColumnsList());

                    //变更后的数据
                    System.out.println("------->; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<Column> columns) {
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
