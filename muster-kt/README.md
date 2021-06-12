# muster-kt

## Dev commands
```
./gradlew test
./gradlew run
./gradlew shadowJar
```

This was initially generated with http4k project wizard
https://toolbox.http4k.org/project

Some decisions made so far:
- General specs
  - Prefer functional style
  - Use gradle for build
  - Uber jar packaging
- Libraries
  - Use http4k with ktor
  - Use harmkrest for testing
  - Use jackson for Json :sad_panda:


## End Points

### Ministries

#### Save Ministry

- URI: /v1/ministry
- Method: POST
- Content-Type: application/json
- Sample request:
  - ```
    curl -XPOST -v -H "Content-Type: application/json" \
    -d '{"name":"My Ministry","description":"a very important ministry"}'\
    localhost:8888/v1/ministry
    ```
