echo "post install"

# script variables
service='tibco-stub-and-mock'
targetdir='/opt/'$service

username='tomcat'
groupname='tomcat'

if [ $1 -gt 1 ]; then
	service $service stop
fi

chmod +x /etc/init.d/$service
chown -R $username:$groupname $targetdir 

# log directories
mkdir -p /var/log/$service /var/log/$service/mdumps 
chown $username:$groupname -R /var/log/$service

# backup log direcroty if exist
if [ -d "$targetdir/logs" ] && [ ! -L "$targetdir/logs" ];
then
    mv $targetdir/logs $targetdir/logs_bkp
fi

# prepare targetdir
if [ ! -d $targetdir ];
then
    mkdir -p $targetdir
fi

# create log symlink 
if [ ! -L $targetdir/logs ];
then
    ln -s /var/log/$service $targetdir/logs
    chown $username:$groupname -R $targetdir/logs
fi

# set run level
chkconfig --add $service
chkconfig --level 35 $service on

service $service start