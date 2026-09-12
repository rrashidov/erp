#!/bin/bash
set -euo pipefail

# Finds free TCP ports on the host and writes them to docker/env.dev.ports so
# that docker-compose and local test scripts don't collide with ports already
# taken by other, unrelated processes/containers.

OUT_FILE="$(dirname "$0")/../docker/env.dev.ports"

is_port_free() {
  ! lsof -nP -iTCP:"$1" -sTCP:LISTEN >/dev/null 2>&1
}

pick_free_port() {
  local port
  while true; do
    port=$(( (RANDOM % 20000) + 20000 ))
    if is_port_free "$port"; then
      echo "$port"
      return
    fi
  done
}

BACKEND_PORT=$(pick_free_port)
FRONTEND_PORT=$(pick_free_port)
while [ "$FRONTEND_PORT" = "$BACKEND_PORT" ]; do
  FRONTEND_PORT=$(pick_free_port)
done
RABBITMQ_ADMIN_PORT=$(pick_free_port)
while [ "$RABBITMQ_ADMIN_PORT" = "$BACKEND_PORT" ] || [ "$RABBITMQ_ADMIN_PORT" = "$FRONTEND_PORT" ]; do
  RABBITMQ_ADMIN_PORT=$(pick_free_port)
done

cat > "$OUT_FILE" <<EOF
BACKEND_PORT=${BACKEND_PORT}
FRONTEND_PORT=${FRONTEND_PORT}
RABBITMQ_ADMIN_PORT=${RABBITMQ_ADMIN_PORT}
EOF

echo "Picked free ports -> backend:${BACKEND_PORT} frontend:${FRONTEND_PORT} rabbitmq-admin:${RABBITMQ_ADMIN_PORT}"
