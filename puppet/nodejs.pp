class nodejs {
	$nodePackages = ["nodejs", "npm"]

	package { $nodePackages : ensure => installed }
}

require nodejs
