# JAVA Подготовка к собеседованиям

## Тестовое задание №2

Тестовое задание выполнено с использованием следующего стека:
- Java11
- Spring Boot
- REST API
- База данных Postgres

SQL скрипты для начальной работы с БД расположены в каталоге sql/

##Реализовано все 6 заданий

### Расположение заданий в проекте:
- задание №1: sql/task1.sql
- задание №2: sql/task2.sql
- задание №3: task3/ - выполнено как отдельный проект
- задания №4-6: task4-6/ - выполнено в одном отдельном проекте

### Запуск проекта-задания 3:
````
$ cd task3
$ mvn package
$ java -jar target/simpleRestApi-1.0.jar
````
Данный проект реализован с помощью Spring Boot. 

Запуск запроса:
````http request
GET /ping
````

Ответ:

````
Cats Service. Version 0.1
````

### Запуск проекта для выполнения заданий 4-6:
````
$ cd task4-6
$ mvn package
$ java -jar target/restfull-1.0.jar
````

Настройки проекта в файле: src/main/resources/application.properties

#### Задание №4 

Реализация GET запросов в контроллере CatsGetController.java
- end-point: /cats
- дополнительные параметры: 
  - attribute - имя поля для сортировки
  - sort - тип сортировки, только `asc`, `desc`
  - page - № страницы (начиная с 0)
  - size - размер страницы (действует проверка на отрицательные значения)

Пример запроса: 
````http request
GET /cats?attribute=name&sort=asc&page=2&size=10
````

Пример ответа:
````json
[
  {
    "name": "Tihon",
    "color": "black",
    "tailLength": 5,
    "whiskersLength": 5
  }
]
````
#### Задание №5

Добавление новых сущностей реализовано в контроллере CatsGetController.java.

- end-point: /cat
- метод: POST

Пример запроса:
````http request
POST /cat
Content-Type: application/json

{
  "name": "Vasya",
  "color": "black & white",
  "tailLength": 5,
  "whiskersLength": 5
}
````

Реализована проверка на валидность входных данных: 

- значение поля `name` должно быть уникально в БД;
- значение поля `color` должно быть в конкретном списке;
- значения поля `tailLength` должно быть в диапазоне: от 1 до 80;
- значения поля `whiskersLength` должно быть в диапазоне: от 1 до 20;

#### Задание №6

Для выполнения данного задания использовалась дополнительная библиотека: `bucket4j`

В данной библиотеке реализован алгоритм `token-bucket`. Bucket4j — это потокобезопасная библиотека.

В `pom.xml` необходимо добавить:

````xml
<dependency>
    <groupId>com.github.vladimir-bukhtoyarov</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>4.10.0</version>
</dependency>
````

В файле `application.properties` добавлены дополнительные строки:
- bucket.capacity=600 - размер корзины
- bucket.interval-minute=1 - время в минутах

При превышении количества запросов `bucket.capacity` в интервале времени `bucket.interval-minute` будет возвращен ответ со статусом `429 Too Many Requests`.

Использование библиотеки `bucket4j`.
В контроллере, в каждом методе (end-point) необходимо добавить:
````java
if (!bucket.tryConsume(1)) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
}
````
