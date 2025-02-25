import subprocess
import re
import os
import socket
import time
from argparse import ArgumentParser

# not support python3 due to new `check_output` returns bytes
# and some of the string concact not work

def find_and_kill_old_process():
    print("finding old pid...\n")

    try:
        pid = subprocess.check_output("lsof -t -i :8080 -s TCP:LISTEN", shell=True)
        print("find out pid: " + pid + "\n")
        print("start killing process...\n")
        subprocess.check_output("kill {pid}".format(pid=pid.replace("\n", "").strip()), shell=True)
        time.sleep(5)
        # recheck pid and process running on
        pid_exist = subprocess.call("ps -p {pid}".format(pid=pid.replace("\n", "").strip()), shell=True)
        pid_on_port_exist = subprocess.call("lsof -t -i :8080 -s TCP:LISTEN", shell=True)
        if pid_exist == 0 or pid_on_port_exist == 0:
            raise Exception("pid still exist: " + pid)
        print("killed finished! \n")
    except subprocess.CalledProcessError:
        print("no process running on 8080 or process has already been killed \n")


def mvn_clean_and_install():
    print("cleaning mvn...\n")
    result = subprocess.check_output("mvn clean", shell=True)
    if (len(re.findall(".+Finished at(.+)", str(result))) != 0):
        print("clean finished! \n")
    print("start building...\n")
    result = subprocess.check_output("mvn install -DskipTests", shell=True)
    if (len(re.findall(".+Finished at(.+)", str(result))) != 0):
        print("build finished! \n")


def start_server(env):
    hostname = socket.gethostname()
    print("starting server...\n");
    # prod env requires both env and hostname
    if (env == "prod" and "prod" in hostname):
        print("using production config file...\n");
        result = subprocess.call("nohup java -jar target/thoth-0.1.0.jar --spring.profiles.active=prod > /dev/null 2>&1&", shell=True)
    elif (env == "stage" or "stage" in hostname):
        print("using stage config file...\n");
        result = subprocess.call("nohup java -jar target/thoth-0.1.0.jar --spring.profiles.active=stage > /dev/null 2>&1&", shell=True)
    # prod env requires both env and hostname
    elif (env == "tagprod" and "tagram" in hostname):
        print("using profile tag config file...\n");
        result = subprocess.call("nohup java -jar target/thoth-0.1.0.jar --spring.profiles.active=tagprod > /dev/null 2>&1&", shell=True)
    else:
        print("using default config file...\n");
        result = subprocess.call("nohup java -jar target/thoth-0.1.0.jar --spring.profiles.active=default > /dev/null 2>&1&", shell=True)

    print("sleep for a while...due to model involved we should wait up to 50 seconds\n")
    # NOTICE: need loading model and embedding
    time.sleep(45)

    try:
        result =subprocess.check_output("curl localhost:8080/thoth/ping", shell=True)
        if (len(re.findall("pong", result)) > 0):
            print("server started success!\n")
        else:
            raise Exception("error")
    except:
        print("error occured during server starting:\n")


def main(args):

    # git pull
    print("git pulling...\n")
    git_result = subprocess.check_output("git pull", shell=True)
    if (bool(re.search(r"Already up(\s|-)to(\s|-)date.", str(git_result)))):
        print(str(git_result))
    else:
        print("git pull finished!\n")

    # find and kill old process
    find_and_kill_old_process()

    # TODO: recheck process

    # clean and recompile
    if (args.rebuild):
        mvn_clean_and_install()

    # start server
    start_server(args.environment)


parser = ArgumentParser()
parser.add_argument("-env", "--env", dest="environment", nargs='?',
                    help="valid env: local, stage, prod, tagprod", default="local")
parser.add_argument("-r", "--reset", dest="rebuild", nargs='?',
                    help="whether rebuild on deploy, default to False", default=True)


args = parser.parse_args()
main(args)
