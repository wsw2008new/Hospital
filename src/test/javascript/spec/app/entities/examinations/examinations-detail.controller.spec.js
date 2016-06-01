'use strict';

describe('Controller Tests', function() {

    describe('Examinations Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockExaminations, MockDoctor, MockPatient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockExaminations = jasmine.createSpy('MockExaminations');
            MockDoctor = jasmine.createSpy('MockDoctor');
            MockPatient = jasmine.createSpy('MockPatient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Examinations': MockExaminations,
                'Doctor': MockDoctor,
                'Patient': MockPatient
            };
            createController = function() {
                $injector.get('$controller')("ExaminationsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hospitalApp:examinationsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
