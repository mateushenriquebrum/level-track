Web Micro-Framework:
----------------------------------------------------------
| EFFECT |                 PURE                |  EFFECT |
| SERVER -> RESOLVER -> DISPATCHER -> HANDLER -> SERVER  |
----------------------------------------------------------

RESOLVER:
Match a provided URL with a custom pattern, extract the parameters and provide the associated handler, eg:
    handlers = {
        /user/<id>  = user.find(id);
        /user       = user.all();
    }

    /user/12 = user.find(12);
    /user = user.all();

DISPATCHER:
Simple thread pool that is schedule in order to execute the handler, this is the for optimization in order to improve the work load

HANDLER:
Simple function where custom user computation is done.
handler(request) = response
