'use strict';

function Player() {
  var _isPlaying = false;
  var _isMuted = false;

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
        setCurrentBeatCSS(beat);
        var tracksToPlay = _pattern.play(beat);
        var j;
        console.log("tracksToPlay " + tracksToPlay);
        console.log("tracksToPlay.length " + tracksToPlay.length)
        for (j = 0; j < tracksToPlay.length; j++) {
          console.log("j " + j);
          console.log("tracksToPlay[j] " + tracksToPlay[j]);
          resetAndPlay(_audioElementsByTrackNumber(tracksToPlay[j]));
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

  function _audioElementsByTrackNumber(track) {
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

  return {
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
    },
    isPlaying: function() {
      return _isPlaying;
    },
    onKeyDown: function(e, testBool = false) {
      var key = e.key;
      if (key === "g") {
        this.startPlaying(testBool);
      } else if (key === "Escape") {
        this.stopPlaying();
      } else if (key === "m") {
        this.toggleMute();
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
      return _audioElementsByTrackNumber(track);
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
