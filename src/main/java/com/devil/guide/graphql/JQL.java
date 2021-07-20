package com.devil.guide.graphql;

import com.devil.guide.graphql.model.Card;
import com.devil.guide.graphql.model.User;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.*;
import org.limbo.utils.UUIDUtils;

/**
 * @author Devil
 * @date 2021/7/20 10:04 上午
 */
public class JQL {

    GraphQLObjectType cardObjectType = GraphQLObjectType.newObject()
            .name("Card")
            .field(GraphQLFieldDefinition.newFieldDefinition().name("cardNumber").type(Scalars.GraphQLString))
            .field(GraphQLFieldDefinition.newFieldDefinition().name("userId").type(Scalars.GraphQLInt))
            .build();

    /*
            定义GraphQL对象,等同于schema中定义的
            type User {
                id:ID
                age:Int
                name:String
            }
        */
    GraphQLObjectType userObjectType = GraphQLObjectType.newObject()
            .name("User")
            .field(GraphQLFieldDefinition.newFieldDefinition().name("id").type(Scalars.GraphQLID))
            .field(GraphQLFieldDefinition.newFieldDefinition().name("age").type(Scalars.GraphQLInt))
            .field(GraphQLFieldDefinition.newFieldDefinition().name("name").type(Scalars.GraphQLString))
            .field(GraphQLFieldDefinition.newFieldDefinition().name("card").type(cardObjectType))
            .build();


    public static void main(String[] args) {
        JQL jql = new JQL();
        jql.test1();
        jql.test2();
    }

    private void test1() {
        /*
            queryUser : User 指定对象及参数类型
            等同于在GraphQL中定义一个无参方法 queryUser，返回值为User
            queryUser:User
            dataFetcher指定了响应的数据集，这个demo里使用了静态写入的方式
         */
        GraphQLFieldDefinition userFileldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                .name("queryUser")
                .type(userObjectType)
                //静态数据
                .dataFetcher(new StaticDataFetcher(new User(19, 2, "CLC")))
                .build();
        /*
            type UserQuery 定义查询类型

            对应的graphQL为：
                type UserQuery {
                    queryUser:User
                }
         */
        GraphQLObjectType userQueryObjectType = GraphQLObjectType.newObject()
                .name("UserQuery")
                .field(userFileldDefinition)
                .build();
        /*
            Schema 定义查询
            定义了query的root类型
            对应的GraphQL语法为：
               schema {
                    query:UserQuery
               }
         */
        GraphQLSchema qlSchema = GraphQLSchema.newSchema().query(userQueryObjectType).build();

        //构建一个GraphQl对象，执行逻辑都在此处进行
        GraphQL graphQL = GraphQL.newGraphQL(qlSchema).build();

        //模拟客户端传入查询脚本，方法名queryUser，获取响应值为 id name age
        String query = "{queryUser{id name age card{cardNumber userId}}}";
        // 执行业务操作逻辑，获取返回值
        ExecutionResult result = graphQL.execute(query);

        System.out.println(result.toSpecification());
    }

    public void test2() {
        SchemaPrinter schemaPrinter = new SchemaPrinter();

        StringBuilder typeContentBuilder = new StringBuilder("schema{query:UserQuery} \ntype UserQuery{queryUser:User queryUserById(id:ID):User}\n");
        typeContentBuilder.append(schemaPrinter.print(userObjectType)).append(schemaPrinter.print(cardObjectType));

        String typeContent = typeContentBuilder.toString();

        System.out.println(typeContent);

        // 解析文件内容
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(typeContent);

        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("UserQuery", builder ->
                        builder.dataFetcher("queryUserById", environment -> {
                            //解析请求参数，根据业务返回结果
                            long id = Long.parseLong(environment.getArgument("id"));
                            Card card = new Card(UUIDUtils.shortRandomID(), id);
                            return new User(18, id, "user0" + id, card);
                        }).dataFetcher("queryUser", environment -> {
                            Card card = new Card(UUIDUtils.shortRandomID(), 1L);
                            return new User(1, 1, "user0" + 1, card);
                        })
                )
                .build();

        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, wiring);

        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();

        System.out.println(graphQL.execute("{queryUserById(id:15){id,name,age,card{cardNumber,userId}}}").toSpecification());
        System.out.println(graphQL.execute("{queryUser{id name age card{cardNumber userId}}}").toSpecification());
    }
}
