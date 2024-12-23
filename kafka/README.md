# Kafka Микросервисы на Spring Boot

## Описание проекта

В этом проекте реализована система из трёх микросервисов на Spring Boot, взаимодействующих через Apache Kafka:

1. **Producer** — сервис, отправляющий сообщения в Kafka.
2. **Consumer First** — первый потребитель, сохраняющий полученные сообщения в базу данных PostgreSQL.
3. **Consumer Second** — второй потребитель, обрабатывающий сообщения и логирующий информацию в зависимости от возраста пользователя.

Также проект содержит `docker-compose.yml` для развертывания необходимых сервисов: PostgreSQL, Zookeeper и Kafka.

## Структура проекта
```
.
├── docker-compose.yml
├── producer
│   ├── src/main/java/org/example
│   ├── src/main/resources
│   └── build.gradle
├── first-consumer
│   ├── src/main/java/org/example
│   ├── src/main/resources
│   └── build.gradle
└── second-consumer
    ├── src/main/java/org/example
    ├── src/main/resources
    └── build.gradle
```
## Требования

- **Docker** и **Docker Compose** — для развертывания Kafka, Zookeeper и PostgreSQL.
- **Java 17** или выше.
- **Gradle** — для сборки проектов (можно использовать Gradle Wrapper).
- **PostgreSQL** — версия 14.8 (устанавливается через Docker).

## Установка и запуск

### 1. Клонирование репозитория
```bash
git clone <URL_ВАШЕГО_РЕПОЗИТОРИЯ>
```
```bash
cd <ИМЯ_РЕПОЗИТОРИЯ>
```
### 2. Настройка Docker Compose

Файл `docker-compose.yml` находится в корне проекта и содержит конфигурацию для следующих сервисов:

- **PostgreSQL**: порт `5433`.
- **Zookeeper**: порт `2181`.
- **Kafka**: порт `9092`.


Примечания:

- **PostgreSQL**: Данные будут сохранены на хосте в папке `./initdb`. Убедитесь, что эта папка существует или измените путь по необходимости.
- **Kafka**: Создаётся топик `test_topic` с 1 партицией и 3 репликами.

### 3. Запуск Docker Compose
```bash
docker-compose up -d
```
Проверьте, что все сервисы запущены:
```bash
docker-compose ps
```
### 4. Сборка и запуск микросервисов

Каждый микросервис имеет собственный каталог с `build.gradle`. Для каждого из них выполните следующие шаги:

#### 4.1. Producer

Переход в каталог Producer:
```bash
cd producer
```
Сборка проекта:
```bash
./gradlew build
```
Запуск приложения:
```bash
./gradlew bootRun
```
Параметры конфигурации:  
Файл `src/main/resources/application.properties` содержит:
```
spring.kafka.bootstrap-servers=localhost:9092
server.port=8080
```
При необходимости измените `server.port` или другие параметры.

#### 4.2. First Consumer

Переход в каталог First Consumer:
```bash
cd ../first-consumer
```
Сборка проекта:
```bash
./gradlew build
```
Запуск приложения:
```bash
./gradlew bootRun
```
Параметры конфигурации:  
Файл `src/main/resources/application.properties` содержит:
```
server.port=8081

# Kafka Config
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.auto-offset-reset=earliest

# JPA Config
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database=postgresql

# Datasource Config
spring.datasource.url=jdbc:postgresql://localhost:5433/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
```
Примечания:

- Убедитесь, что параметры подключения к базе данных совпадают с настройками в `docker-compose.yml`.
- Если вы изменили порты или учётные данные, обновите их здесь.

#### 4.3. Second Consumer

Переход в каталог Second Consumer:
```bash
cd ../second-consumer
```
Сборка проекта:
```bash
./gradlew build
```
Запуск приложения:
```bash
./gradlew bootRun
```
Параметры конфигурации:  
Файл `src/main/resources/application.properties` содержит:
```
server.port=8082

# Kafka Config
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.auto-offset-reset=earliest
```
### 5. Тестирование системы

После запуска всех микросервисов вы можете протестировать систему следующим образом:

1. **Отправка сообщения через Producer**:

   Отправьте POST-запрос на `http://localhost:8080/send` с JSON-телом, соответствующим `UserDto`.

   Пример запроса:
```curl
   curl -X POST \
   http://localhost:8080/send \
   -H 'Content-Type: application/json' \
   -d '{
   "name": "Иван",
   "surname": "Иванов",
   "age": 25,
   "phoneNumber": "+79991234567"
   }'
```
   Ответ:
```
   Message sent: UserDto[name=Иван, surname=Иванов, age=25, phoneNumber=+79991234567]
```
2. **Проверка First Consumer**:

   Проверьте базу данных PostgreSQL, чтобы убедиться, что пользователь был сохранён.

   Подключение к PostgreSQL:
```
   docker exec -it <postgres_container_id> psql -U postgres -d postgres
```
   Запрос:
```sql
   SELECT * FROM user_entity;
```
3. **Проверка Second Consumer**:

   Просмотрите логи Second Consumer для подтверждения обработки сообщения.

   docker-compose logs second-consumer

   Или, если вы запустили сервисы вручную, проверьте вывод терминала, где запущен Second Consumer.

### 6. Настройка параметров

Вы можете изменить различные параметры для тестирования и настройки системы.

#### 6.1. Изменение настроек Kafka

Файл `docker-compose.yml` содержит настройки Kafka. Вы можете изменить:
```
KAFKA_CREATE_TOPICS: "test_topic:1:3"
```
Например, для 2 партиций и 1 реплики:
```
KAFKA_CREATE_TOPICS: "test_topic:2:1"
```
#### 6.2. Изменение настроек PostgreSQL

Файл `first-consumer/src/main/resources/application.properties` содержит параметры подключения к PostgreSQL. При изменении портов, учётных данных или имени базы данных, обновите соответствующие поля:
```
spring.datasource.url=jdbc:postgresql://localhost:5433/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
```
#### 6.3. Изменение портов микросервисов

В каждом микросервисе вы можете изменить порт, изменив `server.port` в `application.properties`.

Пример:
```
server.port=8080
```
#### 6.4. Изменение Group ID для потребителей

В сервисах `first-consumer` и `second-consumer` используются разные `groupId`:

- First Consumer: `group_id`
- Second Consumer: `group_id_2`

При необходимости изменить, обновите аннотацию `@KafkaListener` в соответствующих сервисах.

Пример:
```
@KafkaListener(topics = "test_topic", groupId = "new_group_id")
```
### 7. Остановка сервисов

Чтобы остановить все сервисы, запущенные через Docker Compose:
```bash
docker-compose down
```
Чтобы остановить отдельные микросервисы, просто прервите выполнение команд `bootRun` (Ctrl+C) или завершите соответствующие процессы.

## Дополнительная информация

- **Логирование**: Все микросервисы используют `Slf4j` для логирования. Логи можно просматривать в терминале, где запущен сервис, или перенаправить их в файлы.
- **Spring Boot Actuator**: При необходимости можно добавить Spring Boot Actuator для мониторинга состояния приложений.
- **Расширение функционала**: Вы можете добавить дополнительные микросервисы или расширить существующие, используя преимущества Kafka для межсервисного взаимодействия.

## Заключение

Этот проект демонстрирует базовую архитектуру микросервисов с использованием Apache Kafka для обмена сообщениями. Следуя этим инструкциям, вы сможете развернуть, протестировать и при необходимости модифицировать систему под свои нужды.

Если у вас возникли вопросы или предложения, пожалуйста, создайте issue в репозитории.