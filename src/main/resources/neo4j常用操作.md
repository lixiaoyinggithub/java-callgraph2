
- 全部删除

```

MATCH (n)
OPTIONAL MATCH (n)-[r]-()
DELETE n,r

```

- 查询所有子孙节点
```
MATCH (n)-[:invoke*]->(descendant)
WHERE ID(n) = 48782
RETURN descendant
```
- 根据属性模糊查询；查询所有RPC实现类
```

MATCH (n)
WHERE n.cls CONTAINS {' com.buz.dc.core.service'}
RETURN ID(n) as nodeId 

```

- 根据名称查找指向的节点
```

MATCH (:Method { name: 'com.buz.dc.core.service.activity.ChallengeServiceImpl:getChallengeBackgroundList()' })-->(method)
RETURN method

```