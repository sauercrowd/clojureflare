# clojureworker

[![Clojars Project](https://img.shields.io/clojars/v/com.github.sauercrowd/clojureworker.svg)](https://clojars.org/com.github.sauercrowd/clojureworker)

A Clojurescript library to simplify the use with Cloudflare workers, focusing on simplicity.
## Usage

Clojureflare currently provides three functions, documented [here](https://cljdoc.org/d/com.github.sauercrowd/clojureworker/0.0.1/api/clojureworker.core).

- `worker`, which routes as arguments and attaches the dispatcher to the worker event
- `simulate-worker`, serving the same purpose. It acceps a request as a first argument and instead of responding to a worker event returns the response, which simplifies repl development. It avoids any dependencies that would limit the runtime to the browser.
- `route`, creating a new route which expects three argument: `method` (e.g. GET, POST), `path` (such as /api/v1/ping) and handler (described below).

### The Handler

The handler can currently be one of three types:
- a string, which will generate a static HTML response
- a map, which will generate a static JSON response
- a function, which will receive the request as a parameter and returns a response map

The request is of the form of

```
{:path "/test" :body "{\"key\":1}" :headers {}}
```

while the response map should similar to

```
{:body "{\"key\":1}" :headers {"Content-Type" "application/json"} :status 200}
```

Additionally the handler function can return a promise which eventually resolves to the described map.

### Example Worker

Head over to the [example-worker](/example-worker) to create your first Clojurescript worker.
