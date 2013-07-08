console.log "# SEA - sport event analyzer"
console.log "## Initializing"

ball = new Ball("img/ball.png")
engine = new Engine(ball)

playersdict = {}
tmp_team_name = ""
tmp_counter = {true: 0, false: 0}

console.log Date.now()

console.log "* starting animation loop"
run = ->
  requestAnimationFrame run
  engine.render()
requestAnimationFrame run


refresh_selection = ->
  plr_ids_a = []
  plr_ids_b = []
  $("#team_a").find("tbody").find("tr.ui-selected").each (i, t) ->
    plr_ids_a.push t.id
  $("#team_b").find("tbody").find("tr.ui-selected").each (i, t) ->
    plr_ids_b.push t.id
  $("#player_a_stats").find(".player_name").text playersdict[plr_ids_a[0]].name
  $("#player_b_stats").find(".player_name").text playersdict[plr_ids_b[0]].name
  all_plrs = plr_ids_a.concat plr_ids_b
  console.log all_plrs
  engine.select_players all_plrs

add_player = (v, i) ->
  if i == 0
    tmp_team_name = v.TeamName
  tmp_counter[v.TeamName == tmp_team_name] += 1
  color = "rot"
  tableentry = '<tr id="'+v.PlayerID+'"><td>'+v.PlayerName+'</td><td class="smallinfo"></td></tr>'
  if v.TeamName == tmp_team_name
    $("#team_a").find("tbody").append tableentry
    $("body").find(".team_a_name").text v.TeamName;
    console.log v.TeamName
    color = "gelb"
  else
    $("#team_b").find("tbody").append tableentry
    $("body").find(".team_b_name").text v.TeamName;
    console.log v.TeamName
  tshirt = "img/trikot_" + color + "_" + tmp_counter[v.TeamName == tmp_team_name] + ".png"
  plr = new Player v.PlayerID, v.PlayerName, v.TeamName, tshirt
  console.log "Player: " + v.PlayerName + " (ID: " + v.PlayerID + ", Team: " + v.TeamName + ", Tshirt: " + tshirt + ")"
  engine.add plr
  engine.players.push plr
  playersdict[v.PlayerID] = plr
  playersdict["" + v.PlayerID] = plr

update_position = (v, i) ->
  console.log v
  switch v.constructor.name
    when "BallPosition"
      data =
        y: parseInt v.positionY
        x: parseInt v.positionZ
      data = engine.reposition data
      ball.update 0, data
    when "PlayerPosition"
      data =
        y: parseInt v.positionX
        x: parseInt v.positionY
      playersdict[v.id]?.update 0, engine.reposition data

establish_sea_connection = (onsuccess) ->
  sea.connect "seaclient@sea/Client", "sea", "mobilis@sea", ->
    sea.getGameMappings (mappings) ->
      console.log "* Setting up field"
      gf = mappings.GameFieldSize
      console.log gf
      reality =
        width: gf.GameFieldMaxX - gf.GameFieldMinX
        height: gf.GameFieldMaxY - gf.GameFieldMinY
        offx: parseInt gf.GameFieldMinX
        offy: parseInt gf.GameFieldMinY
      field = new Field("img/Fussballfeld.png", reality)
      engine.set_field field

      console.log "* Setting up goal positions and size"
      # TODO: engine.set_goals_pos mappings.Goals
      
      console.log "* Setting up players"
      console.log mappings.PlayerMappings
      mappings.PlayerMappings.forEach add_player

    console.log "* adding pos handler"
    sea.pubsub.addCurrentPositionDataHandler (item) ->
      item.positionNodes.forEach update_position

    sea.pubsub.subscribeStatistic()
    onsuccess?()
    
$ ->
  $("#content").hide()
  console.log "* preparing view buttons"
  $("#perspectives_menu").buttonset
  for b in $("#perspectives_menu").find("input")
    do (b) ->
      b.onclick = -> engine.camera_mode = b.id

  console.log "* adding canvas"
  $("#field").append engine.get_canvas()

  console.log "* making selectables"
  $("#team_a, #team_b").selectable
    filter: 'tr'
    selected: (event, ui) ->
      refresh_selection()
  
  $("#startbutton").button().click (event) ->
    $(this).html '<img src="img/spinner_animated.svg"/>'
    establish_sea_connection ->
      $("#content").show().fadeIn "slow", ->
        $("#startbutton").fadeOut "fast"
