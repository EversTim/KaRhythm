'use strict';

function Player() {
  var _isPlaying = false;
  var _isMuted = false;

  var _keyboardBeat = -1;

  var _checkboxes = document.getElementsByClassName("pccheckbox")

  var _pattern = new Pattern();

  var _audioElements = document.getElementsByTagName("audio");
  var _numberOfAudioElements = _audioElements.length;

  if (_checkboxes.length != _pattern.getLength() * _pattern.getTracks()) {
    throw 'LengthTrackBoxCountMismatch';
  }

  function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  function resetAudioElement(elem) {
    elem.pause();
    elem.currentTime = 0;
  }

  function resetAndPlay(elem) {
    resetAudioElement(elem);
    elem.play();
  }

  async function loop() {
    while (_isPlaying) {
      var beat;
      for (beat = 0; beat < _pattern.getLength(); beat++) {
        if (!_isPlaying) {
          break;
        }
        _keyboardBeat = beat;
        setCurrentBeatCSS(beat);
        var tracksToPlay = _pattern.play(beat);
        var j;
        console.log("tracksToPlay " + tracksToPlay);
        console.log("tracksToPlay.length " + tracksToPlay.length)
        for (j = 0; j < tracksToPlay.length; j++) {
          console.log("j " + j);
          console.log("tracksToPlay[j] " + tracksToPlay[j]);
          resetAndPlay(audioElementsByTrackNumber(tracksToPlay[j]));
        }
        await sleep(500);
        removeCurrentBeatCSS(beat);
      }
    }
  }

  function setCurrentBeatCSS(beat) {
    updateCurrentBeatCSS(beat, function(container) {
      container.classList.remove("inactive");
      container.classList.add("active");
    });
  }

  function removeCurrentBeatCSS(beat) {
    updateCurrentBeatCSS(beat, function(container) {
      container.classList.remove("active");
      container.classList.add("inactive");
    });
  }

  function updateCurrentBeatCSS(beat, updateFunction) {
    var containers = document.getElementsByClassName("pccheckboxcontainer");
    var i;
    for (i = 0; i < containers.length; i++) {
      if (parseInt(containers[i].dataset.beat) === beat) {
        updateFunction(containers[i]);
      }
    }
  }

  function audioElementsByTrackNumber(track) {
    var i;
    for (i = 0; i < _pattern.getTracks(); i++) {
      var elem = _audioElements[i];
      if (parseInt(elem.dataset.tracknumber) === track) {
        return elem;
      }
    }
    console.log(track);
    throw 'AudioElementForTrackNotFoundException';
  }

  function isNumber(key) {
    var nb = parseInt(key);
    if (isNaN(nb)) {
      return false;
    } else {
      return true;
    }
  }

  function toggleBox(track) {
    Utilities.getCheckbox(track, _keyboardBeat).click(); // click to trigger events
  }

  return {
    togglePlaying: function(testBool = false) {
      if (!_isPlaying) {
        this.startPlaying(testBool);
      } else {
        this.stopPlaying();
      }
    },
    startPlaying: function(testBool = false) {
      if (!_isPlaying) {
        _isPlaying = true;
        if (!testBool) {
          loop();
        }
      }
    },
    stopPlaying: function() {
      _isPlaying = false;
      var i;
      for (i = 0; i < _audioElements.length; i++) {
        resetAudioElement(_audioElements[i]);
      }
      _keyboardBeat = -1;
    },
    isPlaying: function() {
      return _isPlaying;
    },
    onKeyDown: function(e, testBool = false) {
      var key = e.key;
      if (key === "g" || key === "p") {
        this.togglePlaying(testBool);
      } else if (key === "Escape") {
        this.stopPlaying();
      } else if (key === "m") {
        this.toggleMute();
      } else if (isNumber(key)) {
        var intkey = parseInt(key);
        if (intkey < _pattern.getTracks()) {
          toggleBox(parseInt(key));
        }
      }
    },
    getAudioElements: function() {
      return _audioElements;
    },
    getAudioElement: function(i) {
      return this.getAudioElements()[i];
    },
    getNumberOfAudioElements: function() {
      return _numberOfAudioElements;
    },
    playAllOnce: function() {
      var i;
      for (i = 0; i < _pattern.getTracks(); i++) {
        this.getAudioElement(i).play();
      }
    },
    getCheckboxes: function() {
      return _checkboxes;
    },
    getAudioElementByTrackNumber: function(track) {
      return audioElementsByTrackNumber(track);
    },
    getPattern: function() {
      return _pattern;
    },
    isMuted: function() {
      return _isMuted;
    },
    toggleMute: function() {
      var i;
      for (i = 0; i < _audioElements.length; i++) {
        _audioElements[i].muted = !_audioElements[i].muted;
      }
      _isMuted = !_isMuted;
    }
  }
};
