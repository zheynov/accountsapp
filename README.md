### Start the database using docker compose
docker compose -f docker-compose-db-only.yml up -d

### Build, start and stop the database + backend app
docker compose build --no-cache
docker compose up -d
docker compose down


### Кеширование реализовал c использованием caffeine, только в сервис слое. Так как если реализовывать в контроллерах, это наверное нарушит первый принцип SOLID.
### А в репозиториях на некоторых CRUD операциях аннотации нет возможности прописать.

### Ссылка на сваггер после запуска приложения http://localhost:8080/swagger-ui/index.html#/

### Для тестирования API можно испольтзовать готовые запросы из папки http


### Что можно было бы добавить:
### подробнее реализовать обработку исключений, но в рамках тестового задания, предполагаю, в этом нет острой необходимости.
### покрыть всю бизнес-логику логированием.
