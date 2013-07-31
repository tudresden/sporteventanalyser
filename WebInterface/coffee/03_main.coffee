console.info "# SEA - sport event analyzer"
console.info "## Initializing"
running = false

ball = new BallModel()
ball_3d = new Ball3d "img/ball.png", ball
engine = new Engine(ball_3d)

playermodels = {}
playerhtmls = {}
tmp_team_name = ""
tmp_counter = {true: 0, false: 0}
teams = []

team_a_stats = {}
team_b_stats = {}
observed_player_a = null
observed_player_b = null
team_a_observed = false

console.info "* current local time is: " + Date.now()

run = ->
  requestAnimationFrame run
  engine.render()

replace_img_by_svg = ->
  console.info "* replace img by the svg within"
  $("img").each () ->
    $img = $(@)
    imgid = $img.attr "id"
    imgclass = $img.attr "class"
    imgurl = $img.attr "src"
    if imgurl.substring(imgurl.length - 3) == "svg"
      $.get imgurl, (data) ->
        $svg = $(data).find "svg"
        $svg = $svg.attr("id", imgid) if imgid?
        $svg = $svg.attr("class", imgclass) if imgclass?
        $svg = $svg.removeAttr "xmlns:a"
        $img.replaceWith $svg

refresh_selection = (event, ui) ->
  all_plrs = []
  plr_ids_a = []
  plr_ids_b = []
  $("#team_a").find("tbody").find("tr.ui-selected").each (i, t) ->
    all_plrs.push t.id
    plr_ids_a.push t.id
  $("#team_b").find("tbody").find("tr.ui-selected").each (i, t) ->
    all_plrs.push t.id
    plr_ids_b.push t.id

  a_stats = $("#player_a_stats")
  if plr_ids_a.length
    observed_player_a = a_plr = playermodels[plr_ids_a[0]]
    a_stats.find(".player_name").text a_plr?.name
    plrhtml = playerhtmls[a_plr.id]
    a_stats.find("tbody").replaceWith plrhtml.tbody()
  else
    observed_player_a = null
    a_stats.find(".player_name").text teams[0]
    a_stats.find("tbody").html ""

  b_stats = $("#player_b_stats")
  if plr_ids_b.length
    observed_player_b = b_plr = playermodels[plr_ids_b[0]]
    b_stats.find(".player_name").text b_plr?.name
    plrhtml = playerhtmls[b_plr.id]
    b_stats.find("tbody").replaceWith plrhtml.tbody()
  else
    observed_player_b = null
    b_stats.find(".player_name").text teams[1]
    b_stats.find("tbody").html ""

  if all_plrs.length is 0
    $.each playermodels, (k, v) ->
      v.selected = 2
  else
    $.each playermodels, (k, v) ->
      v.selected = if k in all_plrs then 1 else 0

add_player = (v, i) ->
  if i == 0
    tmp_team_name = v.TeamName
  if tmp_counter[v.TeamName == tmp_team_name] == 0
    teams.push v.TeamName
  tmp_counter[v.TeamName == tmp_team_name] += 1
  color = "rot"

  tableentry = '<tr id="'+v.PlayerID+'"><td>'+v.PlayerName+'</td><td class="smallinfo"></td></tr>'
  team_a = $("#team_a")
  team_b = $("#team_b")

  if v.TeamName == tmp_team_name
    team_a.find("tbody").append tableentry
    $("body").find(".team_a_name").text v.TeamName;
    color = "gelb"
  else
    team_b.find("tbody").append tableentry
    $("body").find(".team_b_name").text v.TeamName;

  tshirt = "img/trikot_" + color + "_" + tmp_counter[v.TeamName == tmp_team_name] + ".png"

  if v.TeamName == tmp_team_name
    team_a.find("tr#"+v.PlayerID+" .smallinfo").append $("<img class=\"tshirt\" src=\"" + tshirt + "\"/>")
  else
    team_b.find("tr#"+v.PlayerID+" .smallinfo").append $("<img class=\"tshirt\" src=\"" + tshirt + "\"/>")

  plr = new PlayerModel v.PlayerID, v.PlayerName, v.TeamName
  plr_3d = new Player3d tshirt, plr
  engine.add plr_3d
  engine.players.push plr_3d
  playermodels[v.PlayerID] = playermodels["" + v.PlayerID] = plr
  playerhtmls[v.PlayerID] = playerhtmls["" + v.PlayerID] = new PlayerHTML plr

