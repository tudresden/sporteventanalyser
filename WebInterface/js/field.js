var camera, scene, renderer, amb_light,
    geometry, material, field, ball, ballshadow;

var start_time = (new Date()).getTime();

var bg_color = 0xfffff0;
var resolution = [640, 480];
var cam_mode = "BIRD";

var field_tex = new THREE.ImageUtils.loadTexture("img/Soccer_field_-_empty1.png");
//field_tex.wrapS = field_tex.wrapT = THREE.RepeatWrapping;
//field_tex.repeat.set(10, 10);
var field = {
  width: 120,
  depth: 90,
  map: field_tex
};

$(document).ready(function() {
  init();
  animate();

  $("#perspectives_menu li button").each(function (i, b) {
    b.onclick = function() { cam_mode = b.id; };
  });

  window.setTimeout("cam_mode=\"TAUMEL\";", 10000);
});

function init() {
  scene = new THREE.Scene();

  camera = new THREE.PerspectiveCamera( 75, resolution[0] / resolution[1], 1, 10000 );
  camera.position.z = 100;
  camera.position.y = 33;

  geometry = new THREE.PlaneGeometry( field.width, field.depth );
  material = new THREE.MeshLambertMaterial({
      map: field.map,
      side: THREE.DoubleSide
  });

  field = new THREE.Mesh( geometry, material );
  field.rotation.x = Math.PI/2;
  scene.add(field);

  ball = new THREE.Mesh(
      new THREE.PlaneGeometry(2, 2),
      new THREE.MeshBasicMaterial({
        map: new THREE.ImageUtils.loadTexture("img/ball.png"),
        side: THREE.DoubleSide,
        alphaTest: 0.5
      }));
  ball.rotation.x = Math.PI/2;
  ball.position.y = 0.1;
  scene.add(ball);

  ballshadow = new THREE.Mesh(
    new THREE.PlaneGeometry(1, 1),
    new THREE.MeshBasicMaterial({
      color: 0x003300,
      side: THREE.DoubleSide
      })
    );
  ballshadow.rotation.x = Math.PI/2;
  ballshadow.position.y = 0.1;
  console.log(ballshadow.scale);
  scene.add(ballshadow);

  amb_light = new THREE.AmbientLight(0xffffff, 1.0);
  scene.add(amb_light);

  renderer = new THREE.WebGLRenderer();
  renderer.setSize(resolution[0], resolution[1]);

  $("#field").append(renderer.domElement);
}

function animate() {
  // note: three.js includes requestAnimationFrame shim
  requestAnimationFrame( animate );
  render();
}

function render() {
  var time = (new Date).getTime() - start_time;

  ball.lookAt(camera.position);
  ball.position.y = ball.geometry.height/2 + Math.abs(ball.geometry.height * Math.sin(time / 120));
  ball.position.x = ball.geometry.height/2 + Math.abs(ball.geometry.height * Math.cos(time / 240));
  ballshadow.position.x = ball.position.x;
  ballshadow.position.z = ball.position.z;
  ballshadow.scale.x = ball.position.y - ball.geometry.height;
  ballshadow.scale.y = ball.position.y - ball.geometry.height;

  switch(cam_mode) {
    case "BIRD":
      camera.position.y = 60;
      camera.position.x = 0;
      camera.position.z = 0;
      camera.rotation.set(-Math.PI/2, 0, 0);
      break;
    default:
      camera.position.y = 33 + 5 * Math.sin(time / 600);
      camera.position.x = 5 * Math.cos(time / 1200);
      camera.position.z = 50 + 25 * Math.sin(time / 2700);
      camera.lookAt(field.position);
  }

  renderer.render( scene, camera );
}
