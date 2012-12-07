var express = require('express');
var app = express();
var stylus = require('stylus');
var nib = require('nib');
var packageInfo = require('./package.json');
var logFactory = require('simply-log');
var expRedisStore = require('connect-redis')(express);
var redis = require("redis").createClient();
logFactory.setDefaultLevel(logFactory.TRACE).addDefaultAppender(function(name, level, args) {
	args.unshift(Date.now() + ' ' + name + ':' + level + ' ->');
	Function.prototype.apply.call(console.log, console, args);
});

var log = logFactory.getLogger('root');
log.info('Application starting');

function compile(str, path) {
  return stylus(str).set('filename', path).use(nib());
}

// Define views directory
app.set('views', __dirname + '/views');

// Use jade as view engine
app.set('view engine', 'jade');

var concreteStore = new expRedisStore({ host: 'localhost', port: 6092, client: redis });
// Use stylus as css engine
app.use(stylus.middleware({ src: __dirname + '/public', compile: compile}));
var cookieParser = express.cookieParser('s3cr3t');
app.use(cookieParser);
app.use(express.bodyParser());
app.use(express.session({
      secret: "s3cr3t",
      store: concreteStore
}));

// Define public directory
app.use(express.static(__dirname + '/public'));

app.all('/messenger*', function(req, res, next) {
	var log = logFactory.getLogger('filter');
	log.trace('entering authentication filter');
	log.trace('username is ', req.session.username);
	if(req.session.username == undefined) {
		log.trace('No cookie found, redirecting to login');
		res.redirect('/login');
		return;
	}
	next();
});

app.get('/', function (req, res) {
	var log = logFactory.getLogger('index');
	log.trace('Redirecting to messenger');
	res.redirect('/messenger');
});

app.get('/messenger', function (req, res) {
	var log = logFactory.getLogger('messenger');
	res.render('index', {'packageInfo' : packageInfo});
	log.debug('User cookie found:', req.signedCookies.token);
});

app.get('/login', function(req, res) {
	var log = logFactory.getLogger('login');
	log.trace('Rendering login');
	res.render('login', {'packageInfo' : packageInfo});
});

app.post('/login', function(req, res) {
	var log = logFactory.getLogger('login');
	log.trace('Login recieved from ' + req.body.username);
	req.session.username = req.body.username;
	res.redirect('/');
});

var port = 3000;
var server = app.listen(port);
log.info('Application started and listening on port', port);

var io = require('socket.io').listen(server);
var RedisStore = require('socket.io/lib/stores/redis')
  , redis  = require('socket.io/node_modules/redis')
  , pub    = redis.createClient()
  , sub    = redis.createClient()
  , client = redis.createClient();

io.set('store', new RedisStore({
  redisPub : pub
, redisSub : sub
, redisClient : client
}));

var ioInternalLogger = logFactory.getLogger('io.internal');
io.set('logger', ioInternalLogger);
io.set('log level', logFactory.TRACE);

var SessionSockets = require('session.socket.io');
var sessionSockets = new SessionSockets(io, concreteStore, cookieParser);

sessionSockets.on('connection', function(err, socket, session) {
	var log = logFactory.getLogger('io');
	var id = session.username;
	log.trace('got new connection for ' + id);

	socket.on('clientMessage', function(message) {
		log.trace('recieved message ', message);
		io.sockets.emit('serverMessage', id + ": " + message);
	});

	socket.on('disconnect', function () {
		log.trace('disconnect recieved');
  	});
});