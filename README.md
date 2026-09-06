## KV-Store Implementation, w/ Raft Consensus


## Project Goal & Plan

- Building a distributed key-value store
that implements Raft consensus algorithm,
first in Java, later refactored to Go (partly to learn Go)
- Going at own pace, no timeline
- 8-phase build plan: (1)single-node KV store,
(2) add persistence (WAL + snapshotting),
(3) stand up a multi-node cluster w/ no consensus yet, (4) leader election, (5) log
replication, (6) failure/partition handling,
(7) client-facing consistency guarantees,
(8) stress testing + documentation


## Conceptual Grounding established

- KV store --> dictionary-like service
(get/set/delete) exposed over a network interface, as its own standalone process. The network exposure is what distinguishes it from an in-process data structure
- "In memory" = data lives in RAM inside your running process; disappearse on process exist unless its persisted to disk (what Phase 2 solves)
- single-node vs. multi-node = one copy of the data vs. several coordinate copies that tolerate individual machine failure
- Raft basics: leader election (randomized timeout prevents split votes), log replication (leader-only writes, commited once replicated to a majority), and safety property (a candidate can only win election if its log is at least as up-to-date as a majority. This is what prevents losing commited data)
- Thread pool = fixed set of reusable worker threads, avoids cost/risk of spawning unbounded OS threads per connection
- Go's value for this project: garbage collected like Java, but compiles to native code like C (no JVM overhead); goroutines/channels make concurrent per-node logic (timers, heartbeats, RPCs) a lot lighter to write than in java


## Decided Architecture

- `KVServer` - owns the accept loop and thread pool only; hands off each new client `Socket` to a worker thread; knows nothing about protocol or storage
- `ClientHandler` - per-client read loop; owns protocol parsing (a single text protocol supporting multiple commands, e.g. `GET key`, `SET key value`, `DEL key` distinguished by the first word)
- `KVStore` - actual data (plain `HashMap`, manually synchronized) plus get/set/delete methods; knows nothing about sockets, threads or protocol. Fully decoupled from transport, so it can later sit behind HTTP, raw TCP, or inter-node RPC without changes