update_position = (item) ->
  item.positionNodes.forEach (v, i) ->
    switch v.constructor.name
      when "BallPosition"
        data = {}
        data.x = parseInt v.positionX if v.positionX?
        data.y = parseInt v.positionY if v.positionY?
        data.z = parseInt v.positionZ if v.positionZ?
        ball.update_position Date.now(), engine.reposition data
      when "PlayerPosition"
        data = {}
        data.x = parseInt v.positionX if v.positionX?
        data.y = parseInt v.positionY if v.positionY?
        playermodels[v.id]?.update_position Date.now(), engine.reposition data
      else
        console.warn "Unknown position update.", v

update_statistics = (item) ->
  item.playerStatistics?.forEach (v, i) ->
    switch v.constructor.name
      when "PlayerStatistic"
        playermodels[v.id]?.update_stats Date.now(), v
        if observed_player_a?
          plrhtml = playerhtmls[observed_player_a.id]
          $("#player_a_stats").find("tbody").replaceWith plrhtml.tbody()
        else if observed_player_b?
          plrhtml = playerhtmls[observed_player_b.id]
          $("#player_b_stats").find("tbody").replaceWith plrhtml.tbody()
      else
        console.warn "Unknown Statistics", v

update_commentator = (v) ->
  switch v.constructor.name
    when "CurrentPrognosisData"
      commentator = $("#attackprophet")
      saying = commentator.find(".says")
      data = v.attackResultPrediction
      if data?
        max = 0.0
        probably = "Any"
        $.each data, (k) ->
          d = parseFloat data[k]
          if d > max
            max = d
            probably = k

          height = (Math.floor(d*0.01*BARS_MAX_HEIGHT)+BARS_MIN_HEIGHT)
          top = BARS_MAX_HEIGHT + BARS_MIN_HEIGHT - height

          title = k + ": " + d + "%"
          bar = commentator.find("#prob_"+k)
          if bar.attr(title) != title
            bar.attr "title", title
            bar.animate {"height": height+"pt", "margin-top": top+"pt"}, BARS_ANIM_FACTOR
        
        probably = t "commentPred:" + probably
        if saying.html() != probably
          saying.hide(0, ->
            saying.html(probably)
          ).fadeIn BARS_ANIM_FACTOR

      commentator = $("#passprophet")
      saying = commentator.find(".says")
      data = v.passSuccessPrediction
      if data?  # Probablility for success of pass
        d = parseFloat data.passSuccessful

        height = (Math.floor(d*0.01*BARS_MAX_HEIGHT)+BARS_MIN_HEIGHT)
        top = 0 #BARS_MAX_HEIGHT + BARS_MIN_HEIGHT - height
        bar = commentator.find("#prob_success")
        title = "Success: " + d + "%"
        if bar.attr("title") != title
          bar.animate {"height": height+"pt", "margin-top": top+"pt"}, BARS_ANIM_FACTOR
          bar.attr "title", title

          height = (Math.floor((100.0-d)*0.01*BARS_MAX_HEIGHT)+BARS_MIN_HEIGHT)
          top = 0 #BARS_MAX_HEIGHT + BARS_MIN_HEIGHT - height
          bar = commentator.find("#prob_fail")
          bar.animate {"height": height+"pt", "margin-top": top+"pt"}, BARS_ANIM_FACTOR
          bar.attr "title", "Fail: " + (100.0 - d) + "%"

        probably = t "commentPred:passFail"
        if d > 0.5
          probably = t "commentPred:passSuccess"
        if saying.html() != probably
          saying.hide(0, ->
            saying.html(probably)
          ).fadeIn BARS_ANIM_FACTOR
    else
      console.warn "unknown type of prediction: ", v

