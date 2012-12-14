class redis {
	package { 'redis-server' : ensure => installed }
}

require redis
