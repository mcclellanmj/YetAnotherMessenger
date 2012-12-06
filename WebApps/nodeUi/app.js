var express = require('express');
var app = express();
var stylus = require('stylus');
var nib = require('nib');
var packageInfo = require('./package.json');
var server = require('http').createServer(app);
var io = require('socket.io').listen(server);
var logFactory = require('simply-log');
logFactory.setDefaultLevel(logFactory.TRACE).addDefaultAppender(function(name, level, args) {
	args.unshift(Date.now() + ' ' + name + ':' + level + ' ->');
	Function.prototype.apply.call(console.log, console, args);
});

var ioInternalLogger = logFactory.getLogger('io.internal');
io.set('logger', ioInternalLogger);
io.set('log level', logFactory.TRACE);

var log = logFactory.getLogger('root');
log.info('Application starting');

function compile(str, path) {
  return stylus(str).set('filename', path).use(nib());
}

io.sockets.on('connection', function(socket) {
	var log = logFactory.getLogger('io');
	log.trace('got new connection');

	var id = setInterval(function() {
		log.trace('emitting ', process.memoryUsage());
		socket.emit('memory', process.memoryUsage());
	}, 1000);

	socket.on('disconnect', function () {
		log.trace('disconnect recieved');
    	clearInterval(id);
  	});
});

// Define views directory
app.set('views', __dirname + '/views');

// Use jade as view engine
app.set('view engine', 'jade');

// Use stylus as css engine
app.use(stylus.middleware({ src: __dirname + '/public', compile: compile}));
app.use(express.cookieParser('secr3t'));
app.use(express.bodyParser());

// Define public directory
app.use(express.static(__dirname + '/public'));

app.all('/messenger*', function(req, res, next) {
	var log = logFactory.getLogger('filter');
	log.trace('entering authentication filter');
	if(req.signedCookies.token == undefined) {
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
	res.cookie('token', req.body.username, {signed: true});
	res.redirect('/');
});

var port = 3000;
server.listen(port);
log.info('Application started and listening on port', port);