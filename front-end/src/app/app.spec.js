describe("app", function () {

  beforeEach(module('app'));


  describe('accountService', function () {

    var _logoutService, _requestService;

    beforeEach(function () {
      _requestService = {sendRequest: jasmine.createSpy()};

      module(function ($provide) {
        $provide.value('requestService', _requestService);
      });

      inject(function ($injector) {
        _logoutService = $injector.get('accountService');
      });

    });


    it('should create account', function () {
      var account = {dummy: 'dummy'};

      _logoutService.create(account);

      expect(_requestService.sendRequest).toHaveBeenCalledWith("POST", '/api/v1/accounts', account);
    });

    it('should find all accounts', function () {
      var account = {dummy: 'dummy'};

      _logoutService.findAll();

      expect(_requestService.sendRequest).toHaveBeenCalledWith("GET", '/api/v1/accounts/findAll');
    });

    it('should remove account', function () {
      _logoutService.remove("acc");

      expect(_requestService.sendRequest).toHaveBeenCalledWith("DELETE", '/api/v1/accounts/delete/acc');
    });
  });
});