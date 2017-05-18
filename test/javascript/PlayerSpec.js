describe("A Player", function() {

  var player;
  var keyPress;
  var length = Math.floor(Math.random() * 10) + 2;
  var tracks = Math.floor(Math.random() * 10) + 2;

  function makeArray(d1, d2) {
    var arr = [];
    for (i = 0; i < d1; i++) {
      arr.push(new Array(d2));
    }
    return arr;
  }

  beforeAll(function() {
    var container = document.createElement("div");
    var audio;
    var checkbox;
    var outerDiv;
    var checkboxDiv;
    var i, j;
    for (i = 0; i < tracks; i++) {
      outerDiv = document.createElement("div");
      outerDiv.id = i;
      for (j = 0; j < length; j++) {
        checkboxDiv = document.createElement("div");
        checkboxDiv.id = i;
        checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.className = "pccheckbox";
        checkbox.dataset.beat = j;
        checkbox.dataset.tracknumber = i;
        if ((i + j) % 2 == 0) {
          checkbox.checked = true;
        }
        checkboxDiv.append(checkbox);
        outerDiv.append(checkboxDiv);
      }
      container.append(outerDiv);
    }

    for (i = 0; i < tracks; i++) {
      audio = document.createElement("audio");
      audio.dataset.tracknumber = i;
      container.append(audio);
    }

    container.style.visibility = "hidden";

    document.body.append(container);

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
    player.setLengthAndTracksAndFindCheckboxes(length, tracks);
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
    expect(player.getAudioElements().length).toBe(tracks);
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
    expect(player.getCheckboxes().length).toBe(length * tracks);
  });

  it("should be able to retrieve a checkbox based on beat and track", function() {
    var boxes = player.getCheckboxes();
    var i, j;
    for (i = 0; i < length; i++) {
      for (j = 0; j < tracks; j++) {
        expect(player.getCheckbox(i, j).checked).toBe(
          ((i + j) % 2 == 0) ? true : false)
      }
    }
  });

  it("should be able to retrieve an audio element by tracknumber", function() {
    var elem = player.getAudioElementByTrackNumber("0");
    expect(elem.dataset.tracknumber).toBe("0");
  });

  it("should call callback only on checked boxes when playing loop once", function() {
    var checks = makeArray(length, tracks);
    var boxes = document.getElementsByClassName("pccheckbox");
    player.playLoopOnce(function(beat, track, toSet) {
      checks[beat][track] = toSet;
    }, false);
    var i, j;
    for (i = 0; i < length; i++) {
      for (j = 0; j < tracks; j++) {
        expect(player.getCheckbox(i, j).checked).toBe(checks[i][j])
      }
    }
  });
});
