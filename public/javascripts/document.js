var player;

window.onload = function() {
  player = new Player;

  document.addEventListener('keydown', function(e) {
    player.onKeyDown(e);
  });

  document.getElementById("pattern_form").onsubmit = function() {
    var cbs = Utilities.getCheckboxes();
    var i;
    for(i = 0; i < cbs.length; i++) {
      if(cbs[i].checked) {
        document.getElementById("hidden_" + cbs[i].id).disabled = true;
      }
    }
  };
}
