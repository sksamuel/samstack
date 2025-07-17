# samstack

This is a template project you can clone and use as a basis for your own Kotlin based microservices.

This application structure is my personal preference. I do not claim this is the _best_ approach (whatever
that means), but simply that it works for me and has seen success at several companies I have worked at. This is an
**opinionated project** and as such will not appeal to all.

I am a _mild_ functional programming advocate, and my viewpoints are influenced by functional programming,
but try to avoid the more advanced FP features that don't translate well to Kotlin.

## Opinions

* No dependency injection. You simply do not need it.
   * In Kotlin, it is trivial to pass dependencies through the constructor. Services should be small enough that you don't have
     a dozen layers of serviceX calling serviceY calling serviceZ, where passing dependencies becomes a layer cake.
   * When you use constructors as they were intended, you never have to try and work out where a "bean" is being
     instantiated.
   * You'll get a compile error if anything is not wired up. As it should be.
* Config as data classes.
   * Easy to test because you can simply create an instance of the data class with whatever values you want to use in a test and pass that into the class.
   * No confusion as to where values are being pulled from.
   * Startup time resolution on mapping config values to strongly typed values.
* Tests should be real tests
   * No mocks. Ever. You don't need them (caveat - ok you might need them in some tiny edge cases, like testing a legacy
     Java interface with 200 methods when you don't want to create an implementation).
   * Test your endpoints by using a framework that treats requests as objects.
   * Use [test containers](https://testcontainers.com/) to test against real infrastructure like postgres, kafka, redis, etc. Then you know your SQL will work instead of mocking it out.
   * Use embedded HTTP servers for upstream dependencies.
* Functional error handling
   * Don't throw exceptions unless it's truly exceptional. Expected errors, such as invalid json, are not exceptional.
   * Use Kotlin's [Result](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-result/) structure to capture errors.
* Avoid interfaces for services and datastores
   * You don't need an interface for your database code. You're not going to need to mock them, and the logic is
     bespoke.
   * A good rule of thumb in my opinion is, if you have an implementation of an interface called `MyInterfaceImpl` which indicates
     you don't have a natural name for the implementation, then the interface is adding overhead for no benefit. Contrast this with
     the List interface which has natural implementations such as array list and linked list.
* Avoid ORMs
   * Unless your app is entirely CRUD, it's easier to just write SQL by hand as you can do even moderately complex queries without needing a complex SQL DSL.
   * It's much easier to grab a query from your codebase to test in a database when it's an .sql file than to figure out what a DSL like Jooq is generating for you.
   * Spring JDBC Template is a simple set of helpers around JDBC calls that will handle injection projection and transaction support and mapping results back to classes without needing to jump through hoops.
* Don't use Strings for all your types
   * Avoid so called stringy-typed development.
* Use value objects for extra type safety
   * You can create `data class Height(val value: Int)` and pass that about.
   * Yes it's another object to be instantiated. No you're never going to see a performance impact because your webapp is spending all its time in IO anyway.

## Libraries

* Kotlin - Running the latest Kotlin release.
* [Vert.x](https://vertx.io/) a lightning fast HTTP framework that routinely tops the Java performance metrics at TechEmpower. Offers coroutine support for Kotlin integration. Is simple to use and easy to create some scaffolding around to test locally.
* [Hoplite](https://github.com/sksamuel/hoplite) a Kotlin data-class-as-config loader that provides cascading
  fallback of config files. Config is loaded in regular data classes that you define and can pass about. This allows for
  easy testing, as you can provide test-time values by simply creating instances of those data classes.
* [Kotest](https://github.com/kotest/kotest) Kotlin test framework that supports nested test layout, coroutines,
  idiomatic Kotlin assertions, test-container extensions, property testing, data driven testing, non-deterministic
  helpers, and more.
* [Micrometer](https://micrometer.io) Metrics collection with integration with datadog / grafana, etc and most Java
  libraries come with Micrometer adapters to collect metrics.
  many/most Java libraries.
* [Hikari](https://github.com/brettwooldridge/HikariCP) High performance and robust JDBC pooling library. The go-to
  JVM based connection pool library for several years.
* [Tabby](https://github.com/sksamuel/tabby) - a tiny functional programming accessory kit. Essentially a set of
  extension functions that helps fill in the gaps on Result.
* [Kotlin Logging](https://github.com/MicroUtils/kotlin-logging) - a simple project that wraps slf4j but makes it easier
  to define a kotlin logger as a top level function.
* [Logback](https://logback.qos.ch/) - a simple alternative to log4j.
* [Cohort](https://github.com/sksamuel/cohort) A spring-actuator style plugin for Ktor. Useful for probes for
  kubernetes services.
* [Aedile](https://github.com/sksamuel/aedile) A Kotlin wrapper for Caffeine that provides coroutine support. Caffeine has been the goto Java cache for many years.
* [Tribune](https://github.com/sksamuel/tribune) A validation library for Kotlin that takes the "parse don't
  validate" approach.
* [TestContainers](https://www.testcontainers.org) Creates disposable containers for tests, so you don't need to mock
  database code, because you can test against the real thing.
* [Flyway](https://flywaydb.org/) Versioned database migrations, that you can also apply to your tests.
* [Spring JDBC Template](https://docs.spring.io/spring-framework/docs/current/javadoc-api/index.html?org/springframework/jdbc/core/JdbcTemplate.html)
  Small set of helpers for working with JDBC calls.

## Structure

* `App` is your 'god' object. It contains config parsed by Hoplite, a Micrometer registry backed by datadog, a
  database `java.sql.DataSource`. Any dependencies you need to share should be created here.
   * The micrometer registry comes preconfigured with JVM metrics such as memory, cpu and diskspace.
   * The datasource is backed by a Hikari connection pool. Configure defaults in the database.yml files.
* `main` - This is the starter function that sets some defaults and launches ktor
* `module` - this contains a single logical grouping of ktor endpoints and plugins.

## Modules

* template-domain - place your domain classes here so they are shared between datastores and services
* template-datastore - place your database or cache repositories here
* template-services - place your business logic and endpoints here
* template-client - create a reusable client here for internal services to use
* template-app - an assembly module that builds a docker image for deployment. Config files and logging configuration
  lives here.

## Config

We define a base file, called application.yml which contains defaults that don't change between environments. For
example, port numbers or cache ttls.

Then we have a separate file per environment in the format `application-ENV.yml` eg `application-staging.yml`

This app uses the powerful data-class-as-config approach from Hoplite. Files are loaded in a cascading fashion - earlier
files (as defined in App) override values in later files. The file are converted to data classes and any missing values
or conversion errors are immediately flagged on startup.

AWS secrets manager support is enabled by adding a config key in the form `secretsmanager//:mykey` which you can see
in `application-prod.yml` for example.

## Running

Startup docker `docker-compose up` from the root of this project. This will start up a mysql database.

Run the `main` method located in the app module. This starts up a HTTP interface on the port defined
in `application.yml`. This ktor based http server is configured to return JSON
using [Jackson](https://github.com/FasterXML/jackson).

Three endpoints are included:

* [/health](http://0.0.0.0:10800/health) returns a 200 OK suitable for use in k8
* [/random](http://0.0.0.0:10800/random) returns a random `Wibble` instance
* [/database](http://0.0.0.0:10800/database) returns all _wibbles_ from the database (you need to create the wibble
  table and add your own wibble!)

```sql
CREATE TABLE wibble (
   a TEXT,
   b INT
);

INSERT INTO wibble (a, b)
VALUES ("foo", 1),
       ("bar", 2 ");
```

## Deployments

Executing `./gradlew dockerBuildImage` will build a docker image ready for deployment.
