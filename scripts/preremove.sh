echo "running pre uninstallation script"

# script variables
service='tibco-stub-and-mock'

if [ "$1" = 0 ] ; then
    service $service stop
    chkconfig --del $service
fi
