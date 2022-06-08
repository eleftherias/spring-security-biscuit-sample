**Note**: This is not intended for use in production.

# Spring Security + Biscuit example
This sample uses [Spring Security](https://spring.io/projects/spring-security) with [Biscuit](https://www.biscuitsec.org/).

This allows a user locally authenticates with a username and password and then use a Biscuit as a bearer token.

This sample is modeled after the [JWT login sample](https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/jwt/login).

### Usage
To start the application run:
```
./gradlew bootRun
```

You can exchange the username and password for a Biscuit token by calling the token endpoint:
```
export TOKEN=`curl -XPOST user:password@localhost:8080/token`
```

You can then use the token in subsequent requests:

```
curl -H "Authorization: Bearer $TOKEN" localhost:8080 && echo
```

The expected response is:
```
Hello, "user"!
```



