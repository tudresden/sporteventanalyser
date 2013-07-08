// Generated by CoffeeScript 1.6.2
(function() {
  var ANIM_FACTOR, BALL_SIZE, Ball, Drawable, Engine, FIELD_HEIGHT, FIELD_WIDTH, FIELD_WIDTH2, Field, Moveable, PI2, PLAYER_SIZE, PLAYER_SIZE2, Player, SELECT_SIZE, add_player, ball, engine, establish_sea_connection, playersdict, refresh_selection, run, running, tmp_counter, tmp_team_name, update_position,
    __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; },
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  PI2 = Math.PI / 2;

  FIELD_WIDTH = 120;

  FIELD_WIDTH2 = FIELD_WIDTH / 2;

  FIELD_HEIGHT = 90;

  FIELD_HEIGHT = FIELD_HEIGHT / 2;

  BALL_SIZE = 2;

  PLAYER_SIZE = 4;

  PLAYER_SIZE2 = PLAYER_SIZE / 2;

  SELECT_SIZE = 5;

  ANIM_FACTOR = 10;

  Engine = (function() {
    function Engine(ball) {
      var now;

      this.ball = ball;
      this.resolution = [640, 480];
      this.bgcolor = 0..fffff0;
      this.obj_stack = [];
      this.scene = new THREE.Scene;
      this.camera = new THREE.PerspectiveCamera(75, this.resolution[0] / this.resolution[1], 1, 10000);
      this.camera_mode = "BIRD";
      this.amb_light = new THREE.AmbientLight(0xffffff, 1.0);
      this.scene.add(this.amb_light);
      this.renderer = new THREE.WebGLRenderer;
      this.renderer.clearColor = this.bgcolor;
      this.renderer.clear;
      this.renderer.setSize(this.resolution[0], this.resolution[1]);
      console.log(this.camera);
      now = new Date;
      this.start_time = now.getTime();
      this.add(this.ball);
      this.mean_ball_cnt = 30;
      this.mean_ball_pos = {
        x: 0,
        y: 0,
        z: 0
      };
      this.reality = {
        width: FIELD_WIDTH,
        height: FIELD_HEIGHT,
        offx: 0,
        offy: 0
      };
      this.field = null;
      this.players = [];
    }

    Engine.prototype.set_field = function(field) {
      this.field = field;
      this.reality = this.field.reality;
      return this.add(this.field);
    };

    Engine.prototype.reposition = function(position) {
      var result;

      result = {};
      if (this.field) {
        if (position.x) {
          result.x = (position.x - this.reality.width / 2) * this.field.width / this.reality.width;
        }
        if (position.y) {
          result.y = position.y * this.field.height / this.reality.height;
        }
        if (position.z) {
          result.z = position.z * (this.field.width / this.reality.width + this.field.height / this.reality.height) / 2;
        }
      }
      return result;
    };

    Engine.prototype.get_canvas = function(target_div) {
      return this.renderer.domElement;
    };

    Engine.prototype.add = function(obj) {
      var drawable, _i, _len, _ref, _results;

      this.obj_stack.push(obj);
      _ref = obj.drawables;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        drawable = _ref[_i];
        _results.push(this.scene.add(drawable));
      }
      return _results;
    };

    Engine.prototype.render = function() {
      var obj, time, _i, _j, _len, _len1, _ref, _ref1;

      time = (new Date).getTime() - this.start_time;
      this.mean_ball_pos.x = this.mean_ball_cnt * this.mean_ball_pos.x + this.ball.ball.position.x;
      this.mean_ball_pos.x /= this.mean_ball_cnt + 1;
      this.mean_ball_pos.y = this.mean_ball_cnt * this.mean_ball_pos.y + this.ball.ball.position.y;
      this.mean_ball_pos.y /= this.mean_ball_cnt + 1;
      this.mean_ball_pos.z = this.mean_ball_cnt * this.mean_ball_pos.z + this.ball.ball.position.z;
      this.mean_ball_pos.z /= this.mean_ball_cnt + 1;
      switch (this.camera_mode) {
        case "BIRD":
          this.camera.position.set(0, FIELD_WIDTH / 2, 0);
          this.camera.rotation.set(-Math.PI / 2, 0, 0);
          break;
        case "KEEPERA":
          this.camera.position.set(-FIELD_WIDTH / 2, 6, 0);
          this.camera.lookAt(this.mean_ball_pos);
          break;
        case "KEEPERB":
          this.camera.position.set(FIELD_WIDTH / 2, 6, 0);
          this.camera.lookAt(this.mean_ball_pos);
          break;
        default:
          this.camera.position.x = 5 * Math.sin(time / 600);
          this.camera.position.y = 33 + 5 * Math.cos(time / 1200);
          this.camera.position.z = FIELD_WIDTH / 2 + 5 * Math.sin(time / 2700);
          this.camera.lookAt(this.mean_ball_pos);
      }
      _ref = this.obj_stack;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        obj = _ref[_i];
        obj.follow(this.camera.position, this.camera_mode === "BIRD");
      }
      _ref1 = this.obj_stack;
      for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
        obj = _ref1[_j];
        obj.animate(time);
      }
      return this.renderer.render(this.scene, this.camera);
    };

    Engine.prototype.select_players = function(plr_ids) {
      if (plr_ids.length === 0) {
        return this.players.forEach(function(v, i) {
          return v.selected = 2;
        });
      } else {
        return this.players.forEach(function(v, i) {
          var _ref;

          return v.selected = (_ref = v.id, __indexOf.call(plr_ids, _ref) >= 0) ? 1 : 0;
        });
      }
    };

    return Engine;

  })();

  Drawable = (function() {
    function Drawable() {}

    Drawable.prototype.animate = function(time) {};

    Drawable.prototype.follow = function(target, reset) {
      var f, _i, _len, _ref, _results;

      if (reset == null) {
        reset = false;
      }
      _ref = this.followers;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        f = _ref[_i];
        _results.push((function(f) {
          if (reset) {
            return f.rotation.set(-PI2, 0, 0);
          } else {
            return f.lookAt(target);
          }
        })(f));
      }
      return _results;
    };

    Drawable.prototype.toString = function() {
      return "Drawable";
    };

    return Drawable;

  })();

  Moveable = (function(_super) {
    __extends(Moveable, _super);

    function Moveable() {}

    Moveable.prototype.update = function(time, data) {
      if (data.x != null) {
        this.target_pos.x = data.x;
      }
      if (data.y != null) {
        this.target_pos.y = data.y;
      }
      if (data.z != null) {
        this.target_pos.z = data.z;
      }
      if (time != null) {
        return this.last_update = time;
      }
    };

    Moveable.prototype.toString = function() {
      return "Moveable";
    };

    return Moveable;

  })(Drawable);

  Field = (function(_super) {
    __extends(Field, _super);

    function Field(texturefile, reality) {
      var geometry, mat_cfg, material;

      this.reality = reality;
      this.ratio = this.reality.width / this.reality.height;
      this.width = FIELD_WIDTH;
      this.height = FIELD_HEIGHT;
      geometry = new THREE.PlaneGeometry(this.width, this.height);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture(texturefile),
        side: THREE.DoubleSide
      };
      material = new THREE.MeshLambertMaterial(mat_cfg);
      this.field = new THREE.Mesh(geometry, material);
      this.field.rotation.set(-PI2, 0, 0);
      this.drawables = [this.field];
      this.followers = [];
    }

    Field.prototype.toString = function() {
      return "Field";
    };

    return Field;

  })(Drawable);

  Ball = (function(_super) {
    __extends(Ball, _super);

    function Ball(texturefile) {
      var geometry, mat_cfg, material;

      geometry = new THREE.PlaneGeometry(BALL_SIZE, BALL_SIZE);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture(texturefile),
        alphaTest: 0.5
      };
      material = new THREE.MeshBasicMaterial(mat_cfg);
      this.ball = new THREE.Mesh(geometry, material);
      geometry = new THREE.PlaneGeometry(1, 1);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture("img/shadow.png"),
        transparent: true
      };
      material = new THREE.MeshBasicMaterial(mat_cfg);
      this.shadow = new THREE.Mesh(geometry, material);
      this.shadow.position.y = 0.001;
      this.shadow.rotation.set(-PI2, 0, 0);
      this.followers = [this.ball];
      this.drawables = [this.ball, this.shadow];
      this.ball.position.set(0, this.ball.geometry.height / 2, 0);
      this.last_update = 0;
      this.target_pos = {
        x: 0,
        y: 0,
        z: 0
      };
      this.anim_factor = ANIM_FACTOR;
      this.time = 0;
    }

    Ball.prototype.animate = function(time) {
      var s_scale;

      this.ball.position.x = (this.anim_factor * this.ball.position.x + this.target_pos.x) / (this.anim_factor + 1);
      this.ball.position.y = Math.max(this.ball.geometry.height / 2, (this.anim_factor * this.ball.position.y + 0.5 * this.ball.geometry.height + this.target_pos.z) / (this.anim_factor + 1));
      this.ball.position.z = (this.anim_factor * this.ball.position.z + this.target_pos.y) / (this.anim_factor + 1);
      this.shadow.position.x = this.ball.position.x;
      this.shadow.position.z = this.ball.position.z;
      s_scale = 1.0 + 1.0 * Math.max(1.0, this.ball.position.y);
      this.shadow.scale.set(s_scale, s_scale, s_scale);
      this.shadow.material.opacity = Math.min(1.0, Math.max(0.0, 1.0 / this.ball.position.y));
      return this.time = time;
    };

    Ball.prototype.toString = function() {
      return "Ball";
    };

    return Ball;

  })(Moveable);

  Player = (function(_super) {
    __extends(Player, _super);

    function Player(id, name, team, tricot_image) {
      var geometry, mat_cfg, material;

      this.id = id;
      this.name = name;
      this.team = team;
      this.tricot_image = tricot_image;
      geometry = new THREE.PlaneGeometry(PLAYER_SIZE, PLAYER_SIZE);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture(this.tricot_image),
        transparent: true
      };
      material = new THREE.MeshBasicMaterial(mat_cfg);
      this.shirt = new THREE.Mesh(geometry, material);
      this.shirt.position.y = this.shirt.geometry.height / 2;
      geometry = new THREE.PlaneGeometry(PLAYER_SIZE2, PLAYER_SIZE2);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture("img/shadow.png"),
        alpha: 0.5,
        transparent: true
      };
      material = new THREE.MeshBasicMaterial(mat_cfg);
      this.shadow = new THREE.Mesh(geometry, material);
      this.shadow.position.y = 0.005;
      this.shadow.rotation.set(-PI2, 0, 0);
      geometry = new THREE.PlaneGeometry(SELECT_SIZE, SELECT_SIZE);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture("img/selection.png"),
        transparent: true
      };
      material = new THREE.MeshBasicMaterial(mat_cfg);
      this.select = new THREE.Mesh(geometry, material);
      this.select.position.y = 0.01;
      this.select.rotation.set(-PI2, 0, 0);
      this.followers = [this.shirt];
      this.drawables = [this.shirt, this.shadow, this.select];
      this.last_update = 0;
      this.target_pos = {
        x: 0,
        y: 0
      };
      this.anim_factor = ANIM_FACTOR;
      this.time = 0;
      this.selected = 2;
    }

    Player.prototype.animate = function(time) {
      var s;

      this.shirt.position.x = (this.anim_factor * this.shirt.position.x + this.target_pos.x) / (this.anim_factor + 1);
      this.shirt.position.z = (this.anim_factor * this.shirt.position.z + this.target_pos.y) / (this.anim_factor + 1);
      this.shadow.position.x = this.shirt.position.x;
      this.shadow.position.z = this.shirt.position.z;
      s = this.shirt.position.y - this.shirt.geometry.height;
      this.shadow.scale.set(s, s, s);
      switch (this.selected) {
        case 2:
          this.select.material.opacity = 0.01;
          this.shirt.material.opacity = 1;
          break;
        case 1:
          s = 1 + 0.2 * Math.sin(0.005 * time);
          this.select.scale.set(s, s, s);
          this.select.rotation.z = 0.01 * time;
          this.select.position.x = this.shirt.position.x;
          this.select.position.z = this.shirt.position.z;
          this.select.material.opacity = 1.0;
          this.shirt.material.opacity = 1.0;
          break;
        default:
          this.select.material.opacity = 0;
          this.shirt.material.opacity = 0.5;
      }
      return this.time = time;
    };

    Player.prototype.toString = function() {
      return "Player(" + this.tricot_image + ")";
    };

    return Player;

  })(Moveable);

  console.log("# SEA - sport event analyzer");

  console.log("## Initializing");

  ball = new Ball("img/ball.png");

  engine = new Engine(ball);

  playersdict = {};

  tmp_team_name = "";

  tmp_counter = {
    "true": 0,
    "false": 0
  };

  running = false;

  console.log(Date.now());

  console.log("* starting animation loop");

  run = function() {
    requestAnimationFrame(run);
    return engine.render();
  };

  refresh_selection = function(ui, event) {
    var all_plrs, plr_ids_a, plr_ids_b, _ref, _ref1;

    all_plrs = [];
    plr_ids_a = [];
    plr_ids_b = [];
    $("#team_a").find("tbody").find("tr.ui-selected").each(function(i, t) {
      all_plrs.push(t.id);
      return plr_ids_a.push(t.id);
    });
    $("#team_b").find("tbody").find("tr.ui-selected").each(function(i, t) {
      all_plrs.push(t.id);
      return plr_ids_b.push(t.id);
    });
    $("#player_a_stats").find(".player_name").text((_ref = playersdict[plr_ids_a[0]]) != null ? _ref.name : void 0);
    $("#player_b_stats").find(".player_name").text((_ref1 = playersdict[plr_ids_b[0]]) != null ? _ref1.name : void 0);
    return engine.select_players(all_plrs);
  };

  add_player = function(v, i) {
    var color, plr, tableentry, tshirt;

    if (i === 0) {
      tmp_team_name = v.TeamName;
    }
    tmp_counter[v.TeamName === tmp_team_name] += 1;
    color = "rot";
    tableentry = '<tr id="' + v.PlayerID + '"><td>' + v.PlayerName + '</td><td class="smallinfo"></td></tr>';
    if (v.TeamName === tmp_team_name) {
      $("#team_a").find("tbody").append(tableentry);
      $("body").find(".team_a_name").text(v.TeamName);
      color = "gelb";
    } else {
      $("#team_b").find("tbody").append(tableentry);
      $("body").find(".team_b_name").text(v.TeamName);
    }
    tshirt = "img/trikot_" + color + "_" + tmp_counter[v.TeamName === tmp_team_name] + ".png";
    plr = new Player(v.PlayerID, v.PlayerName, v.TeamName, tshirt);
    engine.add(plr);
    engine.players.push(plr);
    playersdict[v.PlayerID] = plr;
    return playersdict["" + v.PlayerID] = plr;
  };

  update_position = function(v, i) {
    var data, _ref;

    switch (v.constructor.name) {
      case "BallPosition":
        data = {
          x: parseInt(v.positionX),
          y: parseInt(v.positionY),
          z: parseInt(v.positionZ)
        };
        return ball.update(Date.now(), engine.reposition(data));
      case "PlayerPosition":
        data = {
          x: parseInt(v.positionX),
          y: parseInt(v.positionY)
        };
        if ((_ref = playersdict[v.id]) != null) {
          _ref.update(Date.now(), engine.reposition(data));
        }
        if (v.id === "13") {
          return console.log(data);
        }
        break;
      default:
        return console.log("Unknown position update.");
    }
  };

  establish_sea_connection = function(onsuccess) {
    return sea.connect("seaclient@sea/Client", "sea", "mobilis@sea", function() {
      sea.getGameMappings(function(mappings) {
        var field, gf, reality;

        console.log("* Setting up field");
        gf = mappings.GameFieldSize;
        reality = {
          width: gf.GameFieldMaxX - gf.GameFieldMinX,
          height: gf.GameFieldMaxY - gf.GameFieldMinY,
          offx: parseInt(gf.GameFieldMinX),
          offy: parseInt(gf.GameFieldMinY)
        };
        field = new Field("img/Fussballfeld.png", reality);
        engine.set_field(field);
        console.log("* Setting up goal positions and size");
        console.log("* Setting up players");
        console.log(mappings.PlayerMappings);
        return mappings.PlayerMappings.forEach(add_player);
      });
      console.log("* adding pos handler");
      sea.pubsub.addCurrentPositionDataHandler(function(item) {
        return item.positionNodes.forEach(update_position);
      });
      sea.pubsub.subscribeStatistic();
      return typeof onsuccess === "function" ? onsuccess() : void 0;
    });
  };

  $(function() {
    var b, _fn, _i, _len, _ref;

    $("#content").hide();
    console.log("* preparing view buttons");
    $("#perspectives_menu").buttonset;
    _ref = $("#perspectives_menu").find("input");
    _fn = function(b) {
      return b.onclick = function() {
        return engine.camera_mode = b.id;
      };
    };
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      b = _ref[_i];
      _fn(b);
    }
    console.log("* adding canvas");
    $("#field").append(engine.get_canvas());
    console.log("* making selectables");
    $("#team_a, #team_b").selectable({
      filter: 'tr',
      selected: refresh_selection
    });
    return $("#startbutton").button().click(function(event) {
      $(this).html('<img src="img/spinner_animated.svg"/>');
      if (!running) {
        return establish_sea_connection(function() {
          running = true;
          requestAnimationFrame(run);
          return $("#content").show().fadeIn("slow", function() {
            return $("#startbutton").fadeOut("fast");
          });
        });
      }
    });
  });

  Engine = (function() {
    function Engine(ball) {
      var now;

      this.ball = ball;
      console.log(this.ball);
      this.resolution = [640, 480];
      this.bgcolor = 0..fffff0;
      this.obj_stack = [];
      this.scene = new THREE.Scene;
      this.camera = new THREE.PerspectiveCamera(75, this.resolution[0] / this.resolution[1], 1, 10000);
      this.camera.position.y = 33;
      this.camera.position.z = 100;
      this.camera_mode = "BIRD";
      this.amb_light = new THREE.AmbientLight(0xffffff, 1.0);
      this.scene.add(this.amb_light);
      this.renderer = new THREE.WebGLRenderer;
      this.renderer.clearColor = this.bgcolor;
      this.renderer.clear;
      this.renderer.setSize(this.resolution[0], this.resolution[1]);
      now = new Date;
      this.start_time = now.getTime();
      this.add(this.ball);
      this.mean_ball_cnt = 30;
      this.mean_ball_pos = {
        x: 0,
        y: 0,
        z: 0
      };
      this.reality = {
        width: 120,
        height: 50,
        offx: 0,
        offy: 0
      };
      this.field = null;
      this.players = [];
    }

    Engine.prototype.set_field = function(field) {
      this.field = field;
      this.reality = this.field.reality;
      return this.add(this.field);
    };

    Engine.prototype.reposition = function(position) {
      var result;

      result = {};
      if (this.field) {
        if (position.x) {
          result.x = (position.x + this.reality.width / 2 + this.reality.offx) * this.field.width / this.reality.width - this.field.width / 2;
        }
        if (position.y) {
          result.y = (position.y + this.reality.height / 2 + this.reality.offy) * this.field.height / this.reality.height - this.field.height / 2;
        }
      }
      return result;
    };

    Engine.prototype.get_canvas = function(target_div) {
      return this.renderer.domElement;
    };

    Engine.prototype.add = function(obj) {
      var drawable, _i, _len, _ref, _results;

      this.obj_stack.push(obj);
      _ref = obj.drawables;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        drawable = _ref[_i];
        _results.push(this.scene.add(drawable));
      }
      return _results;
    };

    Engine.prototype.render = function() {
      var obj, time, _i, _j, _len, _len1, _ref, _ref1;

      time = (new Date).getTime() - this.start_time;
      this.mean_ball_pos.x = this.mean_ball_cnt * this.mean_ball_pos.x + this.ball.ball.position.x;
      this.mean_ball_pos.x /= this.mean_ball_cnt + 1;
      this.mean_ball_pos.y = this.mean_ball_cnt * this.mean_ball_pos.y + this.ball.ball.position.y;
      this.mean_ball_pos.y /= this.mean_ball_cnt + 1;
      this.mean_ball_pos.z = this.mean_ball_cnt * this.mean_ball_pos.z + this.ball.ball.position.z;
      this.mean_ball_pos.z /= this.mean_ball_cnt + 1;
      switch (this.camera_mode) {
        case "BIRD":
          this.camera.position.set(0, 60, 0);
          this.camera.rotation.set(-Math.PI / 2, 0, 0);
          break;
        case "KEEPERA":
          this.camera.position.set(-60, 10, 0);
          this.camera.lookAt(this.mean_ball_pos);
          break;
        case "KEEPERB":
          this.camera.position.set(60, 10, 0);
          this.camera.lookAt(this.mean_ball_pos);
          break;
        default:
          this.camera.position.x = 5 * Math.cos(time / 1200);
          this.camera.position.y = 33 + 5 * Math.sin(time / 600);
          this.camera.position.z = 50 + 25 * Math.sin(time / 2700);
          this.camera.lookAt(this.mean_ball_pos);
      }
      _ref = this.obj_stack;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        obj = _ref[_i];
        obj.follow(this.camera.position, this.camera_mode === "BIRD");
      }
      _ref1 = this.obj_stack;
      for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
        obj = _ref1[_j];
        obj.animate(time);
      }
      return this.renderer.render(this.scene, this.camera);
    };

    Engine.prototype.select_players = function(plr_ids) {
      return this.players.forEach(function(v, i) {
        var _ref;

        return v.selected = (_ref = v.id, __indexOf.call(plr_ids, _ref) >= 0);
      });
    };

    return Engine;

  })();

  Drawable = (function() {
    function Drawable() {}

    Drawable.prototype.animate = function(time) {};

    Drawable.prototype.follow = function(target, reset) {
      var f, _i, _len, _ref, _results;

      if (reset == null) {
        reset = false;
      }
      _ref = this.followers;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        f = _ref[_i];
        _results.push((function(f) {
          if (reset) {
            return f.rotation.set(-Math.PI / 2, 0, 0);
          } else {
            return f.lookAt(target);
          }
        })(f));
      }
      return _results;
    };

    Drawable.prototype.toString = function() {
      return "Drawable";
    };

    return Drawable;

  })();

  Moveable = (function(_super) {
    __extends(Moveable, _super);

    function Moveable() {}

    Moveable.prototype.update = function(time, data) {
      if (data.x != null) {
        this.target_pos.x = data.x;
      }
      if (data.y != null) {
        this.target_pos.y = data.y;
      }
      if (time != null) {
        return this.last_update = time;
      }
    };

    Moveable.prototype.toString = function() {
      return "Moveable";
    };

    return Moveable;

  })(Drawable);

  Field = (function(_super) {
    __extends(Field, _super);

    function Field(texturefile, reality) {
      var geometry, mat_cfg, material;

      this.reality = reality;
      this.ratio = this.reality.width / this.reality.height;
      this.width = 120;
      this.height = 90;
      geometry = new THREE.PlaneGeometry(this.width, this.height);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture(texturefile),
        side: THREE.DoubleSide
      };
      material = new THREE.MeshLambertMaterial(mat_cfg);
      this.field = new THREE.Mesh(geometry, material);
      this.field.rotation.x = Math.PI / 2;
      this.drawables = [this.field];
      this.followers = [];
    }

    Field.prototype.toString = function() {
      return "Field";
    };

    return Field;

  })(Drawable);

  Ball = (function(_super) {
    __extends(Ball, _super);

    function Ball(texturefile) {
      var geometry, mat_cfg, material;

      geometry = new THREE.PlaneGeometry(2, 2);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture(texturefile),
        alphaTest: 0.5
      };
      material = new THREE.MeshBasicMaterial(mat_cfg);
      this.ball = new THREE.Mesh(geometry, material);
      geometry = new THREE.PlaneGeometry(1, 1);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture("img/shadow.png"),
        transparent: true
      };
      material = new THREE.MeshBasicMaterial(mat_cfg);
      this.shadow = new THREE.Mesh(geometry, material);
      this.shadow.rotation.x = -Math.PI / 2;
      this.shadow.position.y = 0.001;
      this.followers = [this.ball];
      this.drawables = [this.ball, this.shadow];
      this.ball.position.set(0, this.ball.geometry.height / 2, 0);
      this.last_update = 0;
      this.target_pos = {
        x: 0,
        y: 0
      };
      this.anim_factor = 10;
    }

    Ball.prototype.animate = function(time) {
      var s_scale;

      this.ball.position.x = (this.anim_factor * this.ball.position.x + this.target_pos.x) / (this.anim_factor + 1);
      this.ball.position.z = (this.anim_factor * this.ball.position.z + this.target_pos.y) / (this.anim_factor + 1);
      this.shadow.position.x = this.ball.position.x;
      this.shadow.position.z = this.ball.position.z;
      s_scale = 3.0 + this.ball.scale.x + this.ball.position.y - this.ball.geometry.height;
      return this.shadow.scale.set(s_scale, s_scale, s_scale);
    };

    Ball.prototype.toString = function() {
      return "Ball";
    };

    return Ball;

  })(Moveable);

  Player = (function(_super) {
    __extends(Player, _super);

    function Player(id, name, team, tricot_image) {
      var geometry, mat_cfg, material;

      this.id = id;
      this.name = name;
      this.team = team;
      this.tricot_image = tricot_image;
      geometry = new THREE.PlaneGeometry(4, 4);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture(this.tricot_image),
        transparent: true
      };
      material = new THREE.MeshBasicMaterial(mat_cfg);
      this.shirt = new THREE.Mesh(geometry, material);
      this.shirt.position.y = this.shirt.geometry.height / 2;
      geometry = new THREE.PlaneGeometry(2, 2);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture("img/shadow.png"),
        alpha: 0.5,
        transparent: true
      };
      material = new THREE.MeshBasicMaterial(mat_cfg);
      this.shadow = new THREE.Mesh(geometry, material);
      this.shadow.rotation.x = -Math.PI / 2;
      this.shadow.position.y = 0.005;
      geometry = new THREE.PlaneGeometry(5, 5);
      mat_cfg = {
        map: new THREE.ImageUtils.loadTexture("img/selection.png"),
        transparent: true
      };
      material = new THREE.MeshBasicMaterial(mat_cfg);
      this.select = new THREE.Mesh(geometry, material);
      this.select.rotation.x = -Math.PI / 2;
      this.select.position.y = 0.01;
      this.followers = [this.shirt];
      this.drawables = [this.shirt, this.shadow, this.select];
      this.last_update = 0;
      this.target_pos = {
        x: 0,
        y: 0
      };
      this.anim_factor = 10;
      this.selected = false;
    }

    Player.prototype.animate = function(time) {
      var s;

      this.shirt.position.x = (this.anim_factor * this.shirt.position.x + this.target_pos.x) / (this.anim_factor + 1);
      this.shirt.position.z = (this.anim_factor * this.shirt.position.z + this.target_pos.y) / (this.anim_factor + 1);
      this.shadow.position.x = this.shirt.position.x;
      this.shadow.position.z = this.shirt.position.z;
      this.shadow.scale.x = this.shirt.position.y;
      this.shadow.scale.y = this.shirt.position.y;
      this.shadow.scale.z = this.shirt.position.y;
      this.shadow.scale.x -= this.shirt.geometry.height;
      this.shadow.scale.y -= this.shirt.geometry.height;
      this.shadow.scale.z -= this.shirt.geometry.height;
      if (this.selected) {
        s = 1 * (1 + 0.2 * Math.sin(0.005 * time));
        this.select.scale.set(s, s, s);
        this.select.rotation.z = 0.01 * time;
        this.select.position.x = this.shirt.position.x;
        this.select.position.z = this.shirt.position.z;
        this.shirt.material.opacity = 1.0;
        return console.log(this.select.rotation.y);
      } else {
        this.select.scale.set(0, 0, 0);
        this.shirt.material.opacity = 0.5;
        return console.log(this.shirt);
      }
    };

    Player.prototype.toString = function() {
      return "Player(" + this.tricot_image + ")";
    };

    return Player;

  })(Moveable);

  console.log("# SEA - sport event analyzer");

  console.log("## Initializing");

  ball = new Ball("img/ball.png");

  engine = new Engine(ball);

  playersdict = {};

  tmp_team_name = "";

  tmp_counter = {
    "true": 0,
    "false": 0
  };

  console.log(Date.now());

  console.log("* starting animation loop");

  run = function() {
    requestAnimationFrame(run);
    return engine.render();
  };

  requestAnimationFrame(run);

  refresh_selection = function() {
    var all_plrs, plr_ids_a, plr_ids_b;

    plr_ids_a = [];
    plr_ids_b = [];
    $("#team_a").find("tbody").find("tr.ui-selected").each(function(i, t) {
      return plr_ids_a.push(t.id);
    });
    $("#team_b").find("tbody").find("tr.ui-selected").each(function(i, t) {
      return plr_ids_b.push(t.id);
    });
    $("#player_a_stats").find(".player_name").text(playersdict[plr_ids_a[0]].name);
    $("#player_b_stats").find(".player_name").text(playersdict[plr_ids_b[0]].name);
    all_plrs = plr_ids_a.concat(plr_ids_b);
    console.log(all_plrs);
    return engine.select_players(all_plrs);
  };

  add_player = function(v, i) {
    var color, plr, tableentry, tshirt;

    if (i === 0) {
      tmp_team_name = v.TeamName;
    }
    tmp_counter[v.TeamName === tmp_team_name] += 1;
    color = "rot";
    tableentry = '<tr id="' + v.PlayerID + '"><td>' + v.PlayerName + '</td><td class="smallinfo"></td></tr>';
    if (v.TeamName === tmp_team_name) {
      $("#team_a").find("tbody").append(tableentry);
      $("body").find(".team_a_name").text(v.TeamName);
      console.log(v.TeamName);
      color = "gelb";
    } else {
      $("#team_b").find("tbody").append(tableentry);
      $("body").find(".team_b_name").text(v.TeamName);
      console.log(v.TeamName);
    }
    tshirt = "img/trikot_" + color + "_" + tmp_counter[v.TeamName === tmp_team_name] + ".png";
    plr = new Player(v.PlayerID, v.PlayerName, v.TeamName, tshirt);
    console.log("Player: " + v.PlayerName + " (ID: " + v.PlayerID + ", Team: " + v.TeamName + ", Tshirt: " + tshirt + ")");
    engine.add(plr);
    engine.players.push(plr);
    playersdict[v.PlayerID] = plr;
    return playersdict["" + v.PlayerID] = plr;
  };

  update_position = function(v, i) {
    var data, _ref;

    console.log(v);
    switch (v.constructor.name) {
      case "BallPosition":
        data = {
          y: parseInt(v.positionY),
          x: parseInt(v.positionZ)
        };
        data = engine.reposition(data);
        return ball.update(0, data);
      case "PlayerPosition":
        data = {
          y: parseInt(v.positionX),
          x: parseInt(v.positionY)
        };
        return (_ref = playersdict[v.id]) != null ? _ref.update(0, engine.reposition(data)) : void 0;
    }
  };

  establish_sea_connection = function(onsuccess) {
    return sea.connect("seaclient@sea/Client", "sea", "mobilis@sea", function() {
      sea.getGameMappings(function(mappings) {
        var field, gf, reality;

        console.log("* Setting up field");
        gf = mappings.GameFieldSize;
        console.log(gf);
        reality = {
          width: gf.GameFieldMaxX - gf.GameFieldMinX,
          height: gf.GameFieldMaxY - gf.GameFieldMinY,
          offx: parseInt(gf.GameFieldMinX),
          offy: parseInt(gf.GameFieldMinY)
        };
        field = new Field("img/Fussballfeld.png", reality);
        engine.set_field(field);
        console.log("* Setting up goal positions and size");
        console.log("* Setting up players");
        console.log(mappings.PlayerMappings);
        return mappings.PlayerMappings.forEach(add_player);
      });
      console.log("* adding pos handler");
      sea.pubsub.addCurrentPositionDataHandler(function(item) {
        return item.positionNodes.forEach(update_position);
      });
      sea.pubsub.subscribeStatistic();
      return typeof onsuccess === "function" ? onsuccess() : void 0;
    });
  };

  $(function() {
    var b, _fn, _i, _len, _ref;

    $("#content").hide();
    console.log("* preparing view buttons");
    $("#perspectives_menu").buttonset;
    _ref = $("#perspectives_menu").find("input");
    _fn = function(b) {
      return b.onclick = function() {
        return engine.camera_mode = b.id;
      };
    };
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      b = _ref[_i];
      _fn(b);
    }
    console.log("* adding canvas");
    $("#field").append(engine.get_canvas());
    console.log("* making selectables");
    $("#team_a, #team_b").selectable({
      filter: 'tr',
      selected: function(event, ui) {
        return refresh_selection();
      }
    });
    return $("#startbutton").button().click(function(event) {
      $(this).html('<img src="img/spinner_animated.svg"/>');
      return establish_sea_connection(function() {
        return $("#content").show().fadeIn("slow", function() {
          return $("#startbutton").fadeOut("fast");
        });
      });
    });
  });

}).call(this);
