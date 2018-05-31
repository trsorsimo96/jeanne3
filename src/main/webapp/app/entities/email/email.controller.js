(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('EmailController', EmailController);

    EmailController.$inject = ['Email', 'EmailSearch'];

    function EmailController(Email, EmailSearch) {

        var vm = this;

        vm.emails = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Email.query(function(result) {
                vm.emails = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EmailSearch.query({query: vm.searchQuery}, function(result) {
                vm.emails = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
