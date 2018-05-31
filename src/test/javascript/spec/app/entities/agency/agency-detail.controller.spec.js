'use strict';

describe('Controller Tests', function() {

    describe('Agency Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAgency, MockDeposit, MockFeesAgency;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAgency = jasmine.createSpy('MockAgency');
            MockDeposit = jasmine.createSpy('MockDeposit');
            MockFeesAgency = jasmine.createSpy('MockFeesAgency');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Agency': MockAgency,
                'Deposit': MockDeposit,
                'FeesAgency': MockFeesAgency
            };
            createController = function() {
                $injector.get('$controller')("AgencyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:agencyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
