'use strict';

describe('Controller Tests', function() {

    describe('Voyage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVoyage, MockSegment, MockRoutes, MockCar, MockTrain, MockItineraire;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVoyage = jasmine.createSpy('MockVoyage');
            MockSegment = jasmine.createSpy('MockSegment');
            MockRoutes = jasmine.createSpy('MockRoutes');
            MockCar = jasmine.createSpy('MockCar');
            MockTrain = jasmine.createSpy('MockTrain');
            MockItineraire = jasmine.createSpy('MockItineraire');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Voyage': MockVoyage,
                'Segment': MockSegment,
                'Routes': MockRoutes,
                'Car': MockCar,
                'Train': MockTrain,
                'Itineraire': MockItineraire
            };
            createController = function() {
                $injector.get('$controller')("VoyageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:voyageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
