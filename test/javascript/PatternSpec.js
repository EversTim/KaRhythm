describe("A pattern", function() {
  var pattern = undefined;

  beforeEach(function(){
    pattern = new Pattern;
  });

  afterEach(function() {
    pattern = undefined;
  });

  it("should exist", function() {
    expect(pattern).toBeDefined();
  });
});
