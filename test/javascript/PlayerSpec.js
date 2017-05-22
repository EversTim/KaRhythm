describe("A Player", function() {

  var player;
  var keyPress;

  beforeAll(function() {
    document.addEventListener('keydown', function(e) {
      player.onKeyDown(e, true)
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
    player.startPlaying(true);
    expect(player.isPlaying()).toBe(true);
  });

  it("should be stopped by default", function() {
    expect(player.isPlaying()).toBe(false);
  });

  it("should be able to stop playing", function() {
    player.startPlaying(true);
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
    expect(player.getAudioElements().length).toBe(parseInt(player.getPattern().getTracks()));
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

  it("should have audio elements for at least all tracks from 0 to number-1", function() {
    expect(player.getNumberOfAudioElements() >= player.getPattern().getTracks()).toBe(true);
    var i;
    for (i = 0; i < player.getPattern().getTracks(); i++) {
      expect(player.getAudioElementByTrackNumber(i)).toBeDefined();
    }
  })

  it("should have a pattern with length and number of tracks matching the available checkboxes", function() {
    expect(player.getCheckboxes().length).toBe(player.getPattern().getLength() * player.getPattern().getTracks());
  });

  it("should be able to retrieve an audio element by tracknumber", function() {
    var elem = player.getAudioElementByTrackNumber(0);
    expect(elem.dataset.tracknumber).toBe("0");
  });

  it("should have a pattern", function() {
    expect(player.getPattern()).toBeDefined();
  });

  it("should be unmuted by default", function() {
    expect(player.isMuted()).toBe(false);
  });

  it("should mute if not muted and unmute if muted", function() {
    player.toggleMute();
    expect(player.isMuted()).toBe(true);
    player.toggleMute();
    expect(player.isMuted()).toBe(false);
  });

  it("should toggle mute on keypress m", function() {
    keyPress("m");
    expect(player.isMuted()).toBe(true);
  })
});
