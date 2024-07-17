set -e

mongosh <<EOF
db = db.getSiblingDB("$MONGO_INITDB_DATABASE")

db.createUser(
  {
    user:  "$MONGO_USERNAME",
    pwd: "$MONGO_PASSWORD",
    roles: [{"role": "dbOwner", "db": "$MONGO_INITDB_DATABASE"}]
  }
);
EOF