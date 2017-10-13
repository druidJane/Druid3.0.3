require.config({
    baseUrl: "scripts"
});
require([	
	"services/a-service",
    "app",
    "controllers/index",
    "services/util-service",
    "services/repo-service",
    "services/modal-service",
    "services/graph-service",
    "services/enterprise-service",
    "services/msg-service",
    "services/mms-service",
    "services/st-service",
    "directives/util-directive"	,
    "rsa.min"
], function() {
    angular.bootstrap(document, ["mosApp"]);
});

var global_variable = { "formPosting": false };
$(function($) {
    // console.log(" ~~ Document is ready!!!");
    $("body").keydown(function(e) {
        if ($(".modal").is(':visible')) {
            //弹出层后禁用退格键
            var rx = /input|select|textarea/i;
            if (e.keyCode == 8) {
                if (!rx.test(e.target.tagName) || e.target.disabled || e.target.readOnly) {
                    e.preventDefault();
                }
            }
        }
    });
});
