(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Routes', Routes);

    Routes.$inject = ['$resource'];

    function Routes ($resource) {
        var resourceUrl =  'api/routes/:id';

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
