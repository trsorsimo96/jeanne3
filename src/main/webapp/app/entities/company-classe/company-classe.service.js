(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('CompanyClasse', CompanyClasse);

    CompanyClasse.$inject = ['$resource'];

    function CompanyClasse ($resource) {
        var resourceUrl =  'api/company-classes/:id';

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
