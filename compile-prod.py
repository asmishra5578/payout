import os
import paramiko

ipaddress = '13.234.68.151'  # Transaction server
# ipaddress = '3.109.172.81' #Status server
#ipaddress = '65.0.203.43' #Admin server

os.system("./mvnw clean install -DskipTests")
os.system("scp -i /home/analytiq/Desktop/Keys/appservermobile.pem /home/analytiq/Documents/Developer/asktechpayout/target/PaytmPayout-0.0.1.war ubuntu@" +
          ipaddress+":/home/ubuntu/payout.war")
os.system("scp -i /home/analytiq/Desktop/Keys/appservermobile.pem /home/analytiq/Documents/Developer/asktechpayout/deploy.py ubuntu@" +
          ipaddress+":/home/ubuntu/deploy.py")
k = paramiko.RSAKey.from_private_key_file(
    "/home/analytiq/Desktop/Keys/appservermobile.pem")
con = paramiko.SSHClient()
con.set_missing_host_key_policy(paramiko.AutoAddPolicy())
print("connecting")
con.connect(hostname=ipaddress, username="ubuntu", pkey=k)
print("connected")
commands = ["sudo python3 /home/ubuntu/deploy.py"]
for command in commands:
    print("Executing {}".format(command))
    stdin, stdout, stderr = con.exec_command(command)
    print(stdout.read())
con.close()
