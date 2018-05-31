(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('DepositController', DepositController);

    DepositController.$inject = ['Deposit', 'DepositSearch'];

    function DepositController(Deposit, DepositSearch) {

        var vm = this;

        vm.deposits = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Deposit.query(function(result) {
                vm.deposits = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DepositSearch.query({query: vm.searchQuery}, function(result) {
                vm.deposits = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
