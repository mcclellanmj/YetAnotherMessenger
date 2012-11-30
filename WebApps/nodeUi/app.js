var express = require('express');
var stylus = require('stylus');
var nib = require('nib');
var packageInfo = require('./package.json');

var logFactory = require('simply-log');
logFactory.setDefaultLevel(logFactory.TRACE).addDefaultAppender(function(name, level, args) {
		args.unshift(Date.now() + ' ' + name + ':' + level + ' ->');
		Function.prototype.apply.call(console.log, console, args);
});

var log = logFactory.getLogger('root');
log.info('Application starting');

function compile(str, path) {
  return stylus(str).set('filename', path).use(nib());
}

var app = express();

// Define views directory
app.set('views', __dirname + '/views');

// Use jade as view engine
app.set('view engine', 'jade');

// Use stylus as css engine
app.use(stylus.middleware({ src: __dirname + '/public', compile: compile}));
app.use(express.cookieParser('secr3t'));

// Define public directory
app.use(express.static(__dirname + '/public'));

app.get('/', function (req, res) {
	var log = logFactory.getLogger('router');
	if(req.signedCookies.name == undefined) {
		res.cookie('name', 'john', {signed: true});
		log.trace('authentication cookie set');
	}
  res.render('index', {'packageInfo' : packageInfo});
  log.debug('User cookie found:', req.signedCookies.name);
});

var port = 3000;
app.listen(port);
log.info('Application listening on port', port);