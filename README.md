
### Prerequisite
Docker Desktop

### Test in Local
docker compose up -d

```
GET: http://localhost:8080/api/v1/employees
```


```
POST: http://localhost:8080/api/v1/employees

{
    "firstName" : “Test”,
    "lastName" : “User”,
    "email" : “testemail@example.app"
}
```