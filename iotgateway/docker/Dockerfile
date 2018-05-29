FROM java:8
WORKDIR /
ADD iotlab.us.oracle.com.cer iotlab.us.oracle.com.cer
ADD start.sh start.sh
ADD car2 car2
ADD iotgateway.jar iotgateway.jar
RUN \
keytool -importcert -alias iotlabs \
-keystore /usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/security/cacerts \
-file iotlab.us.oracle.com.cer -storepass changeit -noprompt
EXPOSE 8080
CMD /start.sh
