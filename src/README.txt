Web Micro-Framework:
----------------------------------------------------------
| EFFECT |                 PURE                |  EFFECT |
| TCP/IP -> DISPATCHER -> EXECUTOR -> HANDLER -> TCP/IP  |
----------------------------------------------------------

DISPATCHER:
Match a provided URL with a custom pattern, extract the parameters and provide the associated handler, eg:
    handlers = {
        /user/<id>  = user.find(id);
        /user       = user.all();
    }

    /user/12 = user.find(12);
    /user = user.all();

EXECUTOR:
