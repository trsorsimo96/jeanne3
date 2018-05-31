(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ClasseDetailController', ClasseDetailController);

    ClasseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Classe', 'Itineraire', 'CompanyClasse'];

    function ClasseDetailController($scope, $rootScope, $stateParams, previousState, entity, Classe, Itineraire, CompanyClasse) {
        var vm = this;

        vm.classe = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:classeUpdate', function(event, result) {
            vm.classe = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
