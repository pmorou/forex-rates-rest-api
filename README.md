## Forex Rates REST API
Foreign exchange and currency conversion API for latest and historical rates serving JSON responses.

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
    "timestamp": 1472542619,
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
    "timestamp": 1472549572,
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
