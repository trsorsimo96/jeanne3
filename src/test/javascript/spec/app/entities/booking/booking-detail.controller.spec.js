'use strict';

describe('Controller Tests', function() {

    describe('Booking Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBooking, MockPassenger, MockFare, MockEmail, MockSegment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBooking = jasmine.createSpy('MockBooking');
            MockPassenger = jasmine.createSpy('MockPassenger');
            MockFare = jasmine.createSpy('MockFare');
            MockEmail = jasmine.createSpy('MockEmail');
            MockSegment = jasmine.createSpy('MockSegment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Booking': MockBooking,
                'Passenger': MockPassenger,
                'Fare': MockFare,
                'Email': MockEmail,
                'Segment': MockSegment
            };
            createController = function() {
                $injector.get('$controller')("BookingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jeanne3App:bookingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
