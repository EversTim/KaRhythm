describe("A pattern", function() {
  var pattern = undefined;

  beforeEach(function() {
    pattern = new Pattern;
  });

  afterEach(function() {
    pattern = undefined;
  });

  it("should exist", function() {
    expect(pattern).toBeDefined();
  });

  it("should be playable for a given beat", function() {
    var tracks = pattern.getTracks();
    var length = pattern.getLength();
    var i, j;
    for (i = 0; i < length; i++) {
      var toPlay = pattern.play(i);
      for (j = 0; j < tracks; j++) {
        if(pattern.getSingle(j, i)) {
          expect(toPlay.indexOf(j)).not.toBe(-1);
        }
      }
    }
  });
});
