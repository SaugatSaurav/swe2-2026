# brotundbutter-mini
## before you start
# run
bin/configure-local.sh  
# to get config data 


## build cycle 
# run
bin/build.sh  
bin/clean.sh  

## configure for hopper/docker
# edit local/config.txt
webapp=docker-XXX-java
manager=docker-XXX-manager
baseurl=https://informatik.hs-bremerhaven.de/
# add to local/netrc
machine informatik.hs-bremerhaven.de login manager password <password in ~/.my.cnf>

