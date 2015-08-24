#For windows, create the 3 directories manually and then run each mongod command on a separate shell
start mongod --replSet m101 --logpath "1.log" --dbpath /data/rs1 --port 27017 --smallfiles --oplogSize 64
start mongod --replSet m101 --logpath "2.log" --dbpath /data/rs2 --port 27018 --smallfiles --oplogSize 64
start mongod --replSet m101 --logpath "3.log" --dbpath /data/rs3 --port 27019 --smallfiles --oplogSize 64

#Once the replicas are created use the init_replica.js file for initiating the replication set using the command

# mongo --port 27017 < init_replica.js