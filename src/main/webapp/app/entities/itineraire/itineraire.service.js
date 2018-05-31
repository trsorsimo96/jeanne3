(function() {
    'use strict';
    angular
        .module('jeanne3App')
        .factory('Itineraire', Itineraire);

    Itineraire.$inject = ['$resource', 'DateUtils'];

    function Itineraire ($resource, DateUtils) {
        var resourceUrl =  'api/itineraires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDepart = DateUtils.convertDateTimeFromServer(data.dateDepart);
                        data.dateArrive = DateUtils.convertDateTimeFromServer(data.dateArrive);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
