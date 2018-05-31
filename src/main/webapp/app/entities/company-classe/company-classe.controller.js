(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('CompanyClasseController', CompanyClasseController);

    CompanyClasseController.$inject = ['CompanyClasse', 'CompanyClasseSearch'];

    function CompanyClasseController(CompanyClasse, CompanyClasseSearch) {

        var vm = this;

        vm.companyClasses = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            CompanyClasse.query(function(result) {
                vm.companyClasses = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CompanyClasseSearch.query({query: vm.searchQuery}, function(result) {
                vm.companyClasses = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
