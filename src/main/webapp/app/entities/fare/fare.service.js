(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Fare', Fare);

    Fare.$inject = ['$resource'];

    function Fare ($resource) {
        var resourceUrl =  'api/fares/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
