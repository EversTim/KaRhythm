var player;

window.onload = function() {
  player = new Player;

  document.addEventListener('keydown', function(e) {
    player.onKeyDown(e);
  });

  //player.toggleMute();
  player.startPlaying();
}
