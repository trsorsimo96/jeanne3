(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('FeesAgency', FeesAgency);

    FeesAgency.$inject = ['$resource'];

    function FeesAgency ($resource) {
        var resourceUrl =  'api/fees-agencies/:id';

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
