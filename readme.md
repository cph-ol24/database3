## SQL

| Depth        | Total time           | Median  |
| ------------- |-------------| -----:|
| #0      | 0.5850000000000002ms | 0.02925000000000001ms |
| #1      | 0.22200000000000006ms      |   0.011100000000000002ms |
| #2 | 0.18900000000000006ms      |    0.009450000000000004ms |
| #3 | 0.18600000000000005ms|  0.009300000000000003ms |
| #4 |  0.191ms | 0.00955ms |

I had to create a index on `targetId` otherwise it would take too long time for depth #1.

## NEO4J Setup

### Create persons
```
LOAD CSV FROM "file:///social_network_nodes_small.csv" AS line
CREATE (:Person { node_id: line[0], name: line[1], job: line[2], birthdate: line[3] });
```

### Create relations
```
LOAD CSV FROM "file:///social_network_edges_small.csv" AS line
MATCH (p1:Person { node_id: line[0] })
MATCH (p2:Person { node_id: line[1] })
CREATE (p1)-[:ENDORSES]->(p2)
```

### Query
```
MATCH (a:Person {node_id: "20" })-[b:ENDORSES*2]->(c:Person)
RETURN c
```

Vi kan ændre "20" og 2. 20 er noden, og 2 er hvor langt vi vil gå ud.
