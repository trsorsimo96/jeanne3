(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('FeesAgencyController', FeesAgencyController);

    FeesAgencyController.$inject = ['FeesAgency', 'FeesAgencySearch'];

    function FeesAgencyController(FeesAgency, FeesAgencySearch) {

        var vm = this;

        vm.feesAgencies = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FeesAgency.query(function(result) {
                vm.feesAgencies = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FeesAgencySearch.query({query: vm.searchQuery}, function(result) {
                vm.feesAgencies = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
