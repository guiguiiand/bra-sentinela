# Bra-Sentinela

Sistema de denúncias e reclamações baseado em microsserviços.

## Estrutura do Projeto

```
bra-sentinela/
├── back-end/              # Microsserviços Java Spring Boot
├── complaintCenter-frontend/   # Aplicação React
└── data-base/             # Scripts e migrations SQL
```

##  Como Executar

### 1. Subir Infraestrutura (Docker)
```bash
cd back-end
docker-compose up -d
```

### 2. Executar Microsserviços
```bash
cd back-end/api-gateway
./mvnw spring-boot:run
```

### 3. Executar Frontend
```bash
cd complaintCenter-frontend

```

##  URLs Importantes

- API Gateway: http://localhost:8090
- RabbitMQ Management: http://localhost:15672
- MinIO Console: http://localhost:9001
- Grafana: http://localhost:3000
- Kibana: http://localhost:5601

## Equipe


