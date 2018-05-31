'use strict';

describe('Controller Tests', function() {

    describe('Itineraire Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockItineraire, MockVoyage, MockRoutes, MockClasse, MockCompany, MockDay;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockItineraire = jasmine.createSpy('MockItineraire');
            MockVoyage = jasmine.createSpy('MockVoyage');
            MockRoutes = jasmine.createSpy('MockRoutes');
            MockClasse = jasmine.createSpy('MockClasse');
            MockCompany = jasmine.createSpy('MockCompany');
            MockDay = jasmine.createSpy('MockDay');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Itineraire': MockItineraire,
                'Voyage': MockVoyage,
                'Routes': MockRoutes,
                'Classe': MockClasse,
                'Company': MockCompany,
                'Day': MockDay
            };
            createController = function() {
                $injector.get('$controller')("ItineraireDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:itineraireUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
