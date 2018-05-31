(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('CompanyClasseDetailController', CompanyClasseDetailController);

    CompanyClasseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CompanyClasse', 'Company', 'Classe'];

    function CompanyClasseDetailController($scope, $rootScope, $stateParams, previousState, entity, CompanyClasse, Company, Classe) {
        var vm = this;

        vm.companyClasse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jeanne3App:companyClasseUpdate', function(event, result) {
            vm.companyClasse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
