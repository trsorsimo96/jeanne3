(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Day', Day);

    Day.$inject = ['$resource'];

    function Day ($resource) {
        var resourceUrl =  'api/days/:id';

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
