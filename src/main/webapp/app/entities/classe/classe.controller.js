(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('ClasseController', ClasseController);

    ClasseController.$inject = ['Classe', 'ClasseSearch'];

    function ClasseController(Classe, ClasseSearch) {

        var vm = this;

        vm.classes = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Classe.query(function(result) {
                vm.classes = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ClasseSearch.query({query: vm.searchQuery}, function(result) {
                vm.classes = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
