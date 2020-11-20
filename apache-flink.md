# Apache Flink
Based on [GOTO 2019 Conference talk][yt-apache-flink-intro] by Robert Metzger.

Apache Flink is an open source stream processing framework allowing for:  

* Low latency
* High throughput
* Stateful data processing
* Distributed (scalable up & down)

## Data oriented definition

Another way of looking at Flink is **a stateful computations over data streams**. In practice it means it could be used for:  

* Batch processing (process static or historic data in a fast way)
* Data stream processing (process real time data)
* Event-driven processing (allows for analytics use cases over Flink or other stream processing frameworks)

Ververica is the company standing behind Apache Flink. They sell `Ververica Platform` which is a tooling around Apache Flink, to make it easier deploy it and manage in production. 




## Use case #1: Streaming ETL

### Batch jobs
Traditionally ETL is done by triggering periodic jobs once per a number of hours, which process data in a batch manner. Tools like Spark or Hadoop [E]xtract data from source, [T]ransform it and normalize according to some rules and [L]oad at the end to a destination. 

### Data pipelines
Instead of running a batch job every now and then, Flink continuously listens to incoming data, processes it in real time and writing in buckets of hours or minutes into file system or event log.

**Flink's internal checking mechanism does not allow for data loss.**


## Use case #2: Data analytics 


### Batch analytics
It is popular when there is a lot of data, which you want to interactively explore like ad-hoc queries, interactive analytics, prototyping. 

##### Rule of thumb
If queries change faster than your data, then batch approach makes a lot of sense. 

### Stream analytics
It is used when there is a fairly static query, which looks at the data stream searching for pattern or constantly updating the model and its understanding of the world. 

##### Rule of thumb
If data changes faster then your queries, then streaming approach makes a lot of sense. 


##### Lambda Architecture
Back in a day, there was an approach of `Lambda Architecture` providing `batch processor` for ensuring correct results, but in slower pace and `stream processor` for fast and incorrect estimations. 

This is now not necessary with streaming processor like Apache Flink, where results could be provided in fast and correct manner. So there is less overhead, less maintenance & development cost.


## Use case #3: Event-driven applications
Traditionally, there is an application which consumes events, perhaps enriching them by querying a database, processing and triggering some action further down the line.  

Problems 

* Database schema and the application code has to be synchronised
* Database scalability is far more difficult than application scalability
* Crossing organisational boundary while scaling database, as usually it is maintained by other database team

Stream processing architecture it is more like microservice architecture, where everything in contained in Flink (both data and compute is in one system).

Flink guarantees consistency and your data is local, so no boundaries have to be crossed. As a matter of fact, there is no network delay/hit, to go to fetch data. 

Easy to scale, as it is just a matter of adding more nodes, which do single task. It could be called a nanoservice architecture, where there is a lot of small streaming jobs, solving one specific problem very efficiently. Another benefit is that organisational boundaries do not have to be crossed and each streaming job could be maintained by a single team. 


## Case Study
Chinese Shopping Festival: [Single's Day][singles-day] is equivalent to Black Friday in EU/US. 

Alibaba uses Flink to handle many areas incl.:

* payments
* shipping
* real time recommendations 
* real time revenue dashboard shown in TV


## Building Blocks

### Event Streams
Topology of operators, where events are flowing through. Streams are able to process real time data and historic data. 



### State
Flink knows your data, how to back up and restore your data. This allows to provide ***exactly once*** processing guarantee. 

Window operator content is the state e.g. five second window handling some data. 

### (Event) Time
Handling of time allows to deal with out of order events, late arrival events and also for processing of historic data. 

Flink has four notions of time:

* Event time - as it occurred in reality (correct results, but high latency)
* Broker time - when it arrived to Kafka
* Ingestion time - when it arrived to Flink system 
* Window processing time - when it arrived to particular node with `[ ]` (window) operator (ultra low latency, but most likely incorrect results)

### Snapshots
Allow users to work with the state also outside of Flink. If they want to migrate the state to different job or to larger cluster, snapshots could be used. 


## API Types
All APIs could be mixed and matched. 

### Stateful Event-Driven Applications
Lowest level APIs allowing access to raw ingredients of Flink: events, state, time. 

It is a primary API, if you want to build custom, event driven application. 


### Stream & Batch Processing
Middle level API called DataStreamAPI (streams, windows). It has operators like: map, filter, flatMap, keyBy, window. 

It could be compared to what Spark level API presents. 


### Analytics use cases
High level APIs: Stream SQL and Table API (dynamic tables). This layer translates to Stream layer API (mid layer). 

Allows to express problem really quickly, in few lines of code. Flink take care of heavy lifting and all necessary optimisations underneath. 


## Deployment Options

### Master-Worker model
There is one central node `master` (which can also run in high availability mode `stand=-by masters` using `ZooKeeper`), which basically dispatch jobs to `worker` nodes. 

It could be deployed in stand alone mode, were developer manually configures machines so they find each other. 

Alternatively, `Yarn` or `Mesos` could be used to manage deployment. 


Once Flink cluster is running, one of the clients (REST, Java, command line) could be used to submit code/jar to Flink cluster to master node. Then master takes care of distributing the job to worker nodes.


### Container mode (Kubernetes)
There are Kubernetes containers for worker nodes and for master. Mater node also has jar package, which is picked up immediately after container deployment. 


### Cloud 
AWS provides cloud service hosting Flink.


## Integrations

### Event Logs

* Kafka
* Kinesis
* Pulsar

### File Systems

* S3
* HDFS
* NFS

### Encodings

* Avro
* JSON
* CSV
* ORC
* Parquet

### Databases

* JDBC
* HCatalog

### Key-Value Stores

* Cassandra
* ElasticSearch
* Redis



[yt-apache-flink-intro]: https://www.youtube.com/watch?v=DkNeyCW-eH0
[singles-day]: https://www.ververica.com/blog/singles-day-2018-data-in-a-flink-of-an-eye