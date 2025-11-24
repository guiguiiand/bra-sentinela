#!/usr/bin/env bash
set -euo pipefail

echo "=== INICIANDO TODO O PROJETO BRA-SENTINELA ==="

# ==============================
# CONFIGURAÇÕES
# ==============================
ROOT_DIR="$HOME/Documents/bra-sentinela"
BACK_DIR="$ROOT_DIR/back-end"
LOG_DIR="$BACK_DIR/logs"
COMPOSE_FILE="$BACK_DIR/docker-compose.yml"

# microservices
ADMIN_DIR="$BACK_DIR/admin-service"
API_GATEWAY_DIR="$BACK_DIR/api-gateway"
AUTH_DIR="$BACK_DIR/auth-service"
COMPLAINT_DIR="$BACK_DIR/complaint-service"
USER_DIR="$BACK_DIR/user-service"
REPORT_DIR="$BACK_DIR/report-service"

JAVA_OPTS="-Xms256m -Xmx512m"
PORTS=(3000 3001 3002 3003 3004 3005 3006 5173 8090)

# ==============================
# FUNÇÕES
# ==============================
ensure_logs_dir() {
    mkdir -p "$LOG_DIR"
}

kill_port() {
  local port="$1"
  PID=$(lsof -t -i:$port || true)
  if [[ -n "$PID" ]]; then
      echo "[KILL] Porta $port (PID $PID)"
      kill -9 "$PID" || true
  else
      echo "[OK] Porta $port está livre"
  fi
}

build_service() {
  local dir="$1"
  echo "[BUILD] $dir"
  pushd "$dir" >/dev/null
  mvn -q -DskipTests clean package
  popd >/dev/null
}

run_service() {
  local name="$1"
  local dir="$2"

  echo "[RUN] $name"
  pushd "$dir" >/dev/null

  SPRING_PROFILES_ACTIVE="default" \
  JAVA_TOOL_OPTIONS="$JAVA_OPTS" \
  nohup mvn -q spring-boot:run > "$LOG_DIR/$name.log" 2>&1 &

  echo $! > "$LOG_DIR/$name.pid"
  popd >/dev/null
}

# ==============================
# INICIANDO
# ==============================
ensure_logs_dir

echo "🔪 Liberando portas..."
for port in "${PORTS[@]}"; do
  kill_port "$port"
done

echo "🐳 Subindo Docker Compose..."
sudo docker-compose -f "$COMPOSE_FILE" up -d

echo "🔨 Buildando microserviços..."
build_service "$ADMIN_DIR"
build_service "$API_GATEWAY_DIR"
build_service "$AUTH_DIR"
build_service "$COMPLAINT_DIR"
build_service "$USER_DIR"
build_service "$REPORT_DIR"

echo "🚀 Iniciando microserviços..."
run_service "admin-service" "$ADMIN_DIR"
run_service "api-gateway" "$API_GATEWAY_DIR"
run_service "auth-service" "$AUTH_DIR"
run_service "complaint-service" "$COMPLAINT_DIR"
run_service "user-service" "$USER_DIR"
run_service "report-service" "$REPORT_DIR"

echo "🌐 Iniciando front-end (Vite React)..."
pushd "$ROOT_DIR" >/dev/null
nohup npm run dev > "$LOG_DIR/front.log" 2>&1 &
echo $! > "$LOG_DIR/front.pid"
popd >/dev/null

echo ""
echo "=============================================="
echo " ✔ TODOS OS SERVIÇOS RODANDO!"
echo " ✔ Logs em: $LOG_DIR"
echo " ✔ API Gateway → http://localhost:8090"
echo " ✔ Front-end → http://localhost:5173"
echo "=============================================="
