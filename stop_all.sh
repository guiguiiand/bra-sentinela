#!/usr/bin/env bash
set -euo pipefail

echo "=== ENCERRANDO TODO O PROJETO BRA-SENTINELA ==="

ROOT_DIR="$HOME/Documents/bra-sentinela"
BACK_DIR="$ROOT_DIR/back-end"
LOG_DIR="$BACK_DIR/logs"
COMPOSE_FILE="$BACK_DIR/docker-compose.yml"

PORTS=(3000 3001 3002 3003 3004 3005 3006 5173 8090)

kill_pid_file() {
  local file="$1"
  if [[ -f "$file" ]]; then
    PID=$(cat "$file")
    if ps -p "$PID" > /dev/null 2>&1; then
        echo "[STOP] Matando PID $PID"
        kill -9 "$PID" || true
    fi
    rm -f "$file"
  fi
}

echo "🛑 Matando microserviços..."
kill_pid_file "$LOG_DIR/admin-service.pid"
kill_pid_file "$LOG_DIR/api-gateway.pid"
kill_pid_file "$LOG_DIR/auth-service.pid"
kill_pid_file "$LOG_DIR/complaint-service.pid"
kill_pid_file "$LOG_DIR/user-service.pid"
kill_pid_file "$LOG_DIR/report-service.pid"
kill_pid_file "$LOG_DIR/front.pid"

echo "🔪 Matando processos nas portas..."
for port in "${PORTS[@]}"; do
  PID=$(lsof -t -i:$port || true)
  if [[ -n "$PID" ]]; then
      echo "[KILL] Porta $port → PID $PID"
      kill -9 "$PID" || true
  fi
done

echo "🐳 Derrubando containers..."
sudo docker-compose -f "$COMPOSE_FILE" down

echo ""
echo "=============================================="
echo " ✔ TODOS OS SERVIÇOS ENCERRADOS!"
echo " ✔ Portas liberadas!"
echo "=============================================="
