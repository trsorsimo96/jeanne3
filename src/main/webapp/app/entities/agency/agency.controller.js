(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('AgencyController', AgencyController);

    AgencyController.$inject = ['Agency', 'AgencySearch'];

    function AgencyController(Agency, AgencySearch) {

        var vm = this;

        vm.agencies = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Agency.query(function(result) {
                vm.agencies = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AgencySearch.query({query: vm.searchQuery}, function(result) {
                vm.agencies = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
