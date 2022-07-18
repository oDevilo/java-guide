/*
 *
 *  * Copyright 2020-2024 Limbo Team (https://github.com/limbo-world).
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * 	http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.devil.guide.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.Scanner;

/**
 * @author Devil
 * @since 2022/7/15
 */
public class ZKTest {

    String zkPath = "localhost:2181";

    String basePath = "/java/test";

    @Test
    public void testClient() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkPath, new ExponentialBackoffRetry(60, 300, 18000));

        client.start();
        client.blockUntilConnected();


        CuratorCache curatorCache = CuratorCache.build(client, basePath);
//        CuratorCache curatorCache = CuratorCache.build(client, basePath, CuratorCache.Options.SINGLE_NODE_CACHE); // 加了这个只有 CuratorFramework 创建才行，通过本地zkCli就没通知
        // 给CuratorCache实例添加监听器
        curatorCache.listenable().addListener(CuratorCacheListener.builder()
                        // 初始化完成时调用
                        .forInitialized(() -> System.out.println("[forInitialized] : Cache initialized"))
                        // 添加或更改缓存中的数据时调用
//                        .forCreatesAndChanges(
//                                (oldNode, node) -> System.out.printf("[forCreatesAndChanges] : Node changed: Old: [%s] New: [%s]\n",
//                                        oldNode, node)
//                        )
                // 添加缓存中的数据时调用
//                .forCreates(childData -> System.out.printf("[forCreates] : Node created: [%s]\n", childData))
//                // 更改缓存中的数据时调用
//                .forChanges(
//                        (oldNode, node) -> System.out.printf("[forChanges] : Node changed: Old: [%s] New: [%s]\n",
//                                oldNode, node)
//                )

//                        .forNodeCache(() -> System.out.println("forNodeCache"))
                        // 路径下子节点变动通知
//                        .forPathChildrenCache(basePath, client, new PathChildrenCacheListener() {
//                            @Override
//                            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
//                                System.out.printf("[PathChildrenCacheListener] type: [%s]", event.getType());
//                                System.out.println();
//                            }
//                        })
                        .forTreeCache(client, new TreeCacheListener() {
                            @Override
                            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                                System.out.printf("[TreeCacheListener] type: [%s] ", event.getType());
                                System.out.println(event);
                            }
                        })
                // 删除缓存中的数据时调用
//                .forDeletes(childData -> System.out.printf("[forDeletes] : Node deleted: data: [%s]\n", childData))
//                // 添加、更改或删除缓存中的数据时调用
//                .forAll((type, oldData, data) -> System.out.printf("[forAll] : type: [%s] [%s] [%s]\n", type, oldData, data))
                        .build()
        );
        curatorCache.start();

//        client.getChildren().usingWatcher(new Watcher() {
//            @Override
//            public void process(WatchedEvent event) {
//                System.out.println(event.getPath() + "  " + event.getType());
//            }
//        }).forPath(basePath);

        //        client.create().forPath("/ttt");
//        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(basePath + "/b");

        new Scanner(System.in).next();
    }


}
