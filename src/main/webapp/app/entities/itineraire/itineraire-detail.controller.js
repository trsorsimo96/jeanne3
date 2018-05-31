(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ItineraireDetailController', ItineraireDetailController);

    ItineraireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Itineraire', 'Voyage', 'Routes', 'Classe', 'Company', 'Day'];

    function ItineraireDetailController($scope, $rootScope, $stateParams, previousState, entity, Itineraire, Voyage, Routes, Classe, Company, Day) {
        var vm = this;

        vm.itineraire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:itineraireUpdate', function(event, result) {
            vm.itineraire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
