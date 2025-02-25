## Ai-search-platform

Search Engine

- EL relationship support for all indices
- mapping indices
- candidate profile and job profile indices
- common search project for any indices configured by esmapping property

[Documentation](https://wiki.mokahr.com/pages/viewpage.action?pageId=21959518)


### Installation

#### Requirements

1. Elasitcsearch v6.8.3 with [analysis-moka](https://gitlab.mokahr.com/TryMoka/analysis-moka/tags) plugin
2. jdk1.8 +
3. maven 3 +
4. xgboost support

for mac m1 user, please explicit use oracle jdk ad your JAVA_HOME, and don't use zulu jdk, 'cause xgboost not support
zulu java's compile progress or path.

see also: https://github.com/alibaba/nacos/issues/4336#issuecomment-821246302


#### docker

build docker image

```
mvn install && docker build -t thoth .
```

run docker image as container (default env)

```
docker run -e "SPRING_PROFILES_ACTIVE=default" -p 8080:8080 thoth
```

run docker image as container (prod-tc env)
```
docker run -e "SPRING_PROFILES_ACTIVE=prod-tc" -p 8080:8080 thoth
```



### Deploy by deploy script


```shell
git clone git@gitlab.mokahr.com:TryMoka/thoth.git
python deploy.py
```

#### Deployment

1. update `ThothConfig.yml`，customize es servers and ports.

```yaml
ip: 127.0.0.1
port: 8080
threadnum: 3000
timeout: 20000

esconf:
  iprifix: thoth
  timeout: 19000
  servers:
    - ip: 39.106.174.90 # your elasticsearch ip.
      port: 9200
```

2. download dependencies and compile project by maven.

install jblas' missing libraries on Linux/Windows if needed.

see also:
https://github.com/jblas-project/jblas/wiki/Missing-Libraries
https://github.com/jblas-project/jblas/issues/14

```
sudo apt-get install libgfortran3
```

3. start by py script.

```
python deploy.py
```

which did following things:

- kill process running on port:8080
- **recognize enviroment and use corresponding config file by machine name**
- mvn clean & mvn install
- run jar
- help cheking whether service is alive after few seconds


4. test accessibility (port is 8080 by default).

```
curl localhost:8080
```

### To Start thoth on a rarely new local Elasticsearch /  new elasticsearch cluster

as our chunk grouping plan, **an initial chunk should be created firstly**.
(more information:https://wiki.mokahr.com/pages/viewpage.action?pageId=35146553 )

```
curl --location --request POST 'http://localhost:8080/thoth/base/sync-mapping-chunks/candidatev2' \
--header 'Content-Type: application/json' \
--data-raw '[
    {
        "id": "chunk_0",
        "count": 0,
        "org_ids": [
            "just-for-test"
        ]
    }
]'
```


### Production QUO

```
                               --- thoth-prod01(172.17.46.135)
                              /
thoth slb(172.17.141.202) ----
                              \
                               --- thoth-prod02(172.17.46.136)
```

**start single thoth service with production env**

specify production config.

```
nohup ./bin/thoth config/ThothConfigProduction.yml &
```


#### Basic OQL Grammar:

```sql
do FUNCTIONS where CONDITION limit start,size

eg.
do @select({cid}) where name^3.0:"刘翔" and phone="15610872346" limit 0,20

```

#### Project Structure

```bash

└── main
    ├── assemblies
    │   └── deploy.xml
    ├── java
    │   └── com
    │       └── moka
    │           └── search
    │               ├── core
    │               │   ├── Candidate.java                   // Candidate doc Entity for Elasticsearch
    │               │   ├── CandidateIndexMsg.java           // Entity of candidate doc inside request body
    │               │   ├── FIType.java                      // Entity of thoth customized doc index type: EQ / FUZZY / RANGE ...
    │               │   ├── IndexBean.java                   // base class for other object
    │               │   ├── IndexField.java
    │               │   ├── IndexMsg.java                    // index API request body declaration and useful method.
    │               │   ├── JsonProvider.java                // JSON parser provider.
    │               │   ├── SItem.java                       // single search result item object definition.
    │               │   ├── SResult.java                     // search results object definition. List<SItem>
    │               │   ├── ThothActions.java                // RESTful API routers and handlers
    │               │   ├── ThothConfig.java                 // collection of thoth configurations.
    │               │   ├── ThothException.java              // base exception class.
    │               │   ├── ThothExceptionMapper.java        // exception mapping.
    │               │   ├── ThothIndexAbleType.java          // Canddiate field type enumeration
    │               │   ├── ThothQueryResult.java            // query result entity.
    │               │   ├── ThothResponse.java               // RESTful API response object.
    │               │   ├── ThothServer.java                 // entry point of thoth.
    │               │   ├── ThothService.java                // service base class for all thoth services.
    │               │   └── ThothServiceEsImp.java           // es operations implement used by ThothActions.
    │               ├── es
    │               │   ├── candidate.mapping.json           // ES main settings and mappings
    │               │   ├── CandidateIndexFactory.java       // ES operations with respect to candidate inherit from IndexFactory
    │               │   ├── ESService.java                   // ES API wrapper interface.
    │               │   ├── EsDao.java                       // ES data access object interface.
    │               │   ├── IndexFactory.java                // base index related operations factory class
    │               │   ├── IndexFactories.java              // utilities class for outside caller accessing different indices\' instance.
    │               │   └── SimpleEsService.java             // ES client wrapper.
    │               ├── help
    │               │   ├── ClassTools.java                  // class tool, merely for getting all specific type classes from package.
    │               │   ├── LogProvider.java                 // log4j logger
    │               │   ├── OkHttpUtil.java                  // wrapped http client base on okhttp3
    │               │   └── TextUtils.java                   // utilities for text
    │               ├── oql
    │               │   ├── FieldOQLCondition.java           // basic field query
    │               │   ├── GroupFun.java                    // group function support.
    │               │   ├── LimitPair.java                   // OQL limit keyword.
    │               │   ├── NumAryEqCond.java                // OQL array of number equal condition.
    │               │   ├── NumEqCond.java                   // OQL number equal condidtion.
    │               │   ├── NumNotEqCond.java                // OQL number not equal condidtion.
    │               │   ├── NumRangeCond.java                // OQL number range condition.
    │               │   ├── OQLCondition.java                // base OQL condition interface
    │               │   ├── OQLFuncFactory.java              // factory for getting specific function.
    │               │   ├── OQLFunction.java                 // main interface of other oql function.
    │               │   ├── OQLOrderBy.java                  // OQL order by single field.
    │               │   ├── OQLQuery.java                    // OQL execution.
    │               │   ├── ObjField.java                    // OQL field checker.
    │               │   ├── ParseException.java              // OQL parse exeception handler
    │               │   ├── SelectFun.java                   // implement select function.
    │               │   ├── SimpleCharStream.java            // **generated by javacc**
    │               │   ├── StrAryEqCond.java                // OQL array of string euqal condition
    │               │   ├── StringEqCond.java                // OQL string equal condition
    │               │   ├── StringLikeCond.java              // OQL string like condition
    │               │   ├── StringNotEqCond.java             // OQL string not equal condition.
    │               │   ├── StringSmartCond.java             // OQL string match involved in KnowledgeService
    │               │   ├── TestG.java                       // temp grammar test
    │               │   ├── ThothOqlGrammar.java             // **generated by javacc**
    │               │   ├── ThothOqlGrammar.jj               // original javacc grammar file
    │               │   ├── ThothOqlGrammarConstants.java    // **generated by javacc**
    │               │   ├── ThothOqlGrammarTokenManager.java // **generated by javacc**
    │               │   ├── Token.java                       // **generated by javacc**
    │               │   ├── TokenMgrError.java               // **generated by javacc**
    │               │   └── UnionCond.java
    │               └── query
    │                   ├── KnowledgeService.java            // knowledge graph service basic interface.
    │                   └── KnowledgeServiceImpl.java        // Implementation for Knwoledge service.
    └── resources
        ├── search-descriptor.properties
        └── search-security.policy
```

[staging-1.svc.k8s.staging.mokahr.com](http://staging-1.svc.k8s.staging.mokahr.com/):8080
