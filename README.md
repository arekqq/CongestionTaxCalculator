# Congestion Tax Calculator

Welcome the Volvo Cars Congestion Tax Calculator assignment.

## Requirements

For build and runn the app you need:

- [OpenJDK 21](https://openjdk.org/projects/jdk/21/)

## Running the application locally

```shell
./gradlew bootRun
```

## Test the application locally using curl
### Query params:
Possible values for `vehicle`:
```
    REGULAR
    MOTORCYCLE
    BUS
    DIPLOMAT
    EMERGENCY
    FOREIGN
    MILITARY
```
`dates` is an array in the ISO 8601 format `yyyy-MM-dd'T'HH:mm:ss`
```shell
curl -X GET "http://localhost:8080/tax/congestion?vehicle=REGULAR&dates=2013-01-14T21:00:00,2013-01-15T11:00:00,2013-01-15T12:00:00"
```

## Sample response

```shell
{
    "tax": 60
}
```
