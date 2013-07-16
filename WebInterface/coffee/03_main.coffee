comment = console.log

comment "# SEA - sport event analyzer"
comment "## Initializing"
running = false

ball = new Ball("img/ball.png")
engine = new Engine(ball)

playersdict = {}
tmp_team_name = ""
tmp_counter = {true: 0, false: 0}
teams = []

team_a_stats = {}
team_b_stats = {}
observed_player_a = null
observed_player_b = null

comment "* current local time is: " + Date.now()

run = ->
  # animation loop
  requestAnimationFrame run
  engine.render()

replace_img_by_svg = ->
  comment "* replace img with svg by the svg"
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

compose_stats_tbody = (stats) ->
  res = "<tbody>"
  $.each stats, (k, v) ->
    res += "<tr><td>" + t(""+k) + "</td><td>" + v + "</td></tr>\n"
  return res + "</tbody>"

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
    observed_player_a = a_plr = playersdict[plr_ids_a[0]]
    a_stats.find(".player_name").text a_plr?.name
    a_stats.find("tbody").replaceWith compose_stats_tbody a_plr?.stats
  else
    observed_player_a = null
    a_stats.find(".player_name").text teams[0]
    a_stats.find("tbody").replaceWith compose_stats_tbody team_a_stats

  b_stats = $("#player_b_stats")
  if plr_ids_b.length
    observed_player_b = b_plr = playersdict[plr_ids_b[0]]
    b_stats.find(".player_name").text b_plr?.name
    b_stats.find("tbody").replaceWith compose_stats_tbody b_plr?.stats
  else
    observed_player_b = null
    b_stats.find(".player_name").text teams[1]
    b_stats.find("tbody").replaceWith compose_stats_tbody team_b_stats

  engine.select_players all_plrs

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

  plr = new Player v.PlayerID, v.PlayerName, v.TeamName, tshirt
  engine.add plr
  engine.players.push plr
  playersdict[v.PlayerID] = plr
  playersdict["" + v.PlayerID] = plr

update_position = (v, i) ->
  switch v.constructor.name
    when "BallPosition"
      data =
        x: parseInt v.positionX
        y: parseInt v.positionY
        z: parseInt v.positionZ
      ball.update Date.now(), engine.reposition data
    when "PlayerPosition"
      data =
        x: parseInt v.positionX
        y: parseInt v.positionY
      playersdict[v.id]?.update Date.now(), engine.reposition data
    else
      console.log "Unknown position update.", v

update_statistics = (v, i) ->
  switch v.constructor.name
    when "PlayerStatistic"
      playersdict[v.id]?.update_stats Date.now(), v
      if observed_player_a? and v.id == observed_player_a.id
        a_stats = $("#player_a_stats")
        a_stats.find("tbody").replaceWith compose_stats_tbody observed_player_a.stats
      else if observed_player_b? and v.id == observed_player_b.id
        b_stats = $("#player_b_stats")
        b_stats.find("tbody").replaceWith compose_stats_tbody observed_player_b.stats
    else
      console.log "Unknown Statistics", v

update_commentator = (v) ->
  switch v.constructor.name
    when "CurrentPrognosisData"
      saying = $("#commentator").find(".says")
      data = v.attackResultPrediction
      if data?
        console.log data
        max = 0.0
        probably = "Any"
        $.each data, (k) ->
          d = parseInt data[k]
          if d > max
            max = d
            probably = k
        probably = t "commentPred:" + probably
        if saying.html() != probably
          saying.fadeOut "fast", ->
            saying.html(probably).fadeIn("fast")
    else
      console.log "unknown type of prediction: ", v

establish_sea_connection = (onsuccess) ->
  sea.connect "seaclient@sea", "sea", "mobilis@sea", ->
    sea.getGameMappings (mappings) ->
      comment "* Setting up field"
      gf = mappings.GameFieldSize
      reality =  # x and y confuse you here
        height: gf.GameFieldMaxX - gf.GameFieldMinX
        width: gf.GameFieldMaxY - gf.GameFieldMinY
        offy: parseInt gf.GameFieldMinX
        offx: parseInt gf.GameFieldMinY
      field = new Field("img/Fussballfeld.png", reality)
      engine.set_field field

      comment "* Setting up goal positions and size"
      # TODO: engine.set_goals_pos mappings.Goals
      
      comment "* Setting up players"
      mappings.PlayerMappings.forEach add_player

    comment "* adding pos handler"

    sea.pubsub.subscribeStatistic()

    sea.pubsub.addCurrentPositionDataHandler (item) ->
      item.positionNodes.forEach update_position

    sea.pubsub.addCurrentPlayerDataHandler (item) ->
      item.playerStatistics.forEach update_statistics

    sea.pubsub.addCurrentTeamDataHandler (item) ->
      item.teamStatistics.forEach update_statistics

    #sea.pubsub.addCurrentHeatMapDataHandler (item) ->
      #console.log "heatmap", item

    sea.pubsub.addCurrentPrognosisDataHandler update_commentator
      
    sea.pubsub.addCurrentGameDataHandler (item) ->
      console.log item

    onsuccess?()

$ ->
  $("#content").hide()
  $("#heatmap").hide()

  replace_img_by_svg()

  console.log "* preparing view buttons"
  $("#perspectives_menu").buttonset
  for b in $("#perspectives_menu").find("input")
    do (b) ->
      switch b.id
        when "HEAT"
          b.onclick = ->
            $("#field").hide 0, ->
              $("#heatmap").show(0)
        else
          b.onclick = ->
            engine.camera_mode = b.id
            $("#heatmap").hide 0, ->
              $("#field").show(0)

  console.log "* adding canvas"
  $("#field").append engine.get_canvas()

  console.log "* making selectables"
  $("#team_a, #team_b").selectable
    filter: 'tr'
    selected: refresh_selection

  #console.log "* preparing diagrams"
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
        requestAnimationFrame run
        $("#content").show().fadeIn "slow", ->
          $("#startbutton").fadeOut "fast"
