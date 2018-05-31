'use strict';

describe('Controller Tests', function() {

    describe('CompanyClasse Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCompanyClasse, MockCompany, MockClasse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCompanyClasse = jasmine.createSpy('MockCompanyClasse');
            MockCompany = jasmine.createSpy('MockCompany');
            MockClasse = jasmine.createSpy('MockClasse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CompanyClasse': MockCompanyClasse,
                'Company': MockCompany,
                'Classe': MockClasse
            };
            createController = function() {
                $injector.get('$controller')("CompanyClasseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:companyClasseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
