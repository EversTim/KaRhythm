describe("A Player", function() {

  var player;
  var keyPress;

  function makeArray(d1, d2) {
    var arr = [];
    for (i = 0; i < d1; i++) {
      arr.push(new Array(d2));
    }
    return arr;
  }

  beforeAll(function() {
    document.addEventListener('keydown', function(e) {
      player.onKeyDown(e)
    });

    keyPress = function(key) {
      var event = document.createEvent('Event');
      event.key = key;
      event.initEvent('keydown');
      document.dispatchEvent(event)
    }
  });

  beforeEach(function() {
    player = new Player();
  });

  afterEach(function() {
    player = undefined;
  });

  it("should exist", function() {
    expect(player).toBeDefined();
  });

  it("should be able to start playing", function() {
    player.startPlaying();
    expect(player.isPlaying()).toBe(true);
  });

  it("should be stopped by default", function() {
    expect(player.isPlaying()).toBe(false);
  });

  it("should be able to stop playing", function() {
    player.startPlaying();
    player.stopPlaying();
    expect(player.isPlaying()).toBe(false);
  });

  it("should start playing on keypress g", function() {
    keyPress('g');
    expect(player.isPlaying()).toBe(true);
  });

  it("should stop playing on keypress Esc", function() {
    keyPress('g');
    keyPress('Escape');
    expect(player.isPlaying()).toBe(false);
  });

  it("should be able to return a list of audio elements on the page", function() {
    expect(player.getAudioElements()).toEqual(document.getElementsByTagName("audio"));
    expect(player.getAudioElements().length).toBe(parseInt(player.getTracks()));
  });

  it("should be able to return the nth audio element on the page", function() {
    var i;
    for (i = 0; i < player.getNumberOfAudioElements(); i++) {
      expect(player.getAudioElement(i)).toBeDefined();
    }
    expect(player.getAudioElement(player.getNumberOfAudioElements())).toBeUndefined();
  });

  it("should play all audio tags on playAllOnce()", function() {
    player.playAllOnce();
    var i;
    for (i = 0; i < player.getNumberOfAudioElements(); i++) {
      expect(player.getAudioElement(i).paused).toBe(false);
    }
  });

  it("should have a length and number of tracks matching the available checkboxes", function() {
    expect(player.getCheckboxes().length).toBe(player.getLength() * player.getTracks());
  });

  // Broken with Scala-generated tests

  /*it("should be able to retrieve a checkbox based on beat and track", function() {
    var boxes = player.getCheckboxes();
    var i, j;
    for (i = 0; i < player.getLength(); i++) {
      for (j = 0; j < player.getTracks(); j++) {
        expect(player.getCheckbox(i, j).checked).toBe(
          ((i + j) % 2 == 0) ? true : false)
      }
    }
  });*/

  it("should be able to retrieve an audio element by tracknumber", function() {
    var elem = player.getAudioElementByTrackNumber("0");
    expect(elem.dataset.tracknumber).toBe("0");
  });

  it("should call callback only on checked boxes when playing loop once", function() {
    var checks = makeArray(player.getLength(), player.getTracks());
    var boxes = document.getElementsByClassName("pccheckbox");
    player.playLoopOnce(function(beat, track, toSet) {
      checks[beat][track] = toSet;
    }, false);
    var i, j;
    for (i = 0; i < player.getLength(); i++) {
      for (j = 0; j < player.getTracks(); j++) {
        expect(player.getCheckbox(i, j).checked).toBe(checks[i][j])
      }
    }
  });
});
