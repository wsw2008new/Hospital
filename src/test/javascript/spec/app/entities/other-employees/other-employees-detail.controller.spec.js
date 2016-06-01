'use strict';

describe('Controller Tests', function() {

    describe('OtherEmployees Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOtherEmployees;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOtherEmployees = jasmine.createSpy('MockOtherEmployees');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OtherEmployees': MockOtherEmployees
            };
            createController = function() {
                $injector.get('$controller')("OtherEmployeesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hospitalApp:otherEmployeesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
