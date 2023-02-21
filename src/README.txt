Web Micro-Framework:
--------------------------------------------------------
| EFFECT |               PURE                |  EFFECT |
| SERVER -> ROUTER -> DISPATCHER -> HANDLER -> SERVER  |
--------------------------------------------------------

ROUTER:
Match a provided URL and METHOD with a custom pattern, extract the parameters and provide the associated handler.
DISPATCHER:
Simple thread pool that is schedule in order to execute the handler, this is the for optimization in order to improve the work load
HANDLER:
Simple function where custom user request computation is done.


How to run:
> sdk install java 19.0.2-zulu
> gradle --version
< Gradle 7.6
> gradle start