update_gamedata = (item) ->
  $.each item.playingTimeInformation, (k, v) ->
    switch k
      when "playingTime"
        $("#content").find("#time").text if v.indexOf(':') < 2 then "0"+v  else v

update_heatmap = (item) ->
  if observed_player_a or observed_player_b
    #console.log item.playerHeatMaps
  else
    map = $("#heatmap")
    $.each item.teamHeatMaps, (k, heatmap) ->
      if heatmap.teamname == "GELB" and team_a_observed  # TODO needs hands-on if team names alter! (I suggest work on Statistics instead)
        $.each eval(heatmap.map), (y, array) ->
          $.each array, (x, val) ->
            map.find("#"+x+"_"+y).opacity val
      else
        $.each eval(heatmap.map), (y, array) ->
          $.each array, (x, val) ->
            map.find("#"+x+"_"+y).css 
              "fill": "rgba(255,0,0,"+val+")"
              "stroke": "rgba(255,255,255,0.1)"

establish_sea_connection = (onsuccess) ->
  sea.connect "seaclient@sea", "sea", "mobilis@sea", ->
    sea.getGameMappings (mappings) ->
      console.info "* Setting up field"
      gf = mappings.GameFieldSize
      reality =  # x and y confuse you here
        height: gf.GameFieldMaxX - gf.GameFieldMinX
        width: gf.GameFieldMaxY - gf.GameFieldMinY
        offy: parseInt gf.GameFieldMinX
        offx: parseInt gf.GameFieldMinY
      field = new FieldModel reality
      field3d = new Field3d "img/Fussballfeld.png", field
      engine.set_field field3d

      console.info "* Setting up goal positions and size"
      # TODO: engine.set_goals_pos mappings.Goals
      
      console.info "* Setting up players"
      mappings.PlayerMappings.forEach add_player

    console.info "* adding pos handler"

    sea.pubsub.subscribeStatistic()

    sea.pubsub.addCurrentPositionDataHandler update_position

    sea.pubsub.addCurrentPlayerDataHandler update_statistics

    sea.pubsub.addCurrentTeamDataHandler update_statistics

    sea.pubsub.addCurrentHeatMapDataHandler update_heatmap

    sea.pubsub.addCurrentPrognosisDataHandler update_commentator
      
    sea.pubsub.addCurrentGameDataHandler update_gamedata

    onsuccess?()

$ ->
  $("#content").hide()
  $("#heatmap").hide()

  replace_img_by_svg()

  console.info "* preparing view buttons"
  $("#perspectives_menu").buttonset
  for b in $("#perspectives_menu").find("input")
    do (b) ->
      switch b.id
        when "HEAT_A"
          b.onclick = ->
            $("#field").hide 0, ->
              $("#heatmap").show(0)
        when "HEAT_B"
          b.onclick = ->
            $("#field").hide 0, ->
              $("#heatmap").show(0)
        else
          b.onclick = ->
            engine.camera_mode = b.id
            $("#heatmap").hide 0, ->
              $("#field").show(0)

  console.info "* adding canvas"
  $("#field").append engine.get_canvas()

  console.info "* making selectables"
  $("#team_a, #team_b").selectable
    filter: 'tr'
    selected: refresh_selection

  #console.info "* preparing diagrams"
  #nodata = [[0, 0], [1, 0]]
  #options = 
  #  series:
  #    lines:
  #      show: true
  #    points:
  #      show: false
  #$("#player_a_plot").plot([nodata], options).data("plot")
  #$("#player_b_plot").plot([nodata], options).data("plot")
  
  $("#startbutton").button().click (event) ->
    $(this).html '<img src="img/spinner_animated.svg"/>'
    if not running
      establish_sea_connection ->
        running = true
        $("#heatmap").hide()
        $("#content").show().fadeIn "slow", ->
          $("#startbutton").fadeOut "fast", ->
            run()
