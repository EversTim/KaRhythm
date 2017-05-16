var player = new Player();

document.addEventListener('keydown', function(e) {
  player.onKeyDown(e, function() {
    if (/^[a-z]$/.test(e.key) || e.key === "Escape") {
      alert(e.key)
    }
  })
});

player.setLengthAndTracksAndFindCheckboxes(4, 2);
player.playLoopOnce();
