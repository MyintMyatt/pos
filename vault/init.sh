##!/bin/sh
#sleep 3
#echo "Initializing Vault..."
##
### Enable KV secrets engine (v2)
##curl --silent --header "X-Vault-Token: $VAULT_TOKEN" \
##     --request POST \
##     --data '{"type": "kv-v2"}' \
##     "$VAULT_ADDR/v1/sys/mounts/secret" \
##     || echo "KV engine may already exist."
#
## Enable KV secrets engine only if not already enabled
#if ! curl --silent --header "X-Vault-Token: $VAULT_TOKEN" "$VAULT_ADDR/v1/sys/mounts" | grep -q '"secret/":'; then
#  echo "Enabling KV v2 engine..."
#  curl --silent --header "X-Vault-Token: $VAULT_TOKEN" \
#       --request POST \
#       --data '{"type": "kv-v2"}' \
#       "$VAULT_ADDR/v1/sys/mounts/secret"
#else
#  echo "KV engine already enabled at secret/"
#fi

#!/bin/sh

sleep 3
echo "Initializing Vault..."

# Enable KV secrets engine (v2)
curl --silent --header "X-Vault-Token: $VAULT_TOKEN" \
     --request POST \
     --data '{"type": "kv-v2"}' \
     "$VAULT_ADDR/v1/sys/mounts/secret" \
     || echo "KV engine may already exist."
# Write application secrets under secret/data/pos

echo "Writing secret data to Vault..."
curl --silent --header "X-Vault-Token: $VAULT_TOKEN" \
      --request POST \
        --data '{"data": {
            "db-url": "jdbc:postgresql://localhost:5432/pos",
            "db-username": "root",
            "db-password": "passowrd",
            "cloudinary-cloud-name": "cloudname",
            "cloudinary-api-key": "apikey",
            "cloudinary-api-secret": "secret",
             "redis-pw": "helloworld",
            "frontend-url": "http://localhost:5173",
            "jwt-secret-key": "0b1ac160f2abb47ca5a5de8cfe139586b55db5cc6974837979d42366a67b9e89aceae2639005059e277243c81239f8be299f453c4d8e36a21a8fe3e36df1e90b"
        }}' \
    "$VAULT_ADDR/v1/secret/data/pos"

sleep 10
echo "Vault initialization complete."

