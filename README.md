# Sistema de Verificação de Documentos QUOD

Aplicação Spring Boot para verificação biométrica facial, impressão digital e análise de documentos com detecção de fraudes.

## Funcionalidades

- Verificação biométrica facial
- Verificação de impressão digital
- Análise e verificação de documentos
- Detecção de fraude para todos os tipos de verificação
- Notificação automática para eventos de fraude
- Persistência em MongoDB para resultados de verificação

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.0
- Spring Data MongoDB
- Lombok
- Springdoc OpenAPI (Swagger UI)

## Requisitos

- Java 17 ou superior
- Maven
- MongoDB

## Instalação e Execução

### Passo 1: Configurar o MongoDB

**Para MacOS:**

```bash
# Para instalar o MongoDB (MacOS)
brew tap mongodb/brew && brew install mongodb-community

# Para iniciar o MongoDB
brew services start mongodb/brew/mongodb-community

# Verificar se o MongoDB está rodando
ps aux | grep -v grep | grep mongod
```

**Para Windows:**

1. Baixe o MongoDB Community Server do [site oficial](https://www.mongodb.com/try/download/community)
2. Execute o instalador e siga as instruções (recomendamos selecionar "Complete Installation" e deixar marcada a opção "Install MongoDB as a Service")
3. Por padrão, o MongoDB será instalado em `C:\Program Files\MongoDB\Server\[versão]`
4. Opcionalmente, instale o MongoDB Compass para interface gráfica (geralmente incluído no instalador)
5. Verificar e iniciar o serviço MongoDB:

```cmd
# Verificar se o serviço está rodando
sc query MongoDB

# Se não estiver iniciado, inicie o serviço
net start MongoDB

# Criar o diretório de dados se necessário
mkdir C:\data\db
```

Nota: Se você não instalou como serviço, pode iniciar o MongoDB manualmente:

```cmd
# Execute este comando a partir do diretório de instalação do MongoDB
"C:\Program Files\MongoDB\Server\[versão]\bin\mongod.exe" --dbpath="C:\data\db"
```

### Passo 2: Clonar e Configurar o Projeto

1. Clone o repositório
2. Navegue até a pasta do projeto
3. A conexão ao MongoDB já está configurada em `application.yml`

### Passo 3: Executar o Projeto

**Para MacOS/Linux:**

**Opção 1: Usando o script de instalação**

```bash
# Dê permissão de execução ao script (se necessário)
chmod +x install.sh

# Execute o script
./install.sh
```

**Opção 2: Usando comandos Maven diretamente**

```bash
# Compilar o projeto
./mvnw clean install

# Executar o projeto
./mvnw spring-boot:run
```

**Para Windows:**

```cmd
# Compilar o projeto
mvnw.cmd clean install

# Executar o projeto
mvnw.cmd spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## API Endpoints

Após iniciar a aplicação, você pode acessar a documentação Swagger UI em:

```
http://localhost:8080/swagger-ui.html
```

### Endpoints Disponíveis

1. **Verificação Biométrica Facial**

   - POST `/api/v1/verification/facial`
   - Formato: `multipart/form-data`
   - Parâmetros:
     - `userId`: ID do usuário (obrigatório)
     - `faceImage`: Imagem facial (obrigatório)
     - `deviceInfo`: Informações do dispositivo (opcional)
     - `geoLocation`: Localização geográfica (opcional)

2. **Verificação de Impressão Digital**

   - POST `/api/v1/verification/fingerprint`
   - Formato: `multipart/form-data`
   - Parâmetros:
     - `userId`: ID do usuário (obrigatório)
     - `fingerprintImage`: Imagem da impressão digital (obrigatório)
     - `fingerPosition`: Posição do dedo (obrigatório) - Valores: RIGHT_INDEX, LEFT_INDEX, RIGHT_THUMB, LEFT_THUMB, etc.
     - `deviceInfo`: Informações do dispositivo (opcional)
     - `geoLocation`: Localização geográfica (opcional)

3. **Análise de Documento**
   - POST `/api/v1/verification/document`
   - Formato: `multipart/form-data`
   - Parâmetros:
     - `userId`: ID do usuário (obrigatório)
     - `documentImage`: Imagem do documento (obrigatório)
     - `faceImage`: Imagem facial para comparação (obrigatório)
     - `documentType`: Tipo de documento (obrigatório) - Valores: ID_CARD, PASSPORT, DRIVER_LICENSE
     - `deviceInfo`: Informações do dispositivo (opcional)
     - `geoLocation`: Localização geográfica (opcional)

## Testando a Aplicação

### Preparação para Testes

Você pode criar uma pasta para armazenar imagens de teste:

**MacOS/Linux:**

```bash
mkdir -p test_images

# Criar arquivos de imagem de exemplo
echo "Este é um teste de imagem" > test_images/face.jpg
echo "Este é um teste de impressão digital" > test_images/fingerprint.jpg
echo "Este é um teste de documento" > test_images/document.jpg
```

**Windows:**

```cmd
mkdir test_images

# Criar arquivos de imagem de exemplo
echo Este é um teste de imagem > test_images\face.jpg
echo Este é um teste de impressão digital > test_images\fingerprint.jpg
echo Este é um teste de documento > test_images\document.jpg
```

### Testando com cURL

#### Verificação Facial

**MacOS/Linux:**

```bash
curl -X POST "http://localhost:8080/api/v1/verification/facial" \
  -H "Content-Type: multipart/form-data" \
  -F "userId=123456" \
  -F "faceImage=@test_images/face.jpg"
```

**Windows:**

```cmd
curl -X POST "http://localhost:8080/api/v1/verification/facial" -H "Content-Type: multipart/form-data" -F "userId=123456" -F "faceImage=@test_images\face.jpg"
```

#### Verificação de Impressão Digital

**MacOS/Linux:**

```bash
curl -X POST "http://localhost:8080/api/v1/verification/fingerprint" \
  -H "Content-Type: multipart/form-data" \
  -F "userId=123456" \
  -F "fingerprintImage=@test_images/fingerprint.jpg" \
  -F "fingerPosition=RIGHT_INDEX"
```

**Windows:**

```cmd
curl -X POST "http://localhost:8080/api/v1/verification/fingerprint" -H "Content-Type: multipart/form-data" -F "userId=123456" -F "fingerprintImage=@test_images\fingerprint.jpg" -F "fingerPosition=RIGHT_INDEX"
```

#### Análise de Documento

**MacOS/Linux:**

```bash
curl -X POST "http://localhost:8080/api/v1/verification/document" \
  -H "Content-Type: multipart/form-data" \
  -F "userId=123456" \
  -F "documentImage=@test_images/document.jpg" \
  -F "faceImage=@test_images/face.jpg" \
  -F "documentType=ID_CARD"
```

**Windows:**

```cmd
curl -X POST "http://localhost:8080/api/v1/verification/document" -H "Content-Type: multipart/form-data" -F "userId=123456" -F "documentImage=@test_images\document.jpg" -F "faceImage=@test_images\face.jpg" -F "documentType=ID_CARD"
```

### Verificando Resultados no MongoDB

Para verificar os resultados armazenados no MongoDB:

**MacOS/Linux:**

```bash
# Conectar ao MongoDB e listar as coleções
mongosh "mongodb://localhost:27017/biometric_verification" --eval "db.getCollectionNames()"

# Consultar os resultados de verificação
mongosh "mongodb://localhost:27017/biometric_verification" --eval "db.verification_results.find().toArray()"
```

**Windows:**

```cmd
# Conectar ao MongoDB e listar as coleções
mongosh "mongodb://localhost:27017/biometric_verification" --eval "db.getCollectionNames()"

# Consultar os resultados de verificação
mongosh "mongodb://localhost:27017/biometric_verification" --eval "db.verification_results.find().toArray()"
```

Alternativamente, no Windows, você pode usar o MongoDB Compass para visualizar os dados graficamente.

## Formatos de Resposta da API

As respostas da API seguem este formato:

**Exemplo de Verificação Bem-Sucedida:**

```json
{
  "id": "680e4293db8187011846756f",
  "userId": "123456",
  "verificationType": "FACIAL_BIOMETRY",
  "processedAt": "2025-04-27T11:43:31.789334",
  "fraudDetected": false,
  "fraudTypes": [],
  "status": "APPROVED",
  "message": "Verification successful"
}
```

**Exemplo de Fraude Detectada:**

```json
{
  "id": "680e4293db8187011846756f",
  "userId": "123456",
  "verificationType": "FACIAL_BIOMETRY",
  "processedAt": "2025-04-27T11:43:31.789334",
  "fraudDetected": true,
  "fraudTypes": ["DEEPFAKE", "PHOTO_OF_PHOTO"],
  "status": "REJECTED",
  "message": "Fraud detected during verification"
}
```

## Estrutura do Projeto

- `src/main/java/com/quod/biometric/controller`: Controladores REST
- `src/main/java/com/quod/biometric/service`: Serviços de lógica de negócio
- `src/main/java/com/quod/biometric/model`: Entidades MongoDB
- `src/main/java/com/quod/biometric/dto`: Objetos de transferência de dados
- `src/main/java/com/quod/biometric/repository`: Repositórios MongoDB
- `src/main/java/com/quod/biometric/exception`: Tratamento de exceções
- `src/main/java/com/quod/biometric/config`: Classes de configuração

## Notas Importantes

1. **Simulações**:

   - **Validação de imagem**: Verifica formato, tamanho e extrai metadados
   - **Detecção de fraude**: Simula detecção de padrões de fraude como deepfakes, foto-de-foto, impressões digitais falsas, etc.
   - **Serviço de notificação**: Simula envio de notificações HTTP para sistemas externos

2. **Detecção de Fraude**:

   - A detecção de fraude é simulada com probabilidades aleatórias
   - O arquivo `FraudDetectionService.java` contém as probabilidades para cada tipo de fraude
   - Em um ambiente de produção, isso seria substituído por algoritmos reais de detecção

3. **Formatos de Imagem**:

   - Os formatos suportados são JPEG e PNG (configurados em `application.yml`)
   - Para fins de teste, qualquer imagem JPG ou PNG pode ser utilizada

4. **Teste com Maior Probabilidade de Fraude**:
   - Para forçar um teste com maior probabilidade de fraude, modifique o arquivo `src/main/java/com/quod/biometric/service/FraudDetectionService.java` aumentando os valores de probabilidade para mais próximos de 1.0
