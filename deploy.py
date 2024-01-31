import os
import time

os.system("rm -rf /var/lib/tomcat9/webapps/payout.war")
print("Payout Removed War")
time.sleep(10)
os.system("cp /home/ubuntu/payout.war /var/lib/tomcat9/webapps/payout.war")
print("Payout Copy War")
time.sleep(10)
print("Payout Restart Complete")