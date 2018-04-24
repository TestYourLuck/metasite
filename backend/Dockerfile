# Dockerfile
FROM  phusion/baseimage:0.9.17
# Update existing libraries
RUN apt-get -y update
# Install needed libraries
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y -q python-software-properties software-properties-common
# Set environment variables
ENV JAVA_VER 8
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle
# Install JDK, since opensource java is not suitable
RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list  && \
    echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list  && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886 && \
    apt-get update && \
    echo oracle-java${JAVA_VER}-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections && \
    apt-get install -y --force-yes --no-install-recommends oracle-java${JAVA_VER}-installer oracle-java${JAVA_VER}-set-default && \
    apt-get clean && \
    rm -rf /var/cache/oracle-jdk${JAVA_VER}-installer
# Update java
RUN update-java-alternatives -s java-8-oracle
# Set JAVA_HOME on startup
RUN echo "export JAVA_HOME=/usr/lib/jvm/java-8-oracle" >> ~/.bashrc
# Cleanup
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
# Copy fat jar
ADD target/backend-app-fat.jar /usr/lib/metasite/apps/backend-app-fat.jar
# Run application
CMD java ${JAVA_OPTS} -jar /usr/lib/metasite/apps/backend-app-fat.jar -DoutputDir="${results_directory}"
# Make application accessible on port 8080
EXPOSE 8080