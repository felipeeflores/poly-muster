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
