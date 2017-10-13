require.config({
    baseUrl: "scripts",
    paths: {
        "app": "login/app",
        "login": "login/login",
        "util-service": "services/util-service",
		"a-service": "services/a-service",
    }
});
require([
	"a-service",
	"rsa.min",	
    "app",
    "login",
    "util-service"
], function() {
    angular.bootstrap(document, ["loginApp"]);
});

$(function($) {
    // console.log(" ~~ Document is ready!!!");
});
