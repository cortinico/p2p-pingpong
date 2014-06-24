Gnutella Ping Pong Protocol - P2P
============

This software is a simulation of the Gnutella Ping Pong protocol ([Official Website](http://rfc-gnutella.sourceforge.net/)). Gnutella is a Peer-2-Peer protocol, used the share information across the network. The Ping-Pong protocol is used for discovering new nodes across the network.

The simulation is written in Java ad use the Java Threads Objects.

![Epidemic](http://tutorials.jenkov.com/images/p2p/disorganized-network-1.png)

## Documentation

For better understanding the workflow of the simulation you can read:
* [PDF Relation (in italian)](../../raw/master/doc/tex/relazione.pdf)
* [JavaDoc Documentation](http://cortinico.github.io/p2p-pingpong/)

## Executing the simulation

For executing the simulation you must execute the following steps
```bash
git clone git@github.com:cortico/p2p-pingpong.git
cd p2p-pingpong/
ant build
```

Then you can choose from 3 different Ping Pong Implementation

|Implementation | Description |
| --- | --- |
| Simple | That use the simple ping pong algorithm (without caching) |
| Cache | That use the cached version of ping pong algorithm |
| Refined | That use the refined version of pong caching algorithm |

And then execute with
```
ant <implementation>
```
