'use strict';

describe('Controller Tests', function() {

    describe('Wagon Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWagon, MockModelTrain, MockTrain;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWagon = jasmine.createSpy('MockWagon');
            MockModelTrain = jasmine.createSpy('MockModelTrain');
            MockTrain = jasmine.createSpy('MockTrain');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Wagon': MockWagon,
                'ModelTrain': MockModelTrain,
                'Train': MockTrain
            };
            createController = function() {
                $injector.get('$controller')("WagonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:wagonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
