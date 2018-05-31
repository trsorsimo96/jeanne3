'use strict';

describe('Controller Tests', function() {

    describe('Classe Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockClasse, MockItineraire, MockCompanyClasse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockClasse = jasmine.createSpy('MockClasse');
            MockItineraire = jasmine.createSpy('MockItineraire');
            MockCompanyClasse = jasmine.createSpy('MockCompanyClasse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Classe': MockClasse,
                'Itineraire': MockItineraire,
                'CompanyClasse': MockCompanyClasse
            };
            createController = function() {
                $injector.get('$controller')("ClasseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:classeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
