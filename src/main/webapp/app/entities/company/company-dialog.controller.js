(function() {
    'use strict';

    angular
        .module('jeanne3App')
        .controller('CompanyDialogController', CompanyDialogController);

    CompanyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Company', 'Car', 'Train', 'Itineraire', 'CompanyClasse', 'FeesAgency'];

    function CompanyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Company, Car, Train, Itineraire, CompanyClasse, FeesAgency) {
        var vm = this;

        vm.company = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.cars = Car.query();
        vm.trains = Train.query();
        vm.itineraires = Itineraire.query();
        vm.companyclasses = CompanyClasse.query();
        vm.feesagencies = FeesAgency.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.company.id !== null) {
                Company.update(vm.company, onSaveSuccess, onSaveError);
            } else {
                Company.save(vm.company, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jeanne3App:companyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, company) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        company.image = base64Data;
                        company.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
