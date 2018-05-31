'use strict';

describe('Controller Tests', function() {

    describe('Routes Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRoutes, MockItineraire, MockVoyage, MockCity;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRoutes = jasmine.createSpy('MockRoutes');
            MockItineraire = jasmine.createSpy('MockItineraire');
            MockVoyage = jasmine.createSpy('MockVoyage');
            MockCity = jasmine.createSpy('MockCity');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Routes': MockRoutes,
                'Itineraire': MockItineraire,
                'Voyage': MockVoyage,
                'City': MockCity
            };
            createController = function() {
                $injector.get('$controller')("RoutesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:routesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
