var app = angular.module("chatApp", ['ngRoute', 'ngAnimate']);

app.config(function ($routeProvider) {
    $routeProvider
        .when("/home", {
            templateUrl: '/w/home.html',
            controller: 'HomeCtrl'
        })
        .when('/login', {
            templateUrl: '/w/login.html',
            controller: 'LoginCtrl',
        })
        .when("/register", {
            templateUrl: '/w/register.html',
            controller: 'RegisterCtrl',
        })
        .when("/hint/:username/", {
            templateUrl: '/w/hint.html',
            controller: 'HintCtrl',
        })
        .otherwise( {
            redirectTo: '/login',
        });
});



app.controller("HomeCtrl", function($scope) {
    $scope.sload = function ($event) {
        $("#spage").children().each(function (idx) {
            $(this).delay(100).fadeOut(100);
        })
        var nav = $event.currentTarget;
        $("#" + $(nav).attr("data-page")).fadeIn(100);
        $("#snav").children().each(function (idx) {
            $(this).removeClass("active");
        });
        $(nav).addClass("active");
        $event.preventDefault();
    }
});

app.controller("LoginCtrl", function($scope, $http, $location) {
    $scope.login = function () {
        var req = {
            method: "POST",
            url: "/login",
            data: {
                "userid": $scope.userid,
                "passwd": $scope.password,
            },
        };
        $http(req).then(
            function (res) {
                if (res.data['status'] === 'success') {
                    $location.path("/home");
                }
                else {
                    $scope.message = 'Login failed, please retry!';
                }
            },
            function (res) {
                $scope.message = 'Network outage!';
            });
    }
});

app.controller("RegisterCtrl", function($scope, $http, $location) {
    $scope.register = function () {
        if ($scope.password !== $scope.confirmpassword) {
            $scope.message = 'The passwords you entered must be the same!';
        }
        else {
            $http({
                method: "POST",
                url: "/register",
                data: {
                    "nickname": $scope.nickname,
                    "passwd": $scope.password,
                    "email": $scope.email,
                },
            }).then(
                function (res) {
                    if (res.data['status'] === 'success') {
                        $location.path("/hint/" + res.data['username']);
                    }
                    else {
                        $scope.message = 'Register failed, please retry!';
                    }
                },
                function (res) {
                    $scope.message = 'Network outage!';
                });
        }
    }
});

app.controller("HintCtrl", function($scope, $routeParams, $location) {
    $scope.username = $routeParams.username;
    // Other actions.
});



