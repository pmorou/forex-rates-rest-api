## Forex Rates REST API
Foreign exchange and currency conversion API for latest and historical rates serving JSON responses.

By default, historical data (since 1999) is provided by European Central Bank and updated every Monday to Friday around 16 CET. 

## How to start
1. Get the code:

    ```
    $ git clone https://github.com/ww421/forex-rates-rest-api.git
    ```
2. Make sure `application.properties` matches your database settings.
3. Run the application using Maven plugin:

    ```
    $ cd forex-rates-rest-api
    $ mvn spring-boot:run
    ```
4. Application will now automatically create needed tables and fill them with data. Note: This might take a few minutes.
5. You can then access it through <http://localhost:8080/>.

## Endpoints

### GET /rates/daily

Parameter | Description | Example | Required
--- | --- | --- | ---
`base` | Base currency for which rates are returned | `USD` | no
`currencies` | Requested currencies separated by comma | `EUR, GBP` | no
`date` | The date in YYYY-MM-DD format for specific data set | `2016-08-30` | no

Example request:
```javascript
GET /rates/daily?base=USD&currencies=EUR,GBP&date=2016-08-30
```

Example response:
```javascript
{
    "date": "2016-08-30",
    "base": "USD",
    "rates": {
        "EUR": 0.92835,
        "GBP": 0.78169
    }
}
```

---

### GET /rates/series

Parameter | Description | Example | Required
--- | --- | --- | ---
`base` | Base currency for which rates are returned | `USD` | no
`currencies` | Requested currencies separated by comma | `EUR, GBP` | no
`startDate` | The date in YYYY-MM-DD format specifying starting point (inclusive) | `2016-08-29` | yes
`endDate` | The date in YYYY-MM-DD format specifying ending point (inclusive) | `2016-08-30` | yes

Example request:
```javascript
GET /rates/series?base=USD&currencies=EUR,GBP&startDate=2016-08-29&endDate=2016-08-30
```

Example response:
```javascript
{
    "startDate": "2016-08-29",
    "endDate": "2016-08-30",
    "base": "USD",
    "rates": {
        "2016-08-29": {
            "EUR": 0.89526,
            "GBP": 0.76523
        },
        "2016-08-30": {
            "EUR": 0.92835,
            "GBP": 0.78169
        }
    }
}
```

---

### GET /exchange/daily

Parameter | Description | Example | Required
--- | --- | --- | ---
`from` | Currency from which amount is exchanged | `USD` | no
`to` | Requested currencies separated by comma | `EUR, GBP` | no
`amount` | Amount of currency for exchange, non negative with dot as decimal point | `100` | no
`date` | The date in YYYY-MM-DD format | `2016-08-30` | no

Example request:
```javascript
GET /exchange/daily?from=USD&to=EUR,GBP&amount=100&date=2016-08-30
```

Example response:
```javascript
{
    "date": "2016-08-30",
    "amount": 100,
    "from": "USD",
    "to": {
        "EUR": 92.84,
        "GBP": 78.17
    }
}
```

