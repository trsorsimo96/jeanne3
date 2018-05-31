'use strict';

describe('Controller Tests', function() {

    describe('Company Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCompany, MockCar, MockTrain, MockItineraire, MockCompanyClasse, MockFeesAgency;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCompany = jasmine.createSpy('MockCompany');
            MockCar = jasmine.createSpy('MockCar');
            MockTrain = jasmine.createSpy('MockTrain');
            MockItineraire = jasmine.createSpy('MockItineraire');
            MockCompanyClasse = jasmine.createSpy('MockCompanyClasse');
            MockFeesAgency = jasmine.createSpy('MockFeesAgency');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Company': MockCompany,
                'Car': MockCar,
                'Train': MockTrain,
                'Itineraire': MockItineraire,
                'CompanyClasse': MockCompanyClasse,
                'FeesAgency': MockFeesAgency
            };
            createController = function() {
                $injector.get('$controller')("CompanyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:companyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
