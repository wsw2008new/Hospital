'use strict';

describe('Controller Tests', function() {

    describe('Patient Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPatient, MockDoctor, MockExaminations;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPatient = jasmine.createSpy('MockPatient');
            MockDoctor = jasmine.createSpy('MockDoctor');
            MockExaminations = jasmine.createSpy('MockExaminations');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Patient': MockPatient,
                'Doctor': MockDoctor,
                'Examinations': MockExaminations
            };
            createController = function() {
                $injector.get('$controller')("PatientDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hospitalApp:patientUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
