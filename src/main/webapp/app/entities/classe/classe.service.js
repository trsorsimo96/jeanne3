(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Classe', Classe);

    Classe.$inject = ['$resource'];

    function Classe ($resource) {
        var resourceUrl =  'api/classes/:id';

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
