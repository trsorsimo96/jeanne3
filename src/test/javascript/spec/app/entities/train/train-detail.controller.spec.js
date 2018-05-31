'use strict';

describe('Controller Tests', function() {

    describe('Train Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTrain, MockWagon, MockVoyage, MockCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTrain = jasmine.createSpy('MockTrain');
            MockWagon = jasmine.createSpy('MockWagon');
            MockVoyage = jasmine.createSpy('MockVoyage');
            MockCompany = jasmine.createSpy('MockCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Train': MockTrain,
                'Wagon': MockWagon,
                'Voyage': MockVoyage,
                'Company': MockCompany
            };
            createController = function() {
                $injector.get('$controller')("TrainDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:trainUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
