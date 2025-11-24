#!/usr/bin/env bash
set -euo pipefail

echo "=============================================="
echo "     INSTALADOR DO PROJETO BRA-SENTINELA"
echo "=============================================="

# =============================
# VERIFICA SE É ROOT
# =============================
if [[ "$EUID" -ne 0 ]]; then
    echo "❌ Por favor execute como root: sudo ./setup_bra_sentinela.sh"
    exit 1
fi

# =============================
# ATUALIZA SISTEMA
# =============================
echo "🔄 Atualizando sistema..."
apt update -y
apt upgrade -y

# =============================
# INSTALA PACOTES BASE
# =============================
echo "📦 Instalando pacotes essenciais..."
apt install -y curl wget git unzip lsof build-essential software-properties-common

# =============================
# INSTALA JAVA 17
# =============================
echo "☕ Instalando Java 17..."
apt install -y openjdk-17-jdk

# =============================
# INSTALA MAVEN
# =============================
echo "📐 Instalando Maven..."
apt install -y maven

# =============================
# INSTALA NODE + NPM
# =============================
echo "🟩 Instalando Node.js 18 LTS..."
curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
apt install -y nodejs

# =============================
# INSTALA DOCKER
# =============================
echo "🐳 Instalando Docker..."
apt install -y apt-transport-https ca-certificates gnupg-agent software-properties-common

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -

add-apt-repository \
   "deb [arch=$(dpkg --print-architecture)] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"

apt update -y
apt install -y docker-ce docker-ce-cli containerd.io

systemctl enable docker
systemctl start docker

# =============================
# INSTALA DOCKER COMPOSE
# =============================
echo "📦 Instalando Docker Compose..."
DOCKER_COMPOSE_VERSION="2.27.1"

curl -SL \
  "https://github.com/docker/compose/releases/download/v${DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" \
  -o /usr/local/bin/docker-compose

chmod +x /usr/local/bin/docker-compose

echo "docker-compose version: $(docker-compose --version)"

# =============================
# ADICIONA USUÁRIO AO GRUPO DOCKER
# =============================
echo "👤 Adicionando usuário ao grupo docker..."
usermod -aG docker "$SUDO_USER"

# =============================
# CLONAR O PROJETO (O USUÁRIO EDITA AQUI)
# =============================
PROJECT_DIR="/home/$SUDO_USER/bra-sentinela"

if [[ ! -d "$PROJECT_DIR" ]]; then
    echo "🔄 Clonando projeto BRA-SENTINELA..."
    sudo -u "$SUDO_USER" git clone https://github.com/SEU_USUARIO/SEU_REPO.git "$PROJECT_DIR"
else
    echo "📁 Projeto já existe em $PROJECT_DIR"
fi

# =============================
# INSTALA DEPENDÊNCIAS DO FRONT-END
# =============================
echo "🟦 Instalando dependências do front-end..."
sudo -u "$SUDO_USER" bash -c "
  cd $PROJECT_DIR &&
  npm install
"

# =============================
# PERMISSÕES PARA SCRIPTS
# =============================
echo "🔧 Ajustando permissões dos scripts..."
chmod +x "$PROJECT_DIR/start_all.sh" || true
chmod +x "$PROJECT_DIR/stop_all.sh" || true

echo ""
echo "=============================================="
echo " ✔ Instalação concluída!"
echo " ✔ Projeto clonado em: $PROJECT_DIR"
echo " ✔ Docker instalado"
echo " ✔ Java 17 + Maven instalados"
echo " ✔ Node 18 + NPM instalados"
echo " ✔ Para rodar o projeto:"
echo ""
echo "     cd $PROJECT_DIR"
echo "     ./start_all.sh"
echo ""
echo "⚠ IMPORTANTE: faça logout e login novamente"
echo "para ativar permissões do grupo docker."
echo "=============================================="
