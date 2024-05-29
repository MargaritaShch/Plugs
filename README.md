# Проект тестирования заглушки

## Обзор проекта

Этот проект включает разработку заглушки, которая обрабатывает определенные запросы от псевдо-тестируемой системы.

### Реализованные конечные точки

1. **GET** `localhost:8080/app/v1/getRequest?id={id}&name={name}`: Возвращает ответ из `getAnswer.txt`, если `id > 10` и `name.length() > 5`. В противном случае возвращает `InternalServerError` с указанием причины.
2. **POST** `localhost:8080/app/v1/postRequest`: Принимает JSON тело с `name`, `surname` и `age`. Возвращает ответ из `postAnswer.txt`. Если `name` или `surname` пусты, возвращает `InternalServerError`. `age` может быть пустым и по умолчанию равен 123.

### Особенности

- **Управление транзакциями**: Транзакции установлены для всех запросов и бизнес-операций для точного измерения производительности.
- **Проверка**: Реализованы проверки для каждой транзакции для обеспечения корректности запросов.
- **Корреляция и параметризация**: Коррелируются динамические значения, такие как ID сессий и рейсов, а пользовательские значения параметризируются.
- **Подготовка данных пользователя**: Данные для 10 уникальных учетных записей пользователей подготовлены и сохранены в файле, что обеспечивает уникальные сеансы для тестирования.

## Тестирование
1. Сборка проекта:
   ```
   mvn clean install
   ```
2.  Запуск приложения:
     ```
    java -jar target/stub-0.0.1-SNAPSHOT.jar
     ```
## Шаги тестирования
### GET запрос

1. Корректный запрос:
   
   ```
    $uri = "http://localhost:8080/app/v1/getRequest?id=15&name=ExampleName"
    Invoke-WebRequest -Uri $uri -Method GET
  
3. Некорректный ID:
   
   ```
    $uri = "http://localhost:8080/app/v1/getRequest?id=5&name=ExampleName"
    Invoke-WebRequest -Uri $uri -Method GET
   ```
Ожидаемый ответ:

  ```
  Internal Server Error: Invalid id or name length
  ```

3. Некорректная длина имени:
   
   ```
    $uri = "http://localhost:8080/app/v1/getRequest?id=15&name=Name"
    Invoke-WebRequest -Uri $uri -Method GET
   ```
Ожидаемый ответ:
   ```
   Internal Server Error: Invalid id or name length
   ```
### POST запрос

1. Корректный запрос:
   
   ```
    $uri = "http://localhost:8080/app/v1/postRequest"
    $body = @{
      name = "John"
      surname = "Doe"
      age = 25
    }
    Invoke-WebRequest -Uri $uri -Method POST -Body ($body | ConvertTo-Json) -ContentType "application/json"

Ожидаемый ответ:

    {
        "Person1": {
            "name": "John",
            "surname": "Doe",
            "age": 25
        },
        "Person2": {
            "name": "Doe",
            "surname": "John",
            "age": 50
        }
    }

2. Отсутствует имя:
   
   ```
   $uri = "http://localhost:8080/app/v1/postRequest"
    $body = @{
        name = ""
        surname = "Doe"
        age = 25
    }
    Invoke-WebRequest -Uri $uri -Method POST -Body ($body | ConvertTo-Json) -ContentType "application/json"
    ```

   Ожидаемый ответ:
   
   ```
   Internal Server Error: Invalid id or name length
   ```
