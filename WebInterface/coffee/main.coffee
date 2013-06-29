$ ->
  console.log "# SEA - sport event analyzer"

  console.log "## Initializing"

  ball = new Ball("img/ball.png")
  field = new Field("img/Fussballfeld.png")
  engine = new Engine(ball)

  players = []
  playersdict = {}

  refresh_selection = ->
    plr_ids = []
    $("#team_a").find("tbody").find("tr.ui-selected").each (i, t) ->
      console.log i
      plr_ids.push t.id
    $("#team_b").find("tbody").find("tr.ui-selected").each (i, t) ->
      plr_ids.push t.id
    console.log plr_ids

  engine.add field

  console.log Date.now()

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
  
  console.log "* starting animation loop"
  run = ->
    requestAnimationFrame run
    engine.render()
  requestAnimationFrame run

  $("#goals").button().click (event) ->
    sea.connect "seaclient@sea/Client", "sea", "mobilis@sea", ->
      sea.getGameMappings (mappings) ->
        console.log "* Setting up field"
        # TODO: engine.set_field_size mappings.GameFieldSize
        console.log "* Setting up goal positions and size"
        # TODO: engine.set_goals_pos mappings.Goals
        console.log "* Setting up players"
        console.log mappings.PlayerMappings
        tmp_team_name = ""
        tmp_counter = {true: 0, false: 0}
        mappings.PlayerMappings.forEach (v, i) ->
          if i == 0
            tmp_team_name = v.TeamName
          tmp_counter[v.TeamName == tmp_team_name] += 1
          color = "rot"
          if v.TeamName == tmp_team_name
            color = "gelb"
          tshirt = "img/trikot_" + color + "_" + tmp_counter[v.TeamName == tmp_team_name] + ".png"
          plr = new Player v.PlayerID, v.TeamName, tshirt
          console.log "Player: " + v.PlayerName + " (ID: " + v.PlayerID + ", Team: " + v.TeamName + ", Tshirt: " + tshirt + ")"
          engine.add plr
          players.push plr
          playersdict[v.PlayerID] = players.length - 1

    sea.pubsub.subscribeStatistic()

    sea.pubsub.addCurrentPositionDataHandler (item) ->
      console.log(item)

  console.log "* starting update loop"
  update = ->
    window.setTimeout update, 1000
    # setting new player positions
    for plr in players.concat [ball]
      do (plr) ->
        if Math.random() > 0.5
          return
        data =
          pos:
            x: Math.random() * field.width - field.width/2
            y: Math.random() * field.height - field.height/2
        plr.update 0, data
  window.setTimeout update, 1000
