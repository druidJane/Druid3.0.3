define(["app"], function(app) {
    var injectParams =  ['$rootScope','$scope', '$location', 'NgTableParams', 'repoService', 'utilService'];
    var documentIndexCtrl = function ($rootScope, $scope, $location, NgTableParams, repo, util) {
         $scope.downloadUrl = xpath.service("common/download/?path=/doc/");
    }

    documentIndexCtrl.$inject = injectParams;
    app.register.controller('documentIndexCtrl', documentIndexCtrl);
});
