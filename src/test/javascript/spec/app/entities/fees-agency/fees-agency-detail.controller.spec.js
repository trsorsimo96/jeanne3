'use strict';

describe('Controller Tests', function() {

    describe('FeesAgency Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFeesAgency, MockCompany, MockAgency;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFeesAgency = jasmine.createSpy('MockFeesAgency');
            MockCompany = jasmine.createSpy('MockCompany');
            MockAgency = jasmine.createSpy('MockAgency');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FeesAgency': MockFeesAgency,
                'Company': MockCompany,
                'Agency': MockAgency
            };
            createController = function() {
                $injector.get('$controller')("FeesAgencyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:feesAgencyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
