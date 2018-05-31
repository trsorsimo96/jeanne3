(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Month', Month);

    Month.$inject = ['$resource'];

    function Month ($resource) {
        var resourceUrl =  'api/months/:id';

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
