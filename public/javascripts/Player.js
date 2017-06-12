'use strict';

function Player() {
  var _isMuted = false;

  var _interval = undefined;
  var _time = undefined;

  var _beat = 0;

  var _keyboardBeat = undefined;

  var _tempo = 120;

  var _pattern = new Pattern();

  var _audioElements = document.getElementsByTagName("audio");
  var _numberOfAudioElements = _audioElements.length;

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

  function nextBeat(beat) {
    return (beat !== undefined) ? (beat + 1) % _pattern.getLength() : undefined;
  }

  async function singleBeat() {
    _beat = _keyboardBeat;
    setCurrentBeatCSS(_beat);
    var tracksToPlay = _pattern.play(_beat);
    var j;
    for (j = 0; j < tracksToPlay.length; j++) {
      resetAndPlay(audioElementsByTrackNumber(tracksToPlay[j]));
    }
    await sleep(_time * 0.5);
    _keyboardBeat = nextBeat(_keyboardBeat);
    removeCurrentBeatCSS(_beat);
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
    throw 'AudioElementForTrackNotFoundException';
  }

  function isNumber(key) {
    return !isNaN(parseInt(key));
  }

  function numberKey(tracknb) {
    if (_keyboardBeat !== undefined) {
      Utilities.getCheckbox(tracknb, _keyboardBeat).click(); // click to trigger events
      if (Utilities.getCheckbox(tracknb, _keyboardBeat).checked) {
        resetAndPlay(audioElementsByTrackNumber(tracknb));
      }
    } else {
      resetAndPlay(audioElementsByTrackNumber(tracknb));
    }
  }

  return {
    togglePlaying: function(testBool = false) {
      if (!this.isPlaying()) {
        this.startPlaying(testBool);
      } else {
        this.stopPlaying();
      }
    },
    startPlaying: function(testBool = false) {
      if (!this.isPlaying()) {
        _time = 60000 / _tempo;
        if (!testBool) {
          _keyboardBeat = 0;
          singleBeat();
          _interval = setInterval(singleBeat, _time);
        } else {
          _interval = "dummy";
        }
      }
    },
    stopPlaying: function(testBool = false) {
      var i;
      for (i = 0; i < _audioElements.length; i++) {
        resetAudioElement(_audioElements[i]);
      }
      _keyboardBeat = undefined;
      if (!testBool) {
        if (_interval !== undefined) {
          clearInterval(_interval);
        }
      }
      _interval = undefined;
    },
    isPlaying: function() {
      return (_interval !== undefined);
    },
    onKeyDown: function(e, testBool = false) {
      var key = e.key;
      if (key === "g" || key === "p") {
        this.togglePlaying(testBool);
      } else if (key === "Escape") {
        this.stopPlaying(testBool);
      } else if (key === "m") {
        this.toggleMute();
      } else if (isNumber(key)) {
        var tracknb = parseInt(key) - 1;
        if (tracknb < _pattern.getTracks()) {
          numberKey(tracknb);
